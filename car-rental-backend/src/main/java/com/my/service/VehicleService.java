package com.my.service;

import com.my.common.DeleteRequest;
import com.my.domain.dto.vehicle.VehicleAddRequest;
import com.my.domain.dto.vehicle.VehicleUpdateRequest;
import com.my.domain.entity.Vehicle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.my.domain.vo.VehicleVO;

/**
* @author Administrator
* @description 针对表【vehicle(车辆表)】的数据库操作Service
* @createDate 2025-03-12 16:44:26
*/
public interface VehicleService extends IService<Vehicle> {

    /**
     * 新增车辆
     *
     * @param vehicleAddRequest
     * @return
     */
    Long addVehicle(VehicleAddRequest vehicleAddRequest);

    /**
     * 更新车辆
     *
     * @param vehicleUpdateRequest
     * @return
     */
    boolean updateVehicle(VehicleUpdateRequest vehicleUpdateRequest);

    /**
     * 删除车辆
     *
     * @param deleteRequest
     * @return
     */
    boolean deleteVehicle(DeleteRequest deleteRequest);

    /**
     * 根据ID获取车辆
     *
     * @param id
     * @return
     */
    VehicleVO getVehicleById(Long id);

    /**
     * 获取车辆VO
     *
     * @param vehicle
     * @return
     */
    VehicleVO getVehicleVO(Vehicle vehicle);
}
