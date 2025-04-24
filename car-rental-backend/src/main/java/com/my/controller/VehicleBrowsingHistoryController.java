package com.my.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.common.BaseResponse;
import com.my.common.DeleteRequest;
import com.my.domain.dto.vehiclebrowsinghistory.BrowsHistoryAddRequest;
import com.my.domain.dto.vehiclebrowsinghistory.BrowsHistoryQueryRequest;
import com.my.domain.vo.VehicleBrowsingHistoryVO;
import com.my.service.VehicleBrowsingHistoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.my.common.ResultUtils.success;

@RestController
@RequestMapping("/vehicle/brows")
public class VehicleBrowsingHistoryController {

    @Resource
    private VehicleBrowsingHistoryService vehicleBrowsingHistoryService;

    @PostMapping("/addOrUpdate")
    @ApiOperation(value = "增加/更新浏览历史")
    public BaseResponse<Long> addOrUpdateBrowsHistory(@RequestBody BrowsHistoryAddRequest browsHistoryAddRequest, HttpServletRequest request) {
        return success(vehicleBrowsingHistoryService.addBrowsHistory(browsHistoryAddRequest, request));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除浏览历史")
    public BaseResponse<Boolean> deleteBrowsHistory(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        return success(vehicleBrowsingHistoryService.deleteBrowsHistory(deleteRequest, request));
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询浏览历史")
    public BaseResponse<Page<VehicleBrowsingHistoryVO>> pageBrowseHistory(@RequestBody BrowsHistoryQueryRequest browsHistoryQueryRequest, HttpServletRequest request) {
        return success(vehicleBrowsingHistoryService.pageBrowseHistory(browsHistoryQueryRequest, request))
    }
}
