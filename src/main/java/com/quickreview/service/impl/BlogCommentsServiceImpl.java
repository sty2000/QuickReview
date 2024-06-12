package com.quickReview.service.impl;

import com.quickReview.entity.BlogComments;
import com.quickReview.mapper.BlogCommentsMapper;
import com.quickReview.service.IBlogCommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类 service Implentation class
 * </p>
 */
@Service
public class BlogCommentsServiceImpl extends ServiceImpl<BlogCommentsMapper, BlogComments> implements IBlogCommentsService {

}
