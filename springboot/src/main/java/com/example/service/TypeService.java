package com.example.service;

import com.example.entity.Type;
import com.example.mapper.TypeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Classification information table business processing
 **/
@Service
public class TypeService {

    @Resource
    private TypeMapper typeMapper;

    /**
     * add
     */
    public void add(Type type) {
        typeMapper.insert(type);
    }

    /**
     * deleteById
     */
    public void deleteById(Integer id) {
        typeMapper.deleteById(id);
    }

    /**
     * deleteBatch
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            typeMapper.deleteById(id);
        }
    }

    /**
     * updateById
     */
    public void updateById(Type type) {
        typeMapper.updateById(type);
    }

    /**
     * selectById
     */
    public Type selectById(Integer id) {
        return typeMapper.selectById(id);
    }

    /**
     * selectAll
     */
    public List<Type> selectAll(Type type) {
        return typeMapper.selectAll(type);
    }

    /**
     * selectPage
     */
    public PageInfo<Type> selectPage(Type type, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Type> list = typeMapper.selectAll(type);
        return PageInfo.of(list);
    }

}