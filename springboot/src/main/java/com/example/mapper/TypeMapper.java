package com.example.mapper;

import com.example.entity.Type;

import java.util.List;

/**
 * Operation notice related data interface
*/
public interface TypeMapper {

    /**
     * insert
     */
    int insert(Type type);

    /**
     * deleteById
     */
    int deleteById(Integer id);

    /**
     * updateById
     */
    int updateById(Type type);

    /**
     * selectById
     */
    Type selectById(Integer id);

    /**
     * selectAll
     */
    List<Type> selectAll(Type type);

}