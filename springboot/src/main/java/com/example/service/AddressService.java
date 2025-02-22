package com.example.service;


import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Address;
import com.example.mapper.AddressMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Address processing
 **/
@Service
public class AddressService {

    @Resource
    private AddressMapper addressMapper;

    /**
     * add
     */
    public void add(Address address) {
        addressMapper.insert(address);
    }

    /**
     * deleteById
     */
    public void deleteById(Integer id) {
        addressMapper.deleteById(id);
    }

    /**
     * deleteBatch
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            addressMapper.deleteById(id);
        }
    }

    /**
     * updateById
     */
    public void updateById(Address address) {
        addressMapper.updateById(address);
    }

    /**
     * selectById
     */
    public Address selectById(Integer id) {
        return addressMapper.selectById(id);
    }

    /**
     * selectAll
     */
    public List<Address> selectAll(Address address) {
        return addressMapper.selectAll(address);
    }

    /**
     * selectPage
     */
    public PageInfo<Address> selectPage(Address address, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            address.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Address> list = addressMapper.selectAll(address);
        return PageInfo.of(list);
    }

}