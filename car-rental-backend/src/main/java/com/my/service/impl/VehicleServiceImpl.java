package com.my.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.common.DeleteRequest;
import com.my.common.ErrorCode;
import com.my.constant.CommonConstant;
import com.my.domain.dto.vehicle.VehicleAddRequest;
import com.my.domain.dto.vehicle.VehicleQueryRequest;
import com.my.domain.dto.vehicle.VehicleUpdateRequest;
import com.my.domain.entity.*;
import com.my.domain.enums.VehicleStatusEnum;
import com.my.domain.vo.VehicleVO;
import com.my.exception.BusinessException;
import com.my.mapper.VehicleMapper;
import com.my.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

/**
* @author Administrator
* @description 针对表【vehicle(车辆表)】的数据库操作Service实现
* @createDate 2025-03-12 16:44:26
*/
@Service
public class VehicleServiceImpl extends ServiceImpl<VehicleMapper, Vehicle>
    implements VehicleService{

    @Resource
    private VehicleMapper vehicleMapper;

    @Resource
    private VehicleBrandService vehicleBrandService;

    @Resource
    private VehicleModelService vehicleModelService;

    @Resource
    private VehicleTypeDictService vehicleTypeDictService;

    @Resource
    private EnergyTypeDictService energyTypeDictService;

    private void validate(Vehicle vehicle, boolean add) {
        if (vehicle == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long id = vehicle.getId();
        String vehicleNo = vehicle.getVehicleNo();
        Long brandId = vehicle.getBrandId();
        Long modelId = vehicle.getModelId();
        Long vehicleTypeId = vehicle.getVehicleTypeId();
        BigDecimal dailyPrice = vehicle.getDailyPrice();
        Integer status = vehicle.getStatus();

        if (!add) {
            if (id == null || id <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "车辆ID不能为空");
            }
            Vehicle oldVehicle = vehicleMapper.selectById(id);
            if (oldVehicle == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "车辆不存在");
            }
        }

        if (StrUtil.isBlank(vehicleNo)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "车牌号不能为空");
        }
        if (brandId == null || brandId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "品牌不存在");
        }
        boolean brandExists = vehicleBrandService.lambdaQuery().eq(VehicleBrand::getId, brandId).exists();
        if (!brandExists) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "品牌不存在");
        }

        if (modelId == null || modelId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "型号不存在");
        }
        boolean modelExists = vehicleModelService.lambdaQuery()
                .eq(VehicleModel::getId, modelId)
                .eq(VehicleModel::getBrandId, brandId)
                .exists();
        if (!modelExists) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "型号不存在");
        }
        if (vehicleTypeId == null || vehicleTypeId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "车辆类型不存在");
        }
        boolean vehicleTypeExists = vehicleTypeDictService.lambdaQuery().eq(VehicleTypeDict::getId, vehicleTypeId).exists();
        if (!vehicleTypeExists) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "车辆类型不存在");
        }

        if (dailyPrice == null || dailyPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "日租金不能为空");
        }
        if (status == null || VehicleStatusEnum.getEnumByValue(status) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "状态非法");
        }
    }

    @Override
    public Long addVehicle(VehicleAddRequest vehicleAddRequest) {
        if (vehicleAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 校验参数
        Vehicle vehicle = BeanUtil.toBean(vehicleAddRequest, Vehicle.class);
        validate(vehicle, true);

        // 插入数据
        boolean saveResult = this.save(vehicle);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "新增车辆失败");
        }
        return vehicle.getId();
    }

    @Override
    public boolean updateVehicle(VehicleUpdateRequest vehicleUpdateRequest) {
        if (vehicleUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 校验参数
        Vehicle vehicle = BeanUtil.toBean(vehicleUpdateRequest, Vehicle.class);
        validate(vehicle, false);

        // 更新数据
        boolean updateResult = this.updateById(vehicle);
        if (!updateResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新车辆失败");
        }
        return true;
    }

    @Override
    public boolean deleteVehicle(DeleteRequest deleteRequest) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = deleteRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "车辆ID不能为空");
        }
        // 校验存在
        Vehicle oldVehicle = vehicleMapper.selectById(id);
        if (oldVehicle == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "车辆不存在");
        }
        // 删除数据
        boolean deleteResult = this.removeById(id);
        if (!deleteResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除车辆失败");
        }
        return true;
    }

    @Override
    public VehicleVO getVehicleById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Vehicle vehicle = vehicleMapper.selectById(id);
        if (vehicle == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "车辆不存在");
        }
        return this.getVehicleVO(vehicle);
    }

    @Override
    public VehicleVO getVehicleVO(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        VehicleVO vehicleVO = BeanUtil.toBean(vehicle, VehicleVO.class);
        VehicleBrand vehicleBrand = vehicleBrandService.getById(vehicle.getBrandId());
        VehicleModel vehicleModel = vehicleModelService.getById(vehicle.getModelId());
        VehicleTypeDict vehicleTypeDict = vehicleTypeDictService.getById(vehicle.getVehicleTypeId());
        EnergyTypeDict energyTypeDict = energyTypeDictService.getById(vehicle.getEnergyTypeId());
        vehicleVO.setBrandName(Optional.ofNullable(vehicleBrand.getBrandName()).orElse(StrUtil.EMPTY));
        vehicleVO.setModelName(Optional.ofNullable(vehicleModel.getModelName()).orElse(StrUtil.EMPTY));
        vehicleVO.setVehicleTypeName(Optional.ofNullable(vehicleTypeDict.getTypeName()).orElse(StrUtil.EMPTY));
        vehicleVO.setEnergyTypeName(Optional.ofNullable(energyTypeDict.getTypeName()).orElse(StrUtil.EMPTY));
        return vehicleVO;
    }

    @Override
    public Page<VehicleVO> listVehicleByPage(VehicleQueryRequest vehicleQueryRequest) {
        if (vehicleQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long current = vehicleQueryRequest.getCurrent();
        long pageSize = vehicleQueryRequest.getPageSize();
        if (current <= 0 || pageSize <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分页参数不合法");
        }

        Page<VehicleVO> page = new Page<>(current, pageSize);
        return vehicleMapper.selectPageVO(page, vehicleQueryRequest);
    }

    @Override
    public QueryWrapper<Vehicle> getQueryWrapper(VehicleQueryRequest vehicleQueryRequest) {
        QueryWrapper<Vehicle> queryWrapper = new QueryWrapper<>();
        if (vehicleQueryRequest == null) {
            return queryWrapper;
        }

        Long id = vehicleQueryRequest.getId();
        String name = vehicleQueryRequest.getName();
        String vehicleNo = vehicleQueryRequest.getVehicleNo();
        Long brandId = vehicleQueryRequest.getBrandId();
        Long modelId = vehicleQueryRequest.getModelId();
        Long vehicleTypeId = vehicleQueryRequest.getVehicleTypeId();
        BigDecimal minDailyPrice = vehicleQueryRequest.getMinDailyPrice();
        BigDecimal maxDailyPrice = vehicleQueryRequest.getMaxDailyPrice();
        Integer status = vehicleQueryRequest.getStatus();
        String description = vehicleQueryRequest.getDescription();
        String sortField = vehicleQueryRequest.getSortField();
        String sortOrder = vehicleQueryRequest.getSortOrder();
        String searchText = vehicleQueryRequest.getSearchText();
        if (StrUtil.isNotBlank(searchText)) {
            queryWrapper.and(qw -> qw.like("name", searchText).or().like("description", searchText));
        }
        Long energyTypeId = vehicleQueryRequest.getEnergyTypeId();
        Integer seatCount = vehicleQueryRequest.getSeatCount();

        queryWrapper.eq(id != null, "id", id);
        queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        queryWrapper.like(StrUtil.isNotBlank(vehicleNo), "vehicleNo", vehicleNo);
        queryWrapper.eq(brandId != null, "brandId", brandId);
        queryWrapper.eq(modelId != null, "modelId", modelId);
        queryWrapper.eq(vehicleTypeId != null, "vehicleTypeId", vehicleTypeId);
        queryWrapper.eq(energyTypeId != null, "energyTypeId", energyTypeId);
        queryWrapper.eq(seatCount!= null, "seatCount", seatCount);
        queryWrapper.ge(minDailyPrice!= null, "dailyPrice", minDailyPrice);
        queryWrapper.le(maxDailyPrice!= null, "dailyPrice", maxDailyPrice);
        queryWrapper.eq(status!= null, "status", status);
        queryWrapper.like(StrUtil.isNotBlank(description), "description", description);
        queryWrapper.orderBy(StrUtil.isNotBlank(sortField), sortOrder.equalsIgnoreCase(CommonConstant.SORT_ORDER_ASC), sortField);

        return queryWrapper;
    }
}




