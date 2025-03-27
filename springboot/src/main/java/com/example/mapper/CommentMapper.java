package com.example.mapper;

import com.example.entity.Comment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Operation notice related data interface
*/
public interface CommentMapper {

    /**
     * insert
     */
    int insert(Comment comment);

    /**
     * deleteById
     */
    int deleteById(Integer id);

    /**
     * updateById
     */
    int updateById(Comment comment);

    /**
     * selectById
     */
    Comment selectById(Integer id);

    /**
     * selectAll
     */
    List<Comment> selectAll(Comment comment);

    @Select("select comment.*, user.avatar as userAvatar, user.name as userName from comment left join user on comment.user_id = user.id where comment.goods_id = #{id}")
    List<Comment> selectByGoodsId(Integer id);
}