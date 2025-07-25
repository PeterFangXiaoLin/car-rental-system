package com.my.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.my.common.DeleteRequest;
import com.my.domain.dto.vehicle.VehicleAddRequest;
import com.my.domain.dto.vehicle.VehicleQueryRequest;
import com.my.domain.dto.vehicle.VehicleUpdateRequest;
import com.my.domain.entity.Vehicle;
import com.my.domain.vo.VehicleVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    /**
     * 分页获取车辆列表
     *
     * @param vehicleQueryRequest
     * @return
     */
    Page<VehicleVO> listVehicleByPage(VehicleQueryRequest vehicleQueryRequest);

    /**
     * 获取查询条件
     *
     * @param vehicleQueryRequest
     * @return
     */
    QueryWrapper<Vehicle> getQueryWrapper(VehicleQueryRequest vehicleQueryRequest);

    /**
     * 推荐车辆
     *
     * @param vehicleId
     * @param request
     * @return
     */
    List<VehicleVO> recommendVehicle(Long vehicleId, HttpServletRequest request);
}
