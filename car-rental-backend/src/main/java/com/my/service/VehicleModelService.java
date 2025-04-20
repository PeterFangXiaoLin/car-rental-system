package com.my.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.common.DeleteRequest;
import com.my.domain.dto.vehiclemodel.VehicleModelAddRequest;
import com.my.domain.dto.vehiclemodel.VehicleModelQueryRequest;
import com.my.domain.dto.vehiclemodel.VehicleModelUpdateRequest;
import com.my.domain.entity.VehicleModel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.my.domain.vo.VehicleModelVO;

import java.util.List;

/**
* @author Administrator
* @description 针对表【vehicle_model(车辆型号表)】的数据库操作Service
* @createDate 2025-03-12 09:50:47
*/
public interface VehicleModelService extends IService<VehicleModel> {

    /**
     * 增加车辆型号
     *
     * @param vehicleModelAddRequest
     * @return
     */
    Long addVehicleModel(VehicleModelAddRequest vehicleModelAddRequest);

    /**
     * 更新车辆型号
     *
     * @param vehicleModelUpdateRequest
     * @return
     */
    boolean updateVehicleModel(VehicleModelUpdateRequest vehicleModelUpdateRequest);

    /**
     * 获取车辆型号列表
     *
     * @param vehicleModelQueryRequest
     * @return
     */
    List<VehicleModelVO> listVehicleModel(VehicleModelQueryRequest vehicleModelQueryRequest);

    /**
     * 获取车辆型号vo
     *
     * @param vehicleModel
     * @return
     */
    VehicleModelVO getVehicleModelVO(VehicleModel vehicleModel);

    /**
     * 删除车辆型号
     * @param deleteRequest
     * @return
     */
    boolean deleteVehicleModel(DeleteRequest deleteRequest);

    /**
     * 分页获取车辆型号列表
     *
     * @param vehicleModelQueryRequest
     * @return
     */
    Page<VehicleModelVO> listVehicleModelByPage(VehicleModelQueryRequest vehicleModelQueryRequest);

    /**
     * 根据id获取车辆型号
     *
     * @param id
     * @return
     */
    VehicleModelVO getVehicleModelById(Long id);
}
