package com.quickReview.controller;


import cn.hutool.core.bean.BeanUtil;
import com.quickReview.dto.LoginFormDTO;
import com.quickReview.dto.Result;
import com.quickReview.dto.UserDTO;
import com.quickReview.entity.User;
import com.quickReview.entity.UserInfo;
import com.quickReview.service.IUserInfoService;
import com.quickReview.service.IUserService;
import com.quickReview.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * 
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @Resource
    private IUserInfoService userInfoService;

    /**
     * 发送手机验证码 send code via phone
     */
    @PostMapping("code")
    public Result sendCode(@RequestParam("phone") String phone, HttpSession session) {
        // 发送短信验证码并保存验证码 send code and save it
        return userService.sendCode(phone, session);
    }

    /**
     * 登录功能 login
     * @param loginForm 登录参数，包含手机号、验证码；或者手机号、密码 include phone and code or phone and password
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginFormDTO loginForm, HttpSession session){
        // 实现登录功能
        return userService.login(loginForm, session);
    }

    /**
     * 登出功能
     * @return 无
     */
    @PostMapping("/logout")
    public Result logout(){
        // 实现登出功能 clear login user
        UserHolder.clear();
        UserDTO user = UserHolder.getUser();
        if (user == null) {
            return Result.ok();
        }
        return Result.fail("功能未完成");
    }

    @GetMapping("/me")
    public Result me(){
        // 获取当前登录的用户并返回 get login user
        UserDTO user = UserHolder.getUser();
        return Result.ok(user);
    }

    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long userId){
        // 查询详情 query detail
        UserInfo info = userInfoService.getById(userId);
        if (info == null) {
            // 没有详情，应该是第一次查看详情 no detail, maybe first time
            return Result.ok();
        }
        info.setCreateTime(null);
        info.setUpdateTime(null);
        // 返回
        return Result.ok(info);
    }

    @GetMapping("/{id}")
    public Result queryUserById(@PathVariable("id") Long userId){
        // 查询详情 query detail
        User user = userService.getById(userId);
        if (user == null) {
            return Result.ok();
        }
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        // 返回 return
        return Result.ok(userDTO);
    }

    @PostMapping("/sign")
    public Result sign(){
        return userService.sign();
    }

    @GetMapping("/sign/count")
    public Result signCount(){
        return userService.signCount();
    }
}