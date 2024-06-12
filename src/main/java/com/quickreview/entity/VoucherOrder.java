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
 *  代金券订单表 voucher order table
 * </p>
 *
 * 
 * @since 2024
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_voucher_order")
public class VoucherOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 main key
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 下单的用户id user id who placed the order
     */
    private Long userId;

    /**
     * 购买的代金券id voucher id purchased
     */
    private Long voucherId;

    /**
     * 支付方式 1：余额支付；2：支付宝；3：微信
     * payment method: balance or external payment
     */
    private Integer payType;

    /**
     * 订单状态，1：未支付；2：已支付；3：已核销；4：已取消；5：退款中；6：已退款
     * order status 1: unpaid; 2: paid; 3: used; 4: cancelled; 5: refunding; 6: refunded
     */
    private Integer status;

    /**
     * 下单时间 create time
     */
    private LocalDateTime createTime;

    /**
     * 支付时间 pay time
     */
    private LocalDateTime payTime;

    /**
     * 核销时间 use time
     */
    private LocalDateTime useTime;

    /**
     * 退款时间 refund time
     */
    private LocalDateTime refundTime;

    /**
     * 更新时间 update time
     */
    private LocalDateTime updateTime;


}
