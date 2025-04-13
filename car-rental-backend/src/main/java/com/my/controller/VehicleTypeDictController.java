package com.my.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.annotation.AuthCheck;
import com.my.common.BaseResponse;
import com.my.common.DeleteRequest;
import com.my.constant.UserConstant;
import com.my.domain.dto.vehicletypedict.VehicleTypeDictAddRequest;
import com.my.domain.dto.vehicletypedict.VehicleTypeDictQueryRequest;
import com.my.domain.dto.vehicletypedict.VehicleTypeDictUpdateRequest;
import com.my.domain.vo.VehicleTypeDictVO;
import com.my.service.VehicleTypeDictService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static com.my.common.ResultUtils.success;

/**
 * 车辆类型字典表
 *
 * @author Administrator
 * @TableName vehicle_type_dict
 */
@RestController
@RequestMapping("/vehicleTypeDict")
public class VehicleTypeDictController {

    @Resource
    private VehicleTypeDictService vehicleTypeDictService;

    @ApiOperation(value = "增加车辆类型")
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addVehicleTypeDict(@RequestBody VehicleTypeDictAddRequest vehicleTypeDictAddRequest) {
        return success(vehicleTypeDictService.addVehicleTypeDict(vehicleTypeDictAddRequest));
    }

    @ApiOperation(value = "修改车辆类型")
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateVehicleTypeDict(@RequestBody VehicleTypeDictUpdateRequest vehicleTypeDictUpdateRequest) {
        return success(vehicleTypeDictService.updateVehicleTypeDict(vehicleTypeDictUpdateRequest));
    }

    @ApiOperation(value = "删除车辆类型")
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteVehicleTypeDict(@RequestBody DeleteRequest deleteRequest) {
        return success(vehicleTypeDictService.deleteVehicleTypeDict(deleteRequest));
    }

    @ApiOperation(value = "获取车辆类型列表")
    @PostMapping("/page")
    public BaseResponse<Page<VehicleTypeDictVO>> pageVehicleTypeDict(@RequestBody VehicleTypeDictQueryRequest vehicleTypeDictQueryRequest) {
        return success(vehicleTypeDictService.pageVehicleTypeDict(vehicleTypeDictQueryRequest));
    }

    @PostMapping("/list")
    @ApiOperation(value = "获取车辆类型列表")
    public BaseResponse<List<VehicleTypeDictVO>> listVehicleTypeDict() {
        return success(vehicleTypeDictService.listVehicleTypeDict());
    }
}
