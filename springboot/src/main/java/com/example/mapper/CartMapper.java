package com.example.mapper;

import com.example.entity.Cart;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Operation notice related data interface
*/
public interface CartMapper {

    /**
     * insert
     */
    int insert(Cart cart);

    /**
     * deleteById
     */
    int deleteById(Integer id);

    /**
     * updateById
     */
    int updateById(Cart cart);

    /**
     * selectById
     */
    Cart selectById(Integer id);

    /**
     * selectAll
     */
    List<Cart> selectAll(Cart cart);

    @Select("select * from cart where user_id = #{userId} and goods_id = #{goodsId}")
    Cart selectByUserIdAndGoodsId(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId);
}