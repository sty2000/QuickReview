package com.quickReview.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quickReview.dto.LoginFormDTO;
import com.quickReview.dto.Result;
import com.quickReview.dto.UserDTO;
import com.quickReview.entity.User;
import com.quickReview.mapper.UserMapper;
import com.quickReview.service.IUserService;
import com.quickReview.utils.RegexUtils;
import com.quickReview.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.quickReview.utils.RedisConstants.*;
import static com.quickReview.utils.SystemConstants.USER_NICK_NAME_PREFIX;

/**
 * <p>
 * 服务实现类
 * </p>
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result sendCode(String phone, HttpSession session) {
        // 1. check phone number
        if (RegexUtils.isPhoneInvalid(phone)) {
            // 2.if not valid, return error message
            return Result.fail("手机号格式错误！");
        }
        // 3. if valid, generate a 6-digit verification code
        String code = RandomUtil.randomNumbers(6);

        // 4.save the verification code to Redis
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + phone, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);

        // 5.send the verification code to the user's phone number
        log.debug("发送短信验证码成功，验证码：{}", code);
        // 返回ok
        return Result.ok();
    }

    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session) {
        // 1.check phone number
        String phone = loginForm.getPhone();
        if (RegexUtils.isPhoneInvalid(phone)) {
            // 2.if does not match, return error message
            return Result.fail("手机号格式错误！");
        }
        // 3.get the verification code from Redis
        String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
        String code = loginForm.getCode();
        if (cacheCode == null || !cacheCode.equals(code)) {
            // does not match, return error message
            return Result.fail("验证码错误");
        }

        // 4.matches, get user by phone  select * from tb_user where phone = ?
        User user = query().eq("phone", phone).one();

        // 5.sees if the user exists
        if (user == null) {
            // 6.no, create a new user
            user = createUserWithPhone(phone);
        }

        // 7.save user info to Redis
        // 7.1.randomly generate a token to serve as the key
        String token = UUID.randomUUID().toString(true);
        // 7.2.copy user info to UserDTO
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
        // 7.3.save user info
        String tokenKey = LOGIN_USER_KEY + token;
        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
        // 7.4.set expiration time of the token
        stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);

        // 8.return token
        return Result.ok(token);
    }

    @Override
    public Result sign() {
        // 1.get the current user
        Long userId = UserHolder.getUser().getId();
        // 2.get the current date
        LocalDateTime now = LocalDateTime.now();
        // 3.assemble the key
        String keySuffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        String key = USER_SIGN_KEY + userId + keySuffix;
        // 4.get the day of the month
        int dayOfMonth = now.getDayOfMonth();
        // 5.写入Redis SETBIT key offset 1
        stringRedisTemplate.opsForValue().setBit(key, dayOfMonth - 1, true);
        return Result.ok();
    }

    @Override
    public Result signCount() {
        // 1.get the current user
        Long userId = UserHolder.getUser().getId();
        // 2.get the current date
        LocalDateTime now = LocalDateTime.now();
        // 3.assemble the key
        String keySuffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        String key = USER_SIGN_KEY + userId + keySuffix;
        // 4.get the day of the month
        int dayOfMonth = now.getDayOfMonth();
        // 5.get the records of signing in, BITFIELD sign:5:202203 GET u14 0
        List<Long> result = stringRedisTemplate.opsForValue().bitField(
                key,
                BitFieldSubCommands.create()
                        .get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth)).valueAt(0)
        );
        if (result == null || result.isEmpty()) {
            // no records, return 0
            return Result.ok(0);
        }
        Long num = result.get(0);
        if (num == null || num == 0) {
            return Result.ok(0);
        }
        // 6.loop through the bits to count the number of signed days
        int count = 0;
        while (true) {
            // 6.1.sees if the last bit is 0
            if ((num & 1) == 0) {
                // 6.2.if 0, it means the user has not signed in, return the count
                break;
            }else {
                // 6.3.if 1, increment the count
                count++;
            }
            // 6.4.right shift the number by 1 bit
            num >>>= 1;
        }
        return Result.ok(count);
    }

    private User createUserWithPhone(String phone) {
        // 1.创建用户
        User user = new User();
        user.setPhone(phone);
        user.setNickName(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
        // 2.保存用户
        save(user);
        return user;
    }
}
