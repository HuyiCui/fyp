package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Business;
import com.example.exception.CustomException;
import com.example.mapper.BusinessMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Merchant business processing
 **/
@Service
public class BusinessService {

    @Resource
    private BusinessMapper businessMapper;

    /**
     * add
     */
    public void add(Business business) {
        Business dbBusiness = businessMapper.selectByUsername(business.getUsername());
        if (ObjectUtil.isNotNull(dbBusiness)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(business.getPassword())) {
            business.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        if (ObjectUtil.isEmpty(business.getName())) {
            business.setName(business.getUsername());
        }
        business.setRole(RoleEnum.BUSINESS.name());
        businessMapper.insert(business);
    }

    /**
     * deleteById
     */
    public void deleteById(Integer id) {
        businessMapper.deleteById(id);
    }

    /**
     * deleteBatch
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            businessMapper.deleteById(id);
        }
    }

    /**
     * updateById
     */
    public void updateById(Business business) {
        businessMapper.updateById(business);
    }

    /**
     * selectById
     */
    public Business selectById(Integer id) {
        return businessMapper.selectById(id);
    }

    /**
     * selectAll
     */
    public List<Business> selectAll(Business business) {
        return businessMapper.selectAll(business);
    }

    /**
     * selectPage
     */
    public PageInfo<Business> selectPage(Business business, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Business> list = businessMapper.selectAll(business);
        return PageInfo.of(list);
    }

    /**
     * login
     */
    public Account login(Account account) {
        Account dbBusiness = businessMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbBusiness)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbBusiness.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // Generate token
        String tokenData = dbBusiness.getId() + "-" + RoleEnum.BUSINESS.name();
        String token = TokenUtils.createToken(tokenData, dbBusiness.getPassword());
        dbBusiness.setToken(token);
        return dbBusiness;
    }

    /**
     * register
     */
    public void register(Account account) {
        Business business = new Business();
        BeanUtils.copyProperties(account, business);
        add(business);
    }

    /**
     * updatePassword
     */
    public void updatePassword(Account account) {
        Business dbBusiness = businessMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbBusiness)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbBusiness.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbBusiness.setPassword(account.getNewPassword());
        businessMapper.updateById(dbBusiness);
    }

}