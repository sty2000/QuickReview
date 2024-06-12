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
 *  代金券表 list of vouchers
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_voucher")
public class Voucher implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 main key
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商铺id shop id
     */
    private Long shopId;

    /**
     * 代金券标题 voucher title
     */
    private String title;

    /**
     * 副标题 sub title
     */
    private String subTitle;

    /**
     * 使用规则 rules of use
     */
    private String rules;

    /**
     * 支付金额 pay value
     */
    private Long payValue;

    /**
     * 抵扣金额 deduction(actual) value
     */
    private Long actualValue;

    /**
     * 优惠券类型 type of voucher
     */
    private Integer type;

    /**
     * 优惠券状态 status of voucher
     */
    private Integer status;
    /**
     * 库存 stock
     */
    @TableField(exist = false)
    private Integer stock;

    /**
     * 生效时间 effective time
     */
    @TableField(exist = false)
    private LocalDateTime beginTime;

    /**
     * 失效时间 expiration time
     */
    @TableField(exist = false)
    private LocalDateTime endTime;

    /**
     * 创建时间 create time
     */
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
