package com.my.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.common.DeleteRequest;
import com.my.common.ErrorCode;
import com.my.domain.dto.vehicletypedict.VehicleTypeDictAddRequest;
import com.my.domain.dto.vehicletypedict.VehicleTypeDictQueryRequest;
import com.my.domain.dto.vehicletypedict.VehicleTypeDictUpdateRequest;
import com.my.domain.entity.VehicleTypeDict;
import com.my.domain.vo.VehicleTypeDictVO;
import com.my.exception.BusinessException;
import com.my.mapper.VehicleMapper;
import com.my.mapper.VehicleTypeDictMapper;
import com.my.service.VehicleTypeDictService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author Administrator
* @description 针对表【vehicle_type_dict(车型字典表)】的数据库操作Service实现
* @createDate 2025-03-12 16:06:12
*/
@Service
public class VehicleTypeDictServiceImpl extends ServiceImpl<VehicleTypeDictMapper, VehicleTypeDict>
    implements VehicleTypeDictService{

    @Resource
    private VehicleTypeDictMapper vehicleTypeDictMapper;

    @Resource
    private VehicleMapper vehicleMapper;

    @Override
    public Long addVehicleTypeDict(VehicleTypeDictAddRequest vehicleTypeDictAddRequest) {
        if (vehicleTypeDictAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String typeName = vehicleTypeDictAddRequest.getTypeName();
        if (StrUtil.isBlank(typeName)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        VehicleTypeDict vehicleTypeDict = BeanUtil.toBean(vehicleTypeDictAddRequest, VehicleTypeDict.class);
        boolean save = this.save(vehicleTypeDict);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return vehicleTypeDict.getId();
    }

    @Override
    public boolean updateVehicleTypeDict(VehicleTypeDictUpdateRequest vehicleTypeDictUpdateRequest) {
        if (vehicleTypeDictUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = vehicleTypeDictUpdateRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        VehicleTypeDict vehicleTypeDict = this.getById(id);
        if (vehicleTypeDict == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        String typeName = vehicleTypeDictUpdateRequest.getTypeName();
        if (StrUtil.isBlank(typeName)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean update = this.updateById(vehicleTypeDict);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return true;
    }

    @Override
    public boolean deleteVehicleTypeDict(DeleteRequest deleteRequest) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = deleteRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        VehicleTypeDict vehicleTypeDict = this.getById(id);
        if (vehicleTypeDict == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 检查是否有车辆关联
        long count = vehicleMapper.countByTypeId(id);
        if (count > 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "该车型下有车辆关联，无法删除");
        }
        boolean remove = this.removeById(id);
        if (!remove) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return true;
    }

    @Override
    public Page<VehicleTypeDictVO> pageVehicleTypeDict(VehicleTypeDictQueryRequest vehicleTypeDictQueryRequest) {
        QueryWrapper<VehicleTypeDict> queryWrapper = new QueryWrapper<>();
        String typeName = vehicleTypeDictQueryRequest.getTypeName();
        queryWrapper.like(StrUtil.isNotBlank(vehicleTypeDictQueryRequest.getTypeName()), "typeName", typeName);

        int pageSize = vehicleTypeDictQueryRequest.getPageSize();
        int current = vehicleTypeDictQueryRequest.getCurrent();
        if (pageSize <= 0 || current <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Page<VehicleTypeDict> vehicleTypeDictPage = vehicleTypeDictMapper.selectPage(new Page<>(vehicleTypeDictQueryRequest.getCurrent(), vehicleTypeDictQueryRequest.getPageSize()), queryWrapper);
        List<VehicleTypeDict> vehicleTypeDictList = vehicleTypeDictPage.getRecords();
        List<VehicleTypeDictVO> vehicleTypeDictVOList = vehicleTypeDictList.stream().map(this::getVehicleTypeDictVO).collect(Collectors.toList());
        Page<VehicleTypeDictVO> vehicleTypeDictVOPage = new Page<>(current, pageSize, vehicleTypeDictPage.getTotal());
        vehicleTypeDictVOPage.setRecords(vehicleTypeDictVOList);
        return vehicleTypeDictVOPage;
    }

    @Override
    public VehicleTypeDictVO getVehicleTypeDictVO(VehicleTypeDict vehicleTypeDict) {
        if (vehicleTypeDict == null) {
            return null;
        }
        return BeanUtil.toBean(vehicleTypeDict, VehicleTypeDictVO.class);
    }

    @Override
    public List<VehicleTypeDictVO> listVehicleTypeDict() {
        List<VehicleTypeDict> vehicleTypeDictList = this.list();
        if (CollUtil.isEmpty(vehicleTypeDictList)) {
            return new ArrayList<>();
        }
        return vehicleTypeDictList.stream().map(this::getVehicleTypeDictVO).collect(Collectors.toList());
    }

    @Override
    public VehicleTypeDictVO getVehicleTypeDictById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        VehicleTypeDict vehicleTypeDict = this.getById(id);
        if (vehicleTypeDict == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "车辆类型不存在");
        }
        return getVehicleTypeDictVO(vehicleTypeDict);
    }


}




