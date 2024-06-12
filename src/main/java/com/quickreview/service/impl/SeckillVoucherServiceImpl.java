package com.quickReview.service.impl;

import com.quickReview.entity.SeckillVoucher;
import com.quickReview.mapper.SeckillVoucherMapper;
import com.quickReview.service.ISeckillVoucherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 秒杀优惠券表，与优惠券是一对一关系 服务实现类
 * service Implentation class, one-to-one relationship with voucher
 * </p>
 */
@Service
public class SeckillVoucherServiceImpl extends ServiceImpl<SeckillVoucherMapper, SeckillVoucher> implements ISeckillVoucherService {

}
