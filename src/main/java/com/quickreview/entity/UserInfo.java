package com.quickReview.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 *  用户信息表 list of user information
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，用户id main key, user id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 城市名称 city name
     */
    private String city;

    /**
     * 个人介绍，不要超过128个字符 introduce, no more than 128 characters
     */
    private String introduce;

    /**
     * 粉丝数量 fans number
     */
    private Integer fans;

    /**
     * 关注的人的数量 followee number
     */
    private Integer followee;

    /**
     * 性别，0：男，1：女 
     * gender, 0:M, 1:F
     */
    private Boolean gender;

    /**
     * 生日 birthday
     */
    private LocalDate birthday;

    /**
     * 积分 credits
     */
    private Integer credits;

    /**
     * 会员级别，0~9级,0代表未开通会员
     * membership level, 0~9, 0 means not a member
     */
    private Boolean level;

    /**
     * 创建时间 create time
     */
    private LocalDateTime createTime;

    /**
     * 更新时间 update time
     */
    private LocalDateTime updateTime;


}
