package com.my.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.annotation.AuthCheck;
import com.my.common.BaseResponse;
import com.my.common.DeleteRequest;
import com.my.constant.UserConstant;
import com.my.domain.dto.vehicle.VehicleAddRequest;
import com.my.domain.dto.vehicle.VehicleQueryRequest;
import com.my.domain.dto.vehicle.VehicleUpdateRequest;
import com.my.domain.vo.VehicleVO;
import com.my.service.VehicleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static com.my.common.ResultUtils.success;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Resource
    private VehicleService vehicleService;

    @ApiOperation(value = "新增车辆")
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addVehicle(@RequestBody VehicleAddRequest vehicleAddRequest) {
        return success(vehicleService.addVehicle(vehicleAddRequest));
    }

    @ApiOperation(value = "修改车辆")
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateVehicle(@RequestBody VehicleUpdateRequest vehicleUpdateRequest) {
        return success(vehicleService.updateVehicle(vehicleUpdateRequest));
    }

    @ApiOperation(value = "删除车辆")
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteVehicle(@RequestBody DeleteRequest deleteRequest) {
        return success(vehicleService.deleteVehicle(deleteRequest));
    }

    @ApiOperation(value = "根据 id 获取车辆")
    @GetMapping("/get")
    public BaseResponse<VehicleVO> getVehicleById(@RequestParam("id") Long id) {
        return success(vehicleService.getVehicleById(id));
    }

    @ApiOperation(value = "分页获取车辆列表")
    @PostMapping("/page")
    public BaseResponse<Page<VehicleVO>> listVehicleByPage(@RequestBody VehicleQueryRequest vehicleQueryRequest) {
        return success(vehicleService.listVehicleByPage(vehicleQueryRequest));
    }

    @ApiOperation(value = "推荐车辆")
    @PostMapping("/recommend")
    public BaseResponse<List<VehicleVO>> recommendVehicle(HttpServletRequest request) {
        return success(vehicleService.recommendVehicle(request));
    }
}
