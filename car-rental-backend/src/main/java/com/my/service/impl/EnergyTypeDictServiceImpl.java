package com.my.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.common.DeleteRequest;
import com.my.common.ErrorCode;
import com.my.domain.dto.energytypedict.EnergyTypeDictAddRequest;
import com.my.domain.dto.energytypedict.EnergyTypeDictQueryRequest;
import com.my.domain.dto.energytypedict.EnergyTypeDictUpdateRequest;
import com.my.domain.entity.EnergyTypeDict;
import com.my.domain.vo.EnergyTypeDictVO;
import com.my.exception.BusinessException;
import com.my.exception.ThrowUtils;
import com.my.mapper.EnergyTypeDictMapper;
import com.my.service.EnergyTypeDictService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author helloworld
* @description 针对表【energy_type_dict(能源类型字典表)】的数据库操作Service实现
* @createDate 2025-04-12 15:43:39
*/
@Service
public class EnergyTypeDictServiceImpl extends ServiceImpl<EnergyTypeDictMapper, EnergyTypeDict>
    implements EnergyTypeDictService{

    @Resource
    private EnergyTypeDictMapper energyTypeDictMapper;

    @Override
    public Long addEnergyTypeDict(EnergyTypeDictAddRequest energyTypeDictAddRequest) {
        ThrowUtils.throwIf(energyTypeDictAddRequest == null, ErrorCode.PARAMS_ERROR);
        String energyTypeName = energyTypeDictAddRequest.getTypeName();
        ThrowUtils.throwIf(StrUtil.isBlank(energyTypeName) || energyTypeName.length() > 50, ErrorCode.PARAMS_ERROR);
        EnergyTypeDict energyTypeDict = BeanUtil.toBean(energyTypeDictAddRequest, EnergyTypeDict.class);
        boolean result = this.save(energyTypeDict);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return energyTypeDict.getId();
    }

    @Override
    public boolean updateEnergyTypeDict(EnergyTypeDictUpdateRequest energyTypeDictUpdateRequest) {
        ThrowUtils.throwIf(energyTypeDictUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        Long id = energyTypeDictUpdateRequest.getId();
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        String energyTypeName = energyTypeDictUpdateRequest.getTypeName();
        ThrowUtils.throwIf(StrUtil.isBlank(energyTypeName) || energyTypeName.length() > 50, ErrorCode.PARAMS_ERROR);
        EnergyTypeDict oldEnergyType = this.getById(id);
        ThrowUtils.throwIf(oldEnergyType == null, ErrorCode.NOT_FOUND_ERROR, "能源类型不存在");
        EnergyTypeDict energyTypeDict = BeanUtil.toBean(energyTypeDictUpdateRequest, EnergyTypeDict.class);
        boolean update = this.updateById(energyTypeDict);
        ThrowUtils.throwIf(!update, ErrorCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public boolean deleteEnergyTypeDict(DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null, ErrorCode.PARAMS_ERROR);
        Long id = deleteRequest.getId();
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        EnergyTypeDict oldEnergyType = this.getById(id);
        ThrowUtils.throwIf(oldEnergyType == null, ErrorCode.NOT_FOUND_ERROR, "能源类型不存在");
        boolean remove = this.removeById(id);
        ThrowUtils.throwIf(!remove, ErrorCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public EnergyTypeDictVO getEnergyTypeDictById(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        EnergyTypeDict energyTypeDict = this.getById(id);
        ThrowUtils.throwIf(energyTypeDict == null, ErrorCode.NOT_FOUND_ERROR, "能源类型不存在");
        return BeanUtil.toBean(energyTypeDict, EnergyTypeDictVO.class);
    }

    @Override
    public Page<EnergyTypeDictVO> listEnergyTypeDictByPage(EnergyTypeDictQueryRequest energyTypeDictQueryRequest) {
        if (energyTypeDictQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = energyTypeDictQueryRequest.getCurrent();
        long size = energyTypeDictQueryRequest.getPageSize();
        ThrowUtils.throwIf(current < 0 || size < 0, ErrorCode.PARAMS_ERROR);
        Page<EnergyTypeDict> energyTypeDictPage = energyTypeDictMapper.selectPage(new Page<>(current, size),
                this.getQueryWrapper(energyTypeDictQueryRequest));
        Page<EnergyTypeDictVO> energyTypeDictVOPage = new Page<>(current, size, energyTypeDictPage.getTotal());
        energyTypeDictVOPage.setRecords(this.getEnergyTypeDictList(energyTypeDictPage.getRecords()));
        return energyTypeDictVOPage;
    }

    @Override
    public QueryWrapper<EnergyTypeDict> getQueryWrapper(EnergyTypeDictQueryRequest energyTypeDictQueryRequest) {
        QueryWrapper<EnergyTypeDict> queryWrapper = new QueryWrapper<>();
        if (energyTypeDictQueryRequest == null) {
            return queryWrapper;
        }
        String typeName = energyTypeDictQueryRequest.getTypeName();
        queryWrapper.like(StrUtil.isNotBlank(typeName), "typeName", typeName);
        return queryWrapper;
    }

    @Override
    public List<EnergyTypeDictVO> getEnergyTypeDictList(List<EnergyTypeDict> energyTypeDictList) {
        if (CollUtil.isEmpty(energyTypeDictList)) {
            return new ArrayList<>();
        }

        return energyTypeDictList.stream().map(item -> BeanUtil.toBean(item, EnergyTypeDictVO.class)).collect(Collectors.toList());
    }

    @Override
    public List<EnergyTypeDictVO> listEnergyTypeDict() {
        List<EnergyTypeDict> energyTypeDictList = this.list();
        return this.getEnergyTypeDictList(energyTypeDictList);
    }


}




