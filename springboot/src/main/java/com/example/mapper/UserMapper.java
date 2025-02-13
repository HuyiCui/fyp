package com.example.mapper;

import com.example.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Operate user related data interface
*/
public interface UserMapper {

    /**
      * insert
    */
    int insert(User user);

    /**
      * delete
    */
    int deleteById(Integer id);

    /**
      * update
    */
    int updateById(User user);

    /**
      * selectById
    */
    User selectById(Integer id);

    /**
      * selectAll
    */
    List<User> selectAll(User user);

    @Select("select * from user where username = #{username}")
    User selectByUsername(String username);
}