package com.my.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.my.common.DeleteRequest;
import com.my.domain.dto.energytypedict.EnergyTypeDictAddRequest;
import com.my.domain.dto.energytypedict.EnergyTypeDictQueryRequest;
import com.my.domain.dto.energytypedict.EnergyTypeDictUpdateRequest;
import com.my.domain.entity.EnergyTypeDict;
import com.my.domain.vo.EnergyTypeDictVO;

import java.util.List;

/**
* @author helloworld
* @description 针对表【energy_type_dict(能源类型字典表)】的数据库操作Service
* @createDate 2025-04-12 15:43:39
*/
public interface EnergyTypeDictService extends IService<EnergyTypeDict> {

    /**
     * 添加能源类型字典
     * @param energyTypeDictAddRequest
     * @return
     */
    Long addEnergyTypeDict(EnergyTypeDictAddRequest energyTypeDictAddRequest);

    /**
     * 更新能源类型字典
     * @param energyTypeDictUpdateRequest
     * @return
     */
    boolean updateEnergyTypeDict(EnergyTypeDictUpdateRequest energyTypeDictUpdateRequest);

    /**
     * 删除能源类型字典
     * @param deleteRequest
     * @return
     */
    boolean deleteEnergyTypeDict(DeleteRequest deleteRequest);

    /**
     * 根据ID查询能源类型字典
     * @param id
     * @return
     */
    EnergyTypeDictVO getEnergyTypeDictById(Long id);

    /**
     * 分页查询能源类型字典
     * @param energyTypeDictQueryRequest
     * @return
     */
    Page<EnergyTypeDictVO> listEnergyTypeDictByPage(EnergyTypeDictQueryRequest energyTypeDictQueryRequest);

    /**
     * 分页查询能源类型字典
     * @param energyTypeDictQueryRequest
     * @return
     */
    QueryWrapper<EnergyTypeDict> getQueryWrapper(EnergyTypeDictQueryRequest energyTypeDictQueryRequest);

    /**
     * vo转换
     * @param energyTypeDictList
     * @return
     */
    List<EnergyTypeDictVO> getEnergyTypeDictList(List<EnergyTypeDict> energyTypeDictList);

    /**
     * 查询能源类型字典列表
     *
     * @return
     */
    List<EnergyTypeDictVO> listEnergyTypeDict();
}
