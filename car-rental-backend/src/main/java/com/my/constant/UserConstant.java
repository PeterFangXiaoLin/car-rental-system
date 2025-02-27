package com.my.constant;

public interface UserConstant {
    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "user_login";

    /**
     * 默认用户名
     */
    String DEFAULT_USER_NAME = "无名";

    /**
     * 默认密码
     */
    String DEFAULT_PASSWORD = "12345678";

    /**
     * 最小账号长度
     */
    int MIN_ACCOUNT_LEN = 4;

    /**
     * 最长账号长度
     */
    int MAX_ACCOUNT_LEN = 20;


    /**
     * 最小密码长度
     */
    int MIN_PASSWORD_LEN = 6;

    /**
     * 最长密码长度
     */
    int MAX_PASSWORD_LEN = 20;

    String SALT = "car_rental";
}
