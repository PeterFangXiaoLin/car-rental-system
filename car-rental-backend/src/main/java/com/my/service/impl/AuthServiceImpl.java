package com.my.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.lang.UUID;
import com.my.domain.vo.AuthCaptchaVO;
import com.my.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import static com.my.constant.RedisConstant.CAPTCHA_PREFIX;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public AuthCaptchaVO getCaptcha() {
        // 生成 图形验证码
        RandomGenerator randomGenerator = new RandomGenerator("abcdefghjkmnopqrstuvwxyz023456789", 4);
        // 生成干扰线
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(100, 42, 4, 60);
        lineCaptcha.setGenerator(randomGenerator);
        lineCaptcha.createCode();

        // 获取 base64 字符串 和 code 值
        String base64 = "data:image/png;base64," + lineCaptcha.getImageBase64();
        String code = lineCaptcha.getCode();

        log.info("图形验证码内容：{}", code);
        log.debug("图形验证码base64：{}", base64);

        // 根据code 生成redis 的 key, 注册时填写
        // 生成随机UUID作为验证码的key
        String captchaKey = UUID.randomUUID().toString();
        // 将验证码存入Redis，设置过期时间（如5分钟）
        stringRedisTemplate.opsForValue().set(CAPTCHA_PREFIX + captchaKey, code, 5, TimeUnit.MINUTES);

        return new AuthCaptchaVO(base64, captchaKey);
    }
}
