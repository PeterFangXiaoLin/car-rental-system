package com.my.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.annotation.AuthCheck;
import com.my.common.BaseResponse;
import com.my.common.DeleteRequest;
import com.my.common.ErrorCode;
import com.my.config.AlipayConfigProperties;
import com.my.constant.UserConstant;
import com.my.domain.dto.rentalorder.RentalOrderAdminPageRequest;
import com.my.domain.dto.rentalorder.RentalOrderCancelRequest;
import com.my.domain.dto.rentalorder.RentalOrderCreateRequest;
import com.my.domain.dto.rentalorder.RentalOrderPageRequest;
import com.my.domain.dto.rentalorder.RentalOrderRefundRequest;
import com.my.domain.entity.RentalOrder;
import com.my.domain.entity.Vehicle;
import com.my.domain.enums.PaymentStatusEnum;
import com.my.domain.vo.RentalOrderVO;
import com.my.exception.BusinessException;
import com.my.service.RentalOrderService;
import com.my.service.VehicleService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private VehicleService vehicleService;

    @Resource
    private AlipayClient alipayClient;
    
    @Resource
    private AlipayConfigProperties alipayConfigProperties;

    @ApiOperation(value = "创建订单")
    @PostMapping("/create")
    public BaseResponse<Long> createRentalOrder(@RequestBody RentalOrderCreateRequest rentalOrderCreateRequest, HttpServletRequest request) {
        return success(rentalOrderService.createRentalOrder(rentalOrderCreateRequest, request));
    }

    @ApiOperation(value = "取消订单")
    @PostMapping("/cancel")
    public BaseResponse<Boolean> cancelRentalOrder(@RequestBody RentalOrderCancelRequest rentalOrderCancelRequest, HttpServletRequest request) {
        return success(rentalOrderService.cancelRentalOrder(rentalOrderCancelRequest, request));
    }

    @ApiOperation(value = "获取订单详情")
    @GetMapping("/get")
    public BaseResponse<RentalOrderVO> getRentalOrder(@RequestParam("orderId") Long orderId, HttpServletRequest request) {
        return success(rentalOrderService.getRentalOrder(orderId, request));
    }

    @ApiOperation(value = "支付订单")
    @GetMapping("/pay")
    public String payOrder(@RequestParam("orderId") Long orderId, HttpServletResponse httpServletResponse) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单参数错误");
        }

        // 查询订单
        RentalOrder order = rentalOrderService.getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "订单不存在");
        }

        // 查询车辆是否存在
        Long vehicleId = order.getVehicleId();
        Vehicle vehicle = vehicleService.getById(vehicleId);
        if (vehicle == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "车辆不存在");
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
                + "\"out_trade_no\":\"" + order.getOrderNo() + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\","
                + "\"total_amount\":" + order.getTotalAmount() + ","
                + "\"subject\":\"" + vehicle.getName() + "\","
                + "\"body\":\"" + order.getRemark() + "\""
                + "}";
            
            alipayRequest.setBizContent(bizContent);
            
            // 调用支付宝API
            // 调用阿里的SDK生成表单
            // 会收到支付宝的响应，响应的是一个页面，一开始是登陆，然后显示金额，让用户输入密码进行付款
            AlipayTradePagePayResponse response = alipayClient.pageExecute(alipayRequest);
            
            // 处理响应
            if (response.isSuccess()) {
                // 返回支付表单
                return response.getBody();
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
    @PostMapping(value = "/notify", produces = "text/html;charset=UTF-8")
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

    @ApiOperation(value = "获取当前登录用户的订单列表")
    @PostMapping("/page/my")
    public BaseResponse<Page<RentalOrderVO>> pageMyRentalOrder(@RequestBody RentalOrderPageRequest pageRequest, HttpServletRequest request) {
        return success(rentalOrderService.pageMyRentalOrder(pageRequest, request));
    }

    @ApiOperation(value = "获取订单分页")
    @PostMapping("/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<RentalOrderVO>> pageRentalOrder(@RequestBody RentalOrderAdminPageRequest pageRequest) {
        return success(rentalOrderService.pageRentalOrder(pageRequest));
    }

    @ApiOperation(value = "删除订单")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteRentalOrder(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        return success(rentalOrderService.deleteRentalOrder(deleteRequest, request));
    }

    @ApiOperation(value = "取车")
    @GetMapping("/pickup")
    public BaseResponse<Boolean> pickupVehicle(@RequestParam("orderId") Long orderId, HttpServletRequest request) {
        return success(rentalOrderService.pickupVehicle(orderId, request));
    }

    @ApiOperation(value = "还车")
    @GetMapping("/return")
    public BaseResponse<Boolean> returnVehicle(@RequestParam("orderId") Long orderId, HttpServletRequest request) {
        return success(rentalOrderService.returnVehicle(orderId, request));
    }

    @ApiOperation(value = "退款")
    @PostMapping("/refund")
    public BaseResponse<Boolean> refundOrder(@RequestBody RentalOrderRefundRequest refundRequest, HttpServletRequest request) {
        return success(rentalOrderService.refundOrder(refundRequest, request));
    }
}
