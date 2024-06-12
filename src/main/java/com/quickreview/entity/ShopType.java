package com.quickReview.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *  商铺类型表 list of shop types
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_shop_type")
public class ShopType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 main key
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 类型名称 name of the type
     */
    private String name;

    /**
     * 图标 icon
     */
    private String icon;

    /**
     * 顺序 sort
     */
    private Integer sort;

    /**
     * 创建时间 create time
     */
    @JsonIgnore
    private LocalDateTime createTime;

    /**
     * 更新时间 update time
     */
    @JsonIgnore
    private LocalDateTime updateTime;


}
