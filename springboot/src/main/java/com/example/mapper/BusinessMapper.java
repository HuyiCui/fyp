package com.example.mapper;

import com.example.entity.Business;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Operate business related data interface
*/
public interface BusinessMapper {

    /**
      * insert
    */
    int insert(Business business);

    /**
      * delete
    */
    int deleteById(Integer id);

    /**
      * update
    */
    int updateById(Business business);

    /**
      * selectById
    */
    Business selectById(Integer id);

    /**
      * selectAll
    */
    List<Business> selectAll(Business business);

    @Select("select * from business where username = #{username}")
    Business selectByUsername(String username);
}