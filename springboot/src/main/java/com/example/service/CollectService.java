package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Collect;
import com.example.exception.CustomException;
import com.example.mapper.CollectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Collect processing
 **/
@Service
public class CollectService {

    @Resource
    private CollectMapper collectMapper;

    /**
     * add
     */
    public void add(Collect collect) {
        //Determine whether the user has collected the item. If so, remind the user not to collect it again.
        Collect dbCollect = collectMapper.selectByUserIdAndGoodsId(collect.getUserId(), collect.getGoodsId());
        if (ObjectUtil.isNotEmpty(dbCollect)) {
            throw new CustomException(ResultCodeEnum.COLLECT_ALREADY_ERROR);
        }
        collectMapper.insert(collect);
    }

    /**
     * deleteById
     */
    public void deleteById(Integer id) {
        collectMapper.deleteById(id);
    }

    /**
     * deleteBatch
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            collectMapper.deleteById(id);
        }
    }

    /**
     * updateById
     */
    public void updateById(Collect collect) {
        collectMapper.updateById(collect);
    }

    /**
     * selectById
     */
    public Collect selectById(Integer id) {
        return collectMapper.selectById(id);
    }

    /**
     * selectAll
     */
    public List<Collect> selectAll(Collect collect) {
        return collectMapper.selectAll(collect);
    }

    /**
     * selectPage
     */
    public PageInfo<Collect> selectPage(Collect collect, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Collect> list = collectMapper.selectAll(collect);
        return PageInfo.of(list);
    }

}