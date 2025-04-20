package com.my.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.annotation.AuthCheck;
import com.my.common.BaseResponse;
import com.my.common.DeleteRequest;
import com.my.constant.UserConstant;
import com.my.domain.dto.vehiclemodel.VehicleModelAddRequest;
import com.my.domain.dto.vehiclemodel.VehicleModelQueryRequest;
import com.my.domain.dto.vehiclemodel.VehicleModelUpdateRequest;
import com.my.domain.vo.VehicleModelVO;
import com.my.service.VehicleModelService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.my.common.ResultUtils.success;

/**
 * 车辆型号表
 *
 * @TableName vehicle_model
 */
@RestController
@RequestMapping("/vehicle/model")
public class VehicleModelController {

    @Resource
    private VehicleModelService vehicleModelService;

    @ApiOperation(value = "新增车辆型号")
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addVehicleModel(@RequestBody VehicleModelAddRequest vehicleModelAddRequest) {
        return success(vehicleModelService.addVehicleModel(vehicleModelAddRequest));
    }

    @ApiOperation(value = "修改车辆型号")
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateVehicleModel(@RequestBody VehicleModelUpdateRequest vehicleModelUpdateRequest) {
        return success(vehicleModelService.updateVehicleModel(vehicleModelUpdateRequest));
    }

    @ApiOperation(value = "获取车辆型号列表")
    @PostMapping("/list")
    public BaseResponse<List<VehicleModelVO>> listVehicleModel(@RequestBody VehicleModelQueryRequest vehicleModelQueryRequest) {
        return success(vehicleModelService.listVehicleModel(vehicleModelQueryRequest));
    }

    @ApiOperation(value = "删除车辆型号")
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteVehicleModel(@RequestBody DeleteRequest deleteRequest) {
        return success(vehicleModelService.deleteVehicleModel(deleteRequest));
    }

    @ApiOperation(value = "获取车辆型号分页")
    @PostMapping("/page")
    public BaseResponse<Page<VehicleModelVO>> listVehicleModelByPage(@RequestBody VehicleModelQueryRequest vehicleModelQueryRequest) {
        return success(vehicleModelService.listVehicleModelByPage(vehicleModelQueryRequest));
    }

    @ApiOperation(value = "获取车辆型号VO")
    @GetMapping("/get")
    public BaseResponse<VehicleModelVO> getVehicleModelById(@RequestParam("id") Long id) {
        return success(vehicleModelService.getVehicleModelById(id));
    }
}
