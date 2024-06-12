package com.quickReview.utils;

/**
 * 
 */
public abstract class RegexPatterns {
    /**
     * regex for phone number
     */
    public static final String PHONE_REGEX = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";
    /**
     * regex for email
     */
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    /**
     * regex for password, 4-32 characters
     */
    public static final String PASSWORD_REGEX = "^\\w{4,32}$";
    /**
     * regex for verification code, 6 characters
     */
    public static final String VERIFY_CODE_REGEX = "^[a-zA-Z\\d]{6}$";

}
