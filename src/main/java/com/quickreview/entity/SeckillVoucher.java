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
 * 秒杀优惠券表，与优惠券是一对一关系
 * seckill voucher table, one-to-one relationship with voucher
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_seckill_voucher")
public class SeckillVoucher implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联的优惠券的id
     * related voucher id
     */
    @TableId(value = "voucher_id", type = IdType.INPUT)
    private Long voucherId;

    /**
     * 库存 stock
     */
    private Integer stock;

    /**
     * 创建时间 create time
     */
    private LocalDateTime createTime;

    /**
     * 生效时间 effective time
     */
    private LocalDateTime beginTime;

    /**
     * 失效时间 expiration time
     */
    private LocalDateTime endTime;

    /**
     * 更新时间 update time
     */
    private LocalDateTime updateTime;


}
