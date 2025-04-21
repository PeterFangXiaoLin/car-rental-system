package com.my.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "alipay")
@Data
@Configuration
public class AlipayConfigProperties {
    private String appId;
    private String privateKey;
    private String alipayPublicKey;
    private String serverUrl;
    private String notifyUrl;
    private String returnUrl;
    private String signType;
    private String charset;
    private String format;

    @Bean
    public AlipayClient alipayClient() throws AlipayApiException {
        AlipayConfig alipayConfig = new AlipayConfig();
        //设置网关地址
        alipayConfig.setServerUrl(serverUrl);
        //设置应用ID
        alipayConfig.setAppId(appId);
        //设置应用私钥
        alipayConfig.setPrivateKey(privateKey);
        //设置请求格式，固定值json
        alipayConfig.setFormat(format);
        //设置字符集
        alipayConfig.setCharset(charset);
        //设置签名类型
        alipayConfig.setSignType(signType);
        //设置支付宝公钥
        alipayConfig.setAlipayPublicKey(alipayPublicKey);
        return new DefaultAlipayClient(alipayConfig);
    }
}
