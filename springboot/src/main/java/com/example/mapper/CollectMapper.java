package com.example.mapper;

import com.example.entity.Collect;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Operation notice related data interface
*/
public interface CollectMapper {

    /**
     * insert
     */
    int insert(Collect collect);

    /**
     * deleteById
     */
    int deleteById(Integer id);

    /**
     * updateById
     */
    int updateById(Collect collect);

    /**
     * selectById
     */
    Collect selectById(Integer id);

    /**
     * selectAll
     */
    List<Collect> selectAll(Collect collect);

    @Select("select * from collect where user_id = #{userId} and goods_id = #{goodsId}")
    Collect selectByUserIdAndGoodsId(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId);
}