package com.quickReview.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * User table
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 main key
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 手机号码 phone number
     */
    private String phone;

    /**
     * 密码，加密存储 password, encrypted
     */
    private String password;

    /**
     * 昵称，默认是随机字符 nickname, default is random characters
     */
    private String nickName;

    /**
     * 用户头像 user icon
     */
    private String icon = "";

    /**
     * 创建时间 create time
     */
    private LocalDateTime createTime;

    /**
     * 更新时间 update time
     */
    private LocalDateTime updateTime;


}
