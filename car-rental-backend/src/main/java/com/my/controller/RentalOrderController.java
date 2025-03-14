package com.my.controller;

import com.my.common.BaseResponse;
import com.my.domain.dto.rentalorder.RentalOrderCreateRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/rentalOrder")
public class RentalOrderController{

    @ApiOperation(value = "创建订单")
    @PostMapping("/create")
    public BaseResponse<Long> createRentalOrder(@RequestBody RentalOrderCreateRequest rentalOrderCreateRequest, HttpServletRequest request) {

    }
}
