package com.example.controller;

import com.example.common.Result;
import com.example.entity.Notice;
import com.example.service.NoticeService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * Announcement information table front-end operation interface
 **/
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    /**
     * add
     */
    @PostMapping("/add")
    public Result add(@RequestBody Notice notice) {
        noticeService.add(notice);
        return Result.success();
    }

    /**
     * delete
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        noticeService.deleteById(id);
        return Result.success();
    }

    /**
     * batch delete
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        noticeService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * update
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Notice notice) {
        noticeService.updateById(notice);
        return Result.success();
    }

    /**
     * select by id
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Notice notice = noticeService.selectById(id);
        return Result.success(notice);
    }

    /**
     * select all
     */
    @GetMapping("/selectAll")
    public Result selectAll(Notice notice ) {
        List<Notice> list = noticeService.selectAll(notice);
        return Result.success(list);
    }

    /**
     * select page
     */
    @GetMapping("/selectPage")
    public Result selectPage(Notice notice,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Notice> page = noticeService.selectPage(notice, pageNum, pageSize);
        return Result.success(page);
    }

}