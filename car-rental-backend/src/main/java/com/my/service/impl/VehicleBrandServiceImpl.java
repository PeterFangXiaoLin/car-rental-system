package com.my.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.common.DeleteRequest;
import com.my.common.ErrorCode;
import com.my.constant.CommonConstant;
import com.my.domain.dto.vehiclebrand.VehicleBrandAddRequest;
import com.my.domain.dto.vehiclebrand.VehicleBrandQueryRequest;
import com.my.domain.dto.vehiclebrand.VehicleBrandUpdateRequest;
import com.my.domain.entity.VehicleBrand;
import com.my.domain.vo.VehicleBrandVO;
import com.my.exception.BusinessException;
import com.my.mapper.VehicleBrandMapper;
import com.my.service.VehicleBrandService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author helloworld
* @description 针对表【vehicle_brand(车辆品牌表)】的数据库操作Service实现
* @createDate 2025-03-11 21:24:22
*/
@Service
public class VehicleBrandServiceImpl extends ServiceImpl<VehicleBrandMapper, VehicleBrand>
    implements VehicleBrandService{

    @Resource
    private VehicleBrandMapper vehicleBrandMapper;

    @Override
    public Long addVehicleBrand(VehicleBrandAddRequest vehicleBrandAddRequest) {
        if (vehicleBrandAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        VehicleBrand vehicleBrand = BeanUtil.toBean(vehicleBrandAddRequest, VehicleBrand.class);
        validate(vehicleBrand, true);

        boolean save = this.save(vehicleBrand);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return vehicleBrand.getId();
    }

    @Override
    public boolean updateVehicleBrand(VehicleBrandUpdateRequest vehicleBrandUpdateRequest) {
        if (vehicleBrandUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = vehicleBrandUpdateRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        VehicleBrand vehicleBrand = this.getById(id);
        if (ObjUtil.isNull(vehicleBrand)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "品牌不存在");
        }
        VehicleBrand updateVehicleBrand = BeanUtil.toBean(vehicleBrandUpdateRequest, VehicleBrand.class);
        validate(updateVehicleBrand, false);
        boolean update = this.updateById(updateVehicleBrand);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return true;
    }

    @Override
    public boolean deleteVehicleBrand(DeleteRequest deleteRequest) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = deleteRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        VehicleBrand vehicleBrand = this.getById(id);
        if (ObjUtil.isNull(vehicleBrand)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "品牌不存在");
        }
        boolean remove = this.removeById(id);
        if (!remove) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return true;
    }

    @Override
    public List<VehicleBrandVO> listVehicleBrand() {
        List<VehicleBrand> vehicleBrandList = vehicleBrandMapper.selectList(null);
        return vehicleBrandList.stream().map(this::getVehicleBrandVO).collect(Collectors.toList());
    }

    @Override
    public VehicleBrandVO getVehicleBrandVO(VehicleBrand vehicleBrand) {
        if (vehicleBrand == null) {
            return null;
        }
        return BeanUtil.toBean(vehicleBrand, VehicleBrandVO.class);
    }

    @Override
    public Page<VehicleBrandVO> listVehicleBrandByPage(VehicleBrandQueryRequest vehicleBrandQueryRequest) {
        if (vehicleBrandQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String searchText = vehicleBrandQueryRequest.getSearchText();
        long pageSize = vehicleBrandQueryRequest.getPageSize();
        long current = vehicleBrandQueryRequest.getCurrent();
        String sortOrder = vehicleBrandQueryRequest.getSortOrder();
        String sortField = vehicleBrandQueryRequest.getSortField();

        QueryWrapper<VehicleBrand> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(searchText), "brandName", searchText);
        queryWrapper.orderBy(StrUtil.isNotBlank(sortField), sortOrder.equalsIgnoreCase(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<VehicleBrand> vehicleBrandPage = vehicleBrandMapper.selectPage(new Page<>(current, pageSize), queryWrapper);
        List<VehicleBrandVO> vehicleBrandVOList = vehicleBrandPage.getRecords().stream().map(this::getVehicleBrandVO).collect(Collectors.toList());
        Page<VehicleBrandVO> vehicleBrandVOPage = new Page<>(current, pageSize, vehicleBrandPage.getTotal());
        vehicleBrandVOPage.setRecords(vehicleBrandVOList);
        return vehicleBrandVOPage;
    }

    @Override
    public VehicleBrandVO getVehicleBrandById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        VehicleBrand vehicleBrand = this.getById(id);
        if (ObjUtil.isNull(vehicleBrand)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "品牌不存在");
        }
        return this.getVehicleBrandVO(vehicleBrand);
    }

    @Override
    public List<VehicleBrandVO> listVehicleBrandByLetter(String letter) {
        return this.query().eq("firstLetter", letter).list().stream().map(this::getVehicleBrandVO).collect(Collectors.toList());
    }

    private void validate(VehicleBrand vehicleBrand, boolean add) {
        if (vehicleBrand == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String brandName = vehicleBrand.getBrandName();
        if (StrUtil.isBlank(brandName)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (brandName.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "品牌名称过长");
        }
    }
}




