package com.my.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.common.BaseResponse;
import com.my.common.ErrorCode;
import com.my.config.AlipayConfigProperties;
import com.my.domain.dto.rentalorder.RentalOrderCreateRequest;
import com.my.domain.dto.rentalorder.RentalOrderPageRequest;
import com.my.domain.dto.rentalorder.RentalOrderPayRequest;
import com.my.domain.entity.RentalOrder;
import com.my.domain.enums.PaymentStatusEnum;
import com.my.domain.vo.RentalOrderVO;
import com.my.exception.BusinessException;
import com.my.service.RentalOrderService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static com.my.common.ResultUtils.success;

@RestController
@RequestMapping("/rentalOrder")
@Slf4j
public class RentalOrderController{

    @Resource
    private RentalOrderService rentalOrderService;
    
    @Resource
    private AlipayClient alipayClient;
    
    @Resource
    private AlipayConfigProperties alipayConfigProperties;

    @ApiOperation(value = "创建订单")
    @PostMapping("/create")
    public BaseResponse<Long> createRentalOrder(@RequestBody RentalOrderCreateRequest rentalOrderCreateRequest, HttpServletRequest request) {
        return success(rentalOrderService.createRentalOrder(rentalOrderCreateRequest, request));
    }

    @ApiOperation(value = "支付订单")
    @PostMapping("/pay")
    public BaseResponse<String> payOrder(@RequestBody RentalOrderPayRequest payRequest) {
        if (payRequest == null || payRequest.getOrderId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单参数错误");
        }
        
        try {
            // 创建API对应的request
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            
            // 设置异步通知地址
            alipayRequest.setNotifyUrl(rentalOrderService.getNotifyUrl());
            
            // 设置同步返回地址
            alipayRequest.setReturnUrl(rentalOrderService.getReturnUrl());
            
            // 构建业务参数
            String bizContent = "{"
                + "\"out_trade_no\":\"" + payRequest.getOrderNo() + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\","
                + "\"total_amount\":" + payRequest.getTotalAmount() + ","
                + "\"subject\":\"" + payRequest.getSubject() + "\","
                + "\"body\":\"" + payRequest.getBody() + "\""
                + "}";
            
            alipayRequest.setBizContent(bizContent);
            
            // 调用支付宝API
            AlipayTradePagePayResponse response = alipayClient.pageExecute(alipayRequest);
            
            // 处理响应
            if (response.isSuccess()) {
                // 更新订单状态为支付中
                rentalOrderService.updateOrderPayStatus(payRequest.getOrderId(), 1);
                
                // 返回支付表单
                return success(response.getBody());
            } else {
                log.error("生成支付订单失败: {}", response.getMsg());
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成支付订单失败");
            }
        } catch (AlipayApiException e) {
            log.error("调用支付宝接口异常", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "支付系统异常");
        }
    }
    
    @ApiOperation(value = "支付宝异步通知")
    @PostMapping("/notify")
    public String alipayNotify(HttpServletRequest request) {
        // 获取支付宝POST过来的信息
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        
        try {
            // 调用SDK验证签名
            boolean signVerified = AlipaySignature.rsaCheckV1(
                params, 
                alipayConfigProperties.getAlipayPublicKey(),
                alipayConfigProperties.getCharset(),
                alipayConfigProperties.getSignType()
            );
            
            if (signVerified) {
                // 验签成功后，按照支付宝要求的规则校验通知内容
                String outTradeNo = params.get("out_trade_no");
                String tradeStatus = params.get("trade_status");
                
                // 查询订单
                RentalOrder order = rentalOrderService.lambdaQuery()
                    .eq(RentalOrder::getOrderNo, outTradeNo)
                    .one();
                
                if (order == null) {
                    log.error("订单不存在: {}", outTradeNo);
                    return "failure";
                }
                
                // 判断通知类型
                if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                    // 支付成功，更新订单状态
                    rentalOrderService.updateOrderPayStatus(order.getId(), PaymentStatusEnum.PAID.getValue());
                    log.info("订单[{}]支付成功", outTradeNo);
                } else if ("TRADE_CLOSED".equals(tradeStatus)) {
                    // 交易关闭，更新订单状态
                    rentalOrderService.updateOrderPayStatus(order.getId(), PaymentStatusEnum.UNPAID.getValue());
                    log.info("订单[{}]交易关闭", outTradeNo);
                }
                
                // 通知成功，向支付宝返回success
                return "success";
            } else {
                log.error("支付宝通知验签失败");
                return "failure";
            }
        } catch (AlipayApiException e) {
            log.error("支付宝通知处理异常", e);
            return "failure";
        }
    }

    @ApiOperation("获取订单列表")
    @PostMapping("/page")
    public BaseResponse<Page<RentalOrderVO>> pageRentalOrder(@RequestBody RentalOrderPageRequest pageRequest, HttpServletRequest request) {
        return success(rentalOrderService.pageRentalOrder(pageRequest, request));
    }

}
