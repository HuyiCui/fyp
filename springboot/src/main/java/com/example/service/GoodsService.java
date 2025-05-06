package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.mapper.*;
import com.example.utils.CFAlgo;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Classification information table business processing
 **/
@Service
public class GoodsService {

    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CollectMapper collectMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private CartMapper cartMapper;
    @Resource
    private OrdersMapper ordersMapper;
    /**
     * add
     */
    public void add(Goods goods) {
        Account currentUser = TokenUtils.getCurrentUser();
        if(RoleEnum.BUSINESS.name().equals(currentUser.getRole())) {
            goods.setBusinessId(currentUser.getId());
        }
        goodsMapper.insert(goods);
    }

    /**
     * deleteById
     */
    public void deleteById(Integer id) {
        goodsMapper.deleteById(id);
    }

    /**
     * deleteBatch
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            goodsMapper.deleteById(id);
        }
    }

    /**
     * updateById
     */
    public void updateById(Goods goods) {
        goodsMapper.updateById(goods);
    }

    /**
     * selectById
     */
    public Goods selectById(Integer id) {
        return goodsMapper.selectById(id);
    }

    /**
     * selectAll
     */
    public List<Goods> selectAll(Goods goods) {
        return goodsMapper.selectAll(goods);
    }

    /**
     * selectPage
     */
    public PageInfo<Goods> selectPage(Goods goods, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if(RoleEnum.BUSINESS.name().equals(currentUser.getRole())) {
            goods.setBusinessId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> list = goodsMapper.selectAll(goods);
        return PageInfo.of(list);
    }

    public List<Goods> selectTop15() {
        return goodsMapper.selectTop15();
    }

    public List<Goods> selectByTypeId(Integer id) {
        return goodsMapper.selectByTypeId(id);
    }

    public List<Goods> selectByBusinessId(Integer id) {
        return goodsMapper.selectByBusinessId(id);
    }

    public List<Goods> selectByName(String name) {
        return goodsMapper.selectByName(name);
    }

    public List<Goods> recommend() {
        Account currentUser = TokenUtils.getCurrentUser();
        if (ObjectUtil.isEmpty(currentUser)) {
            return new ArrayList<>();
        }
        List<Collect> allCollects = collectMapper.selectAll(null);
        List<Cart> allCarts = cartMapper.selectAll(null);
        List<Orders> allOrders = ordersMapper.selectAllOKOrders();
        List<Comment> allComments = commentMapper.selectAll(null);
        List<User> allUsers = userMapper.selectAll(null);
        List<Goods> allGoods = goodsMapper.selectAll(null);

        List<RelateDTO> data = new ArrayList<>();
        List<Goods> recommendResult;

        //Creating a Barrier, wait for all asynchronous processing to complete before moving on
        CountDownLatch countDownLatch = new CountDownLatch(allGoods.size() * allUsers.size());
        //Creating a thread pool
        ExecutorService threadPool = Executors.newCachedThreadPool();

        for (Goods goods : allGoods) {
            Integer goodsId = goods.getId();
            for (User user : allUsers) {
                threadPool.execute(() -> {
                    Integer userId = user.getId();
                    int index = 1;
                    Optional<Collect> collectOptional = allCollects.stream().filter(x -> x.getGoodsId().equals(goodsId) && x.getUserId().equals(userId)).findFirst();
                    if (collectOptional.isPresent()) {
                        index += 1;
                    }
                    Optional<Cart> cartOptional = allCarts.stream().filter(x -> x.getGoodsId().equals(goodsId) && x.getUserId().equals(userId)).findFirst();
                    if (cartOptional.isPresent()) {
                        index += 2;
                    }
                    Optional<Orders> ordersOptional = allOrders.stream().filter(x -> x.getGoodsId().equals(goodsId) && x.getUserId().equals(userId)).findFirst();
                    if (ordersOptional.isPresent()) {
                        index += 3;
                    }
                    Optional<Comment> commentOptional = allComments.stream().filter(x -> x.getGoodsId().equals(goodsId) && x.getUserId().equals(userId)).findFirst();
                    if (commentOptional.isPresent()) {
                        index += 2;
                    }
                    if (index > 1) {
                        RelateDTO relateDTO = new RelateDTO(userId, goodsId, index);
                        data.add(relateDTO);
                    }
                    countDownLatch.countDown();
                });
            }
        }

        try{
            countDownLatch.await();
            threadPool.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            List<Integer> goodsIds = CFAlgo.recommend(currentUser.getId(), data);

            recommendResult = goodsIds.stream().map(goodsId -> allGoods.stream()
                            .filter(x -> x.getId().equals(goodsId)).findFirst().orElse(null))
                    .limit(10).collect(Collectors.toList());
        }

        return recommendResult;
    }

    private List<Goods> getRandomGoods(int num) {
        List<Goods> list = new ArrayList<>(num);
        List<Goods> goods = goodsMapper.selectAll(null);
        for (int i = 0; i < num; i++) {
            int index = new Random().nextInt(goods.size());
            list.add(goods.get(index));
        }
        return list;
    }
}