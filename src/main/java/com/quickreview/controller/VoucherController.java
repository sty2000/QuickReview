package com.quickReview.controller;


import com.quickReview.dto.Result;
import com.quickReview.entity.Voucher;
import com.quickReview.service.IVoucherService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 * 
 */
@RestController
@RequestMapping("/voucher")
public class VoucherController {

    @Resource
    private IVoucherService voucherService;

    /**
     * add seckill voucher
     * @param voucher voucher info, including seckill info
     * @return voucher id
     */
    @PostMapping("seckill")
    public Result addSeckillVoucher(@RequestBody Voucher voucher) {
        voucherService.addSeckillVoucher(voucher);
        return Result.ok(voucher.getId());
    }

    /**
     * add voucher
     * @param voucher  voucher info
     * @return  voucher id
     */
    @PostMapping
    public Result addVoucher(@RequestBody Voucher voucher) {
        voucherService.save(voucher);
        return Result.ok(voucher.getId());
    }


    /**
     * 查询店铺的优惠券列表 query voucher list of shop
     * @param shopId 店铺id shop id
     * @return 优惠券列表 voucher list
     */
    @GetMapping("/list/{shopId}")
    public Result queryVoucherOfShop(@PathVariable("shopId") Long shopId) {
       return voucherService.queryVoucherOfShop(shopId);
    }
}
