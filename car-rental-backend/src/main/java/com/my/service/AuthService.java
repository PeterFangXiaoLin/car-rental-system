package com.my.service;

import com.my.domain.vo.AuthCaptchaVO;

public interface AuthService {
    AuthCaptchaVO getCaptcha();

    /**
     * 刷新验证码
     *
     * @param captchaKey
     * @return
     */
    AuthCaptchaVO refreshCaptcha(String captchaKey);
}
