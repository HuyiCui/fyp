package com.example.mapper;

import com.example.entity.Orders;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Operation notice related data interface
*/
public interface OrdersMapper {

    /**
     * insert
     */
    int insert(Orders orders);

    /**
     * deleteById
     */
    int deleteById(Integer id);

    /**
     * updateById
     */
    int updateById(Orders orders);

    /**
     * selectById
     */
    Orders selectById(Integer id);

    /**
     * selectAll
     */
    List<Orders> selectAll(Orders orders);
}