package com.my.service;

import com.my.domain.dto.rentalorder.RentalOrderCreateRequest;
import com.my.domain.entity.RentalOrder;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
