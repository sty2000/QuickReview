package com.quickReview.utils;


import cn.hutool.core.util.RandomUtil;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class PasswordEncoder {

    public static String encode(String password) {
        // generate salt
        String salt = RandomUtil.randomString(20);
        // encrypt
        return encode(password,salt);
    }
    private static String encode(String password, String salt) {
        // md5
        return salt + "@" + DigestUtils.md5DigestAsHex((password + salt).getBytes(StandardCharsets.UTF_8));
    }
    public static Boolean matches(String encodedPassword, String rawPassword) {
        if (encodedPassword == null || rawPassword == null) {
            return false;
        }
        if(!encodedPassword.contains("@")){
            throw new RuntimeException("密码格式不正确！");
        }
        String[] arr = encodedPassword.split("@");
        // get salt
        String salt = arr[0];
        // compare
        return encodedPassword.equals(encode(rawPassword, salt));
    }
}
