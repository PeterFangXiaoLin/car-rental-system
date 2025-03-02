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

    /**
     * 最大用户名长度
     */
    int MAX_USERNAME_LEN = 20;

    /**
     * 最大简介长度
     */
    int MAX_PROFILE_LEN = 1024;

    /**
     * 最大信用评分
     */
    int MAX_CREDIT_SCORE = 10;

    String SALT = "car_rental";

    //  region 权限
    /**
     * 默认角色
     */
    String DEFAULT_ROLE = "user";
    /**
     * 管理员角色
     */
    String ADMIN_ROLE = "admin";
    // endregion
}
