package com.example.mapper;

import com.example.entity.Notice;
import java.util.List;

/**
 * Operation notice related data interface
*/
public interface NoticeMapper {

    /**
      * insert
    */
    int insert(Notice notice);

    /**
      * deleteById
    */
    int deleteById(Integer id);

    /**
      * updateById
    */
    int updateById(Notice notice);

    /**
      * selectById
    */
    Notice selectById(Integer id);

    /**
      * selectAll
    */
    List<Notice> selectAll(Notice notice);

}