package com.my.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.annotation.AuthCheck;
import com.my.common.BaseResponse;
import com.my.common.DeleteRequest;
import com.my.constant.UserConstant;
import com.my.domain.dto.vehiclebrand.VehicleBrandAddRequest;
import com.my.domain.dto.vehiclebrand.VehicleBrandQueryRequest;
import com.my.domain.dto.vehiclebrand.VehicleBrandUpdateRequest;
import com.my.domain.vo.VehicleBrandVO;
import com.my.service.VehicleBrandService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.my.common.ResultUtils.success;

@RestController
@RequestMapping("/vehicle/brand")
public class VehicleBrandController {

    @Resource
    private VehicleBrandService vehicleBrandService;

    @ApiOperation(value = "添加车辆品牌")
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addVehicleBrand(@RequestBody VehicleBrandAddRequest vehicleBrandAddRequest) {
        return success(vehicleBrandService.addVehicleBrand(vehicleBrandAddRequest));
    }

    @ApiOperation(value = "更新车辆品牌")
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateVehicleBrand(@RequestBody VehicleBrandUpdateRequest vehicleBrandUpdateRequest) {
        return success(vehicleBrandService.updateVehicleBrand(vehicleBrandUpdateRequest));
    }

    @ApiOperation(value = "删除车辆品牌")
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteVehicleBrand(@RequestBody DeleteRequest deleteRequest) {
        return success(vehicleBrandService.deleteVehicleBrand(deleteRequest));
    }

    @ApiOperation(value = "查询车辆品牌")
    @PostMapping("/list")
    public BaseResponse<List<VehicleBrandVO>> listVehicleBrand() {
        return success(vehicleBrandService.listVehicleBrand());
    }

    @ApiOperation(value = "查询车辆品牌")
    @PostMapping("/list/page")
    public BaseResponse<Page<VehicleBrandVO>> listVehicleBrandByPage(@RequestBody VehicleBrandQueryRequest vehicleBrandQueryRequest) {
        return success(vehicleBrandService.listVehicleBrandByPage(vehicleBrandQueryRequest));
    }

    @ApiOperation(value = "根据id获取车辆品牌")
    @GetMapping("/get")
    public BaseResponse<VehicleBrandVO> getVehicleBrandById(@RequestParam("id") Long id) {
        return success(vehicleBrandService.getVehicleBrandById(id));
    }

    @ApiOperation(value = "根据首字母获得车辆品牌")
    @GetMapping("/list/letter")
    public BaseResponse<List<VehicleBrandVO>> listVehicleBrandByLetter(@RequestParam("letter") String letter) {
        return success(vehicleBrandService.listVehicleBrandByLetter(letter));
    }
}
