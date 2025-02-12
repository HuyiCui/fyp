package com.example.mapper;

import com.example.entity.Admin;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Operate admin related data interface
*/
public interface AdminMapper {

    /**
      * insert
    */
    int insert(Admin admin);

    /**
      * delete
    */
    int deleteById(Integer id);

    /**
      * update
    */
    int updateById(Admin admin);

    /**
      * selectById
    */
    Admin selectById(Integer id);

    /**
      * selectAll
    */
    List<Admin> selectAll(Admin admin);

    @Select("select * from admin where username = #{username}")
    Admin selectByUsername(String username);
}