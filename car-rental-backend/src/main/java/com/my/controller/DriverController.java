package com.my.controller;

import com.my.annotation.AuthCheck;
import com.my.common.BaseResponse;
import com.my.common.DeleteRequest;
import com.my.constant.UserConstant;
import com.my.domain.dto.driver.DriverAddRequest;
import com.my.domain.dto.driver.DriverQueryRequest;
import com.my.domain.dto.driver.DriverUpdateRequest;
import com.my.domain.vo.DriverVO;
import com.my.service.DriverService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.my.common.ResultUtils.success;

@RestController
@RequestMapping("/driver")
public class DriverController {

    @Resource
    private DriverService driverService;

    @PostMapping("/add")
    @ApiOperation("添加司机")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addDriver(@RequestBody DriverAddRequest driverAddRequest) {
        return success(driverService.addDriver(driverAddRequest));
    }

    @PostMapping("/update")
    @ApiOperation("修改司机")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateDriver(@RequestBody DriverUpdateRequest driverUpdateRequest) {
        return success(driverService.updateDriver(driverUpdateRequest));
    }

    @PostMapping("/delete")
    @ApiOperation("删除司机")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteDriver(@RequestBody DeleteRequest deleteRequest) {
        return success(driverService.deleteDriver(deleteRequest));
    }

    @GetMapping("/get/vo")
    @ApiOperation("获取司机")
    public BaseResponse<DriverVO> getDriverVOById(@RequestParam("id") Long id) {
        return success(driverService.getDriverVOById(id));
    }

    @PostMapping("/list/page")
    @ApiOperation("分页获取司机列表")
    public BaseResponse<DriverVO> listDriverVOByPage(@RequestBody DriverQueryRequest driverQueryRequest) {
        return success(driverService.listDriverVOByPage(driverQueryRequest));
    }
}
