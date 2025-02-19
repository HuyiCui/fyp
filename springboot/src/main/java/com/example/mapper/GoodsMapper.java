package com.example.mapper;

import com.example.entity.Goods;

import java.util.List;

/**
 * Operation notice related data interface
*/
public interface GoodsMapper {

    /**
     * insert
     */
    int insert(Goods goods);

    /**
     * deleteById
     */
    int deleteById(Integer id);

    /**
     * updateById
     */
    int updateById(Goods goods);

    /**
     * selectById
     */
    Goods selectById(Integer id);

    /**
     * selectAll
     */
    List<Goods> selectAll(Goods goods);

}