package com.my.controller;

import com.my.annotation.AuthCheck;
import com.my.common.BaseResponse;
import com.my.common.DeleteRequest;
import com.my.constant.UserConstant;
import com.my.domain.dto.city.CityAddRequest;
import com.my.domain.dto.city.CityUpdateRequest;
import com.my.domain.vo.CityVO;
import com.my.service.CityService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

import static com.my.common.ResultUtils.success;

@RestController
@RequestMapping("/city")
public class CityController {

    @Resource
    private CityService cityService;

    @ApiOperation(value = "增加城市")
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addCity(@RequestBody CityAddRequest cityAddRequest) {
        return success(cityService.addCity(cityAddRequest));
    }

    @ApiOperation(value = "修改城市")
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateCity(@RequestBody CityUpdateRequest cityUpdateRequest) {
        return success(cityService.updateCity(cityUpdateRequest));
    }

    @ApiOperation(value = "删除城市")
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteCity(@RequestBody DeleteRequest deleteRequest) {
        return success(cityService.deleteCity(deleteRequest));
    }

    @ApiOperation(value = "获取城市列表")
    @GetMapping("/list")
    public BaseResponse<List<CityVO>> listCity() {
        return success(cityService.listCity());
    }

    @PostMapping("/import")
    @ApiOperation(value = "导入城市数据")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> importCityData(@RequestPart("file")MultipartFile file) throws IOException {
        return success(cityService.importCityData(file));
    }
}
