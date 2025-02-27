package com.my.controller;

import com.my.common.BaseResponse;
import com.my.domain.vo.AuthCaptchaVO;
import com.my.service.AuthService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.my.common.ResultUtils.success;

@RestController
@RequestMapping("/sms/auth")
public class SmsAuthController {

    @Resource
    private AuthService authService;

    @GetMapping("/getCaptcha")
    @ApiOperation(value = "生成图形验证码")
    public BaseResponse<AuthCaptchaVO> getCaptcha() {
        return success(authService.getCaptcha());
    }
}
