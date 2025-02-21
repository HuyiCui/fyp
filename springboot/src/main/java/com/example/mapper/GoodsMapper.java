package com.example.mapper;

import com.example.entity.Goods;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Operation notice related data interface
*/
public interface GoodsMapper {

    /**
     * insert
     */
    int insert(Goods goods);

    /**
     * deleteById
     */
    int deleteById(Integer id);

    /**
     * updateById
     */
    int updateById(Goods goods);

    /**
     * selectById
     */
    Goods selectById(Integer id);

    /**
     * selectAll
     */
    List<Goods> selectAll(Goods goods);

    @Select("select * from goods order by count desc limit 15")
    List<Goods> selectTop15();

    @Select("select * from goods where type_id = #{id}")
    List<Goods> selectByTypeId(Integer id);

}