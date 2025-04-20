package com.my.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.my.common.DeleteRequest;
import com.my.domain.dto.vehicletypedict.VehicleTypeDictAddRequest;
import com.my.domain.dto.vehicletypedict.VehicleTypeDictQueryRequest;
import com.my.domain.dto.vehicletypedict.VehicleTypeDictUpdateRequest;
import com.my.domain.entity.VehicleTypeDict;
import com.my.domain.vo.VehicleTypeDictVO;

import java.util.List;

/**
* @author Administrator
* @description 针对表【vehicle_type_dict(车型字典表)】的数据库操作Service
* @createDate 2025-03-12 16:06:12
*/
public interface VehicleTypeDictService extends IService<VehicleTypeDict> {

    /**
     * 增加车型
     * @param vehicleTypeDictAddRequest
     * @return
     */
    Long addVehicleTypeDict(VehicleTypeDictAddRequest vehicleTypeDictAddRequest);

    /**
     * 更新车型
     *
     * @param vehicleTypeDictUpdateRequest
     * @return
     */
    boolean updateVehicleTypeDict(VehicleTypeDictUpdateRequest vehicleTypeDictUpdateRequest);

    /**
     * 删除车型
     * @param deleteRequest
     * @return
     */
    boolean deleteVehicleTypeDict(DeleteRequest deleteRequest);

    /**
     * 分页查询车型
     *
     * @param vehicleTypeDictQueryRequest 查询条件
     * @return 分页查询结果
     */
    Page<VehicleTypeDictVO> pageVehicleTypeDict(VehicleTypeDictQueryRequest vehicleTypeDictQueryRequest);

    /**
     * 获取车型vo
     *
     * @param vehicleTypeDict
     * @return
     */
    VehicleTypeDictVO getVehicleTypeDictVO(VehicleTypeDict vehicleTypeDict);

    /**
     * 获取车型列表
     *
     * @return
     */
    List<VehicleTypeDictVO> listVehicleTypeDict();

    /**
     * 根据id获取车型
     * @param id
     * @return
     */
    VehicleTypeDictVO getVehicleTypeDictById(Long id);
}
