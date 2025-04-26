package com.my.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.common.BaseResponse;
import com.my.domain.dto.vehiclefavorite.VehicleFavoriteAddRequest;
import com.my.domain.dto.vehiclefavorite.VehicleFavoriteCancelRequest;
import com.my.domain.dto.vehiclefavorite.VehicleFavoriteQueryRequest;
import com.my.domain.vo.VehicleFavoriteVO;
import com.my.service.VehicleFavoriteService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.my.common.ResultUtils.success;

@RestController
@RequestMapping("/vehicleFavorite")
public class VehicleFavoriteController {

    @Resource
    private VehicleFavoriteService vehicleFavoriteService;;

    @PostMapping("/add")
    @ApiOperation(value = "添加收藏")
    public BaseResponse<Long> addVehicleFavorite(@RequestBody VehicleFavoriteAddRequest vehicleFavoriteAddRequest, HttpServletRequest request) {
        return success(vehicleFavoriteService.addVehicleFavorite(vehicleFavoriteAddRequest, request));
    }

    @PostMapping("/cancel")
    @ApiOperation(value = "取消收藏")
    public BaseResponse<Boolean> cancelVehicleFavorite(@RequestBody VehicleFavoriteCancelRequest vehicleFavoriteCancelRequest, HttpServletRequest request) {
        return success(vehicleFavoriteService.cancelVehicleFavorite(vehicleFavoriteCancelRequest, request));
    }

    @GetMapping("/check")
    @ApiOperation(value = "检查是否收藏")
    public BaseResponse<Boolean> checkVehicleFavorite(@RequestParam("vehicleId") Long vehicleId, HttpServletRequest request) {
        return success(vehicleFavoriteService.checkVehicleFavorite(vehicleId, request));
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询收藏")
    public BaseResponse<Page<VehicleFavoriteVO>> pageVehicleFavorite(@RequestBody VehicleFavoriteQueryRequest vehicleFavoriteQueryRequest, HttpServletRequest request) {
        return success(vehicleFavoriteService.pageVehicleFavorite(vehicleFavoriteQueryRequest, request));
    }
}
