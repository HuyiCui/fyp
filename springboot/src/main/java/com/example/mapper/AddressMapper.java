package com.example.mapper;

import com.example.entity.Address;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Operation notice related data interface
*/
public interface AddressMapper {

    /**
     * insert
     */
    int insert(Address address);

    /**
     * deleteById
     */
    int deleteById(Integer id);

    /**
     * updateById
     */
    int updateById(Address address);

    /**
     * selectById
     */
    Address selectById(Integer id);

    /**
     * selectAll
     */
    List<Address> selectAll(Address address);

}