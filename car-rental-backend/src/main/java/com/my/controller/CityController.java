package com.my.controller;

import com.my.annotation.AuthCheck;
import com.my.common.BaseResponse;
import com.my.constant.UserConstant;
import com.my.service.CityService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import java.io.IOException;

import static com.my.common.ResultUtils.success;

@RestController
@RequestMapping("/city")
public class CityController {

    @Resource
    private CityService cityService;

    @PostMapping("/import")
    @ApiOperation(value = "导入城市数据")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> importCityData(@RequestPart("file")MultipartFile file) throws IOException {
        return success(cityService.importCityData(file));
    }
}
