package com.quickReview.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_blog_comments")
public class BlogComments implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 main key
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id user id
     */
    private Long userId;

    /**
     * 探店id explore id
     */
    private Long blogId;

    /**
     * 关联的1级评论id，如果是一级评论，则值为0 related 1st level comment id, if it is a 1st level comment, the value is 0
     */
    private Long parentId;

    /**
     * 回复的评论id reply comment id
     */
    private Long answerId;

    /**
     * 回复的内容 reply content
     */
    private String content;

    /**
     * 点赞数 likes
     */
    private Integer liked;

    /**
     * 状态，0：正常，1：被举报，2：禁止查看 
     * status, 0: normal, 1: reported, 2: forbidden to view
     */
    private Boolean status;

    /**
     * 创建时间 create time
     */
    private LocalDateTime createTime;

    /**
     * 更新时间 update time
     */
    private LocalDateTime updateTime;


}
