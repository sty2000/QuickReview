package com.quickReview.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
    * 博客表 list
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_blog")
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 main key
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商户id shop id
     */
    private Long shopId;
    /**
     * 用户id user id
     */
    private Long userId;
    /**
     * 用户图标 user icon
     */
    @TableField(exist = false)
    private String icon;
    /**
     * 用户姓名 user name
     */
    @TableField(exist = false)
    private String name;
    /**
     * 是否点赞过了 if the user liked the blog
     */
    @TableField(exist = false)
    private Boolean isLike;

    /**
     * 标题
     */
    private String title;

    /**
     * 探店的照片，最多9张，多张以","隔开 images of the blog, separated by "," at most 9
     */
    private String images;

    /**
     * 探店的文字描述 description of the blog
     */
    private String content;

    /**
     * 点赞数量 like count
     */
    private Integer liked;

    /**
     * 评论数量 comment count
     */
    private Integer comments;

    /**
     * 创建时间 create time
     */
    private LocalDateTime createTime;

    /**
     * 更新时间 update time
     */
    private LocalDateTime updateTime;


}
