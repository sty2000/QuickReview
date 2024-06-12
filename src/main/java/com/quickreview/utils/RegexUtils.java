package com.quickReview.utils;

import cn.hutool.core.util.StrUtil;

/**
 * 
 */
public class RegexUtils {
    /**
     * seeif it is invalid phone number format
     * @param phone the phone number to be verified
     * @return true:valid, false:invalid
     */
    public static boolean isPhoneInvalid(String phone){
        return mismatch(phone, RegexPatterns.PHONE_REGEX);
    }
    /**
     * see if it is invalid email format
     * @param email the email to be verified
     * @return true:valid, false:invalid
     */
    public static boolean isEmailInvalid(String email){
        return mismatch(email, RegexPatterns.EMAIL_REGEX);
    }

    /**
     * see if it is invalid password format
     * @param code the password to be verified
     * @return true:valid, false:invalid
     */
    public static boolean isCodeInvalid(String code){
        return mismatch(code, RegexPatterns.VERIFY_CODE_REGEX);
    }

    // whether the string does not match the regular expression
    private static boolean mismatch(String str, String regex){
        if (StrUtil.isBlank(str)) {
            return true;
        }
        return !str.matches(regex);
    }
}
