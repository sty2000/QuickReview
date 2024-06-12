package com.quickReview.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quickReview.dto.Result;
import com.quickReview.dto.UserDTO;
import com.quickReview.entity.Follow;
import com.quickReview.mapper.FollowMapper;
import com.quickReview.service.IFollowService;
import com.quickReview.service.IUserService;
import com.quickReview.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类 service implentation class
 * </p>
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private IUserService userService;

    @Override
    public Result follow(Long followUserId, Boolean isFollow) {
        // 1. get current user
        Long userId = UserHolder.getUser().getId();
        String key = "follows:" + userId;
        // 1.sees if the user is following 
        if (isFollow) {
            // 2.is following, insert the following user into the database
            Follow follow = new Follow();
            follow.setUserId(userId);
            follow.setFollowUserId(followUserId);
            boolean isSuccess = save(follow);
            if (isSuccess) {
                // put the following user's id into the Redis set  sadd userId followerUserId
                stringRedisTemplate.opsForSet().add(key, followUserId.toString());
            }
        } else {
            // 3.unfollow  delete from tb_follow where user_id = ? and follow_user_id = ?
            boolean isSuccess = remove(new QueryWrapper<Follow>()
                    .eq("user_id", userId).eq("follow_user_id", followUserId));
            if (isSuccess) {
                // remove the following user's id from the Redis set
                stringRedisTemplate.opsForSet().remove(key, followUserId.toString());
            }
        }
        return Result.ok();
    }

    @Override
    public Result isFollow(Long followUserId) {
        // 1.get current user
        Long userId = UserHolder.getUser().getId();
        // 2.see if following select count(*) from tb_follow where user_id = ? and follow_user_id = ?
        Integer count = query().eq("user_id", userId).eq("follow_user_id", followUserId).count();
        // 3.return result
        return Result.ok(count > 0);
    }

    @Override
    public Result followCommons(Long id) {
        // 1.get current user
        Long userId = UserHolder.getUser().getId();
        String key = "follows:" + userId;
        // 2.get the intersection of the two sets
        String key2 = "follows:" + id;
        Set<String> intersect = stringRedisTemplate.opsForSet().intersect(key, key2);
        if (intersect == null || intersect.isEmpty()) {
            // no intersection
            return Result.ok(Collections.emptyList());
        }
        // 3.resolve the set of user ids
        List<Long> ids = intersect.stream().map(Long::valueOf).collect(Collectors.toList());
        // 4.query user by id
        List<UserDTO> users = userService.listByIds(ids)
                .stream()
                .map(user -> BeanUtil.copyProperties(user, UserDTO.class))
                .collect(Collectors.toList());
        return Result.ok(users);
    }
}
