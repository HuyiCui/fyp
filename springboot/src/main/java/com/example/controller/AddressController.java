package com.example.controller;

import com.example.common.Result;
import com.example.entity.Address;
import com.example.service.AddressService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Address front-end operation interface
 **/
@RestController
@RequestMapping("/address")
public class AddressController {

    @Resource
    private AddressService addressService;

    /**
     * Add
     */
    @PostMapping("/add")
    public Result add(@RequestBody Address address) {
        addressService.add(address);
        return Result.success();
    }

    /**
     * Delete
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        addressService.deleteById(id);
        return Result.success();
    }

    /**
     * Batch delete
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        addressService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * Update
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Address address) {
        addressService.updateById(address);
        return Result.success();
    }

    /**
     * Select by id
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Address address = addressService.selectById(id);
        return Result.success(address);
    }

    /**
     * Select all
     */
    @GetMapping("/selectAll")
    public Result selectAll(Address address ) {
        List<Address> list = addressService.selectAll(address);
        return Result.success(list);
    }

    /**
     * Select page
     */
    @GetMapping("/selectPage")
    public Result selectPage(Address address,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Address> page = addressService.selectPage(address, pageNum, pageSize);
        return Result.success(page);
    }

}