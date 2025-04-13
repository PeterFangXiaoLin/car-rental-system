package com.my.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.annotation.AuthCheck;
import com.my.common.BaseResponse;
import com.my.common.DeleteRequest;
import com.my.constant.UserConstant;
import com.my.domain.dto.energytypedict.EnergyTypeDictAddRequest;
import com.my.domain.dto.energytypedict.EnergyTypeDictQueryRequest;
import com.my.domain.dto.energytypedict.EnergyTypeDictUpdateRequest;
import com.my.domain.vo.EnergyTypeDictVO;
import com.my.service.EnergyTypeDictService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.my.common.ResultUtils.success;

/**
 * 能源类型字典表
 *
 * @author helloworld
 */
@RestController
@RequestMapping("/energyTypeDict")
public class EnergyTypeDictController {

    @Resource
    private EnergyTypeDictService energyTypeDictService;

    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @ApiOperation(value = "添加能源类型字典")
    public BaseResponse<Long> addEnergyTypeDict(@RequestBody EnergyTypeDictAddRequest energyTypeDictAddRequest) {
        return success(energyTypeDictService.addEnergyTypeDict(energyTypeDictAddRequest));
    }

    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @ApiOperation(value = "更新能源类型字典")
    public BaseResponse<Boolean> updateEnergyTypeDict(@RequestBody EnergyTypeDictUpdateRequest energyTypeDictUpdateRequest) {
        return success(energyTypeDictService.updateEnergyTypeDict(energyTypeDictUpdateRequest));
    }

    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @ApiOperation(value = "删除能源类型字典")
    public BaseResponse<Boolean> deleteEnergyTypeDict(@RequestBody DeleteRequest deleteRequest) {
        return success(energyTypeDictService.deleteEnergyTypeDict(deleteRequest));
    }

    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @ApiOperation(value = "根据id获取能源类型字典")
    public BaseResponse<EnergyTypeDictVO> getEnergyTypeDictById(@RequestParam("id") Long id) {
        return success(energyTypeDictService.getEnergyTypeDictById(id));
    }

    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @ApiOperation(value = "分页获取能源类型字典列表")
    public BaseResponse<Page<EnergyTypeDictVO>> listEnergyTypeDictByPage(@RequestBody EnergyTypeDictQueryRequest energyTypeDictQueryRequest) {
        return success(energyTypeDictService.listEnergyTypeDictByPage(energyTypeDictQueryRequest));
    }

    @PostMapping("/list")
    @ApiOperation(value = "获取能源类型字典列表")
    public BaseResponse<List<EnergyTypeDictVO>> listEnergyTypeDict() {
        return success(energyTypeDictService.listEnergyTypeDict());
    }
}
