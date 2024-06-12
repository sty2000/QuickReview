package com.quickReview.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quickReview.dto.LoginFormDTO;
import com.quickReview.dto.Result;
import com.quickReview.entity.User;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  服务类
 * </p>
 */
public interface IUserService extends IService<User> {

    Result sendCode(String phone, HttpSession session);

    Result login(LoginFormDTO loginForm, HttpSession session);

    Result sign();

    Result signCount();

}
