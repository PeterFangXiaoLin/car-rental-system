package com.my.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.my.domain.dto.rentalorder.RentalOrderCreateRequest;
import com.my.domain.dto.rentalorder.RentalOrderPageRequest;
import com.my.domain.entity.RentalOrder;
import com.my.domain.vo.RentalOrderVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author Administrator
* @description 针对表【rental_order(租赁订单表)】的数据库操作Service
* @createDate 2025-04-08 09:38:16
*/
public interface RentalOrderService extends IService<RentalOrder> {

    /**
     * 创建订单
     * @param rentalOrderCreateRequest
     * @param request
     * @return
     */
    Long createRentalOrder(RentalOrderCreateRequest rentalOrderCreateRequest, HttpServletRequest request);

    /**
     * 获取支付宝异步通知URL
     * @return 异步通知URL
     */
    String getNotifyUrl();

    /**
     * 获取支付宝同步返回URL
     * @return 同步返回URL
     */
    String getReturnUrl();

    /**
     * 更新订单支付状态
     * @param orderId 订单ID
     * @param status 支付状态：0-未支付，1-支付中，2-已支付，3-支付失败
     * @return 是否更新成功
     */
    boolean updateOrderPayStatus(Long orderId, Integer status);

    /**
     * 分页查询订单
     * @param pageRequest 分页请求参数
     * @param request 当前请求
     * @return 订单分页结果
     */
    Page<RentalOrderVO> pageRentalOrder(RentalOrderPageRequest pageRequest, HttpServletRequest request);
}
