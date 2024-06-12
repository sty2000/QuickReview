package com.quickReview.service.impl;

import com.quickReview.entity.UserInfo;
import com.quickReview.mapper.UserInfoMapper;
import com.quickReview.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

}
