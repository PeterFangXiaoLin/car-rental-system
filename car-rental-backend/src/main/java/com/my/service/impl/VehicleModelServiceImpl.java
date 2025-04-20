package com.my.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.common.DeleteRequest;
import com.my.common.ErrorCode;
import com.my.domain.dto.vehiclemodel.VehicleModelAddRequest;
import com.my.domain.dto.vehiclemodel.VehicleModelQueryRequest;
import com.my.domain.dto.vehiclemodel.VehicleModelUpdateRequest;
import com.my.domain.entity.VehicleBrand;
import com.my.domain.entity.VehicleModel;
import com.my.domain.vo.VehicleModelVO;
import com.my.exception.BusinessException;
import com.my.mapper.VehicleModelMapper;
import com.my.service.VehicleBrandService;
import com.my.service.VehicleModelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author Administrator
* @description 针对表【vehicle_model(车辆型号表)】的数据库操作Service实现
* @createDate 2025-03-12 09:50:47
*/
@Service
public class VehicleModelServiceImpl extends ServiceImpl<VehicleModelMapper, VehicleModel>
    implements VehicleModelService{

    @Resource
    private VehicleBrandService vehicleBrandService;

    @Resource
    private VehicleModelMapper vehicleModelMapper;

    @Override
    public Long addVehicleModel(VehicleModelAddRequest vehicleModelAddRequest) {
        if (vehicleModelAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 校验参数
        VehicleModel vehicleModel = BeanUtil.toBean(vehicleModelAddRequest, VehicleModel.class);
        validateVehicleModel(vehicleModel, true);

        boolean save = this.save(vehicleModel);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "车辆型号添加失败");
        }
        return vehicleModel.getId();
    }

    @Override
    public boolean updateVehicleModel(VehicleModelUpdateRequest vehicleModelUpdateRequest) {
        if (vehicleModelUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        VehicleModel newVehicleModel = BeanUtil.toBean(vehicleModelUpdateRequest, VehicleModel.class);
        validateVehicleModel(newVehicleModel, false);

        // 更新车辆型号
        boolean update = this.updateById(newVehicleModel);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "车辆型号更新失败");
        }
        return true;
    }

    @Override
    public List<VehicleModelVO> listVehicleModel(VehicleModelQueryRequest vehicleModelQueryRequest) {
        if (vehicleModelQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long brandId = vehicleModelQueryRequest.getBrandId();
        QueryWrapper<VehicleModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(brandId != null, "brandId", brandId);
        List<VehicleModel> vehicleModels = vehicleModelMapper.selectList(queryWrapper);
        return vehicleModels.stream().map(this::getVehicleModelVO).collect(Collectors.toList());
    }

    @Override
    public VehicleModelVO getVehicleModelVO(VehicleModel vehicleModel) {
        if (vehicleModel == null) {
            return null;
        }
        return BeanUtil.toBean(vehicleModel, VehicleModelVO.class);
    }

    @Override
    public boolean deleteVehicleModel(DeleteRequest deleteRequest) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = deleteRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        VehicleModel vehicleModel = this.getById(id);
        if (vehicleModel == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "车辆型号不存在");
        }
        boolean remove = this.removeById(id);
        if (!remove) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return true;
    }

    @Override
    public Page<VehicleModelVO> listVehicleModelByPage(VehicleModelQueryRequest vehicleModelQueryRequest) {
        if (vehicleModelQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long current = vehicleModelQueryRequest.getCurrent();
        long pageSize = vehicleModelQueryRequest.getPageSize();

        Page<VehicleModelVO> page = new Page<>(current, pageSize);
        return vehicleModelMapper.selectPageVO(page, vehicleModelQueryRequest);
    }

    @Override
    public VehicleModelVO getVehicleModelById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        VehicleModel vehicleModel = this.getById(id);
        if (vehicleModel == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "车辆型号不存在");
        }
        return getVehicleModelVO(vehicleModel);
    }

    private void validateVehicleModel(VehicleModel vehicleModel, boolean add) {
        if (!add) {
            Long id = vehicleModel.getId();
            if (id == null || id <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            VehicleModel oldVehicleModel = this.getById(id);
            if (oldVehicleModel == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "车辆型号不存在");
            }
        }

        String modelName = vehicleModel.getModelName();
        if (StrUtil.isBlank(modelName)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "车辆型号名称不能为空");
        }
        Long brandId = vehicleModel.getBrandId();
        if (brandId == null || brandId <= 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "品牌不存在");
        }
        VehicleBrand vehicleBrand = vehicleBrandService.getById(brandId);
        if (vehicleBrand == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "品牌不存在");
        }
    }
}




