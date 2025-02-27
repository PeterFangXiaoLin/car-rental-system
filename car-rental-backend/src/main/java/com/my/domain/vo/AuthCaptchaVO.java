package com.my.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 验证码响应类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthCaptchaVO implements Serializable {
    private String image;

    private String captchaKey;
}
