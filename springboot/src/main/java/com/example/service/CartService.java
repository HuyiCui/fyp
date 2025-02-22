package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Cart;
import com.example.mapper.CartMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Cart processing
 **/
@Service
public class CartService {

    @Resource
    private CartMapper cartMapper;

    /**
     * add
     */
    public void add(Cart cart) {
        Cart dbCart =cartMapper.selectByUserIdAndGoodsId(cart.getUserId(), cart.getGoodsId());
        if(ObjectUtil.isNotEmpty(dbCart)){
            dbCart.setNum(dbCart.getNum()+1);
            cartMapper.updateById(dbCart);
        } else {
            cartMapper.insert(cart);
        }
    }

    /**
     * deleteById
     */
    public void deleteById(Integer id) {
        cartMapper.deleteById(id);
    }

    /**
     * deleteBatch
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            cartMapper.deleteById(id);
        }
    }

    /**
     * updateById
     */
    public void updateById(Cart cart) {
        cartMapper.updateById(cart);
    }

    /**
     * selectById
     */
    public Cart selectById(Integer id) {
        return cartMapper.selectById(id);
    }

    /**
     * selectAll
     */
    public List<Cart> selectAll(Cart cart) {
        return cartMapper.selectAll(cart);
    }

    /**
     * selectPage
     */
    public PageInfo<Cart> selectPage(Cart cart, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            cart.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Cart> list = cartMapper.selectAll(cart);
        return PageInfo.of(list);
    }

}