package com.my.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.annotation.AuthCheck;
import com.my.common.BaseResponse;
import com.my.common.DeleteRequest;
import com.my.constant.UserConstant;
import com.my.domain.dto.store.StoreCreateRequest;
import com.my.domain.dto.store.StoreQueryRequest;
import com.my.domain.dto.store.StoreUpdateRequest;
import com.my.domain.vo.StoreVO;
import com.my.service.StoreService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.my.common.ResultUtils.success;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Resource
    private StoreService storeService;

    @PostMapping("/create")
    @ApiOperation(value = "创建门店")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> createStore(@RequestBody StoreCreateRequest storeCreateRequest) {
        return success(storeService.createStore(storeCreateRequest));
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新门店")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateStore(@RequestBody StoreUpdateRequest storeUpdateRequest) {
        return success(storeService.updateStore(storeUpdateRequest));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除门店")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteStore(@RequestBody DeleteRequest deleteRequest) {
        return success(storeService.deleteStore(deleteRequest));
    }

    @GetMapping("/get")
    @ApiOperation(value = "根据id获取门店")
    public BaseResponse<StoreVO> getStore(@RequestParam("id") Long id) {
        return success(storeService.getStore(id));
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页获取门店列表")
    public BaseResponse<Page<StoreVO>> listStoreByPage(@RequestBody StoreQueryRequest storeQueryRequest) {
        return success(storeService.getStoreVOByPage(storeQueryRequest));
    }

    @PostMapping("/upload")
    @ApiOperation(value = "上传门店图片")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<String>> uploadStoreImage(@RequestPart("files") MultipartFile[] files, HttpServletRequest request) {
        return success(storeService.uploadStoreImage(files, request));
    }

    @GetMapping("/list")
    @ApiOperation(value = "根据城市名获取门店列表")
    public BaseResponse<List<StoreVO>> listStoreByCityName(@RequestParam("cityName") String cityName) {
        return success(storeService.getStoreVOByCityName(cityName));
    }
}
