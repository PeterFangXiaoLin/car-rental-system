package com.my.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.common.DeleteRequest;
import com.my.domain.dto.vehiclebrand.VehicleBrandAddRequest;
import com.my.domain.dto.vehiclebrand.VehicleBrandQueryRequest;
import com.my.domain.dto.vehiclebrand.VehicleBrandUpdateRequest;
import com.my.domain.entity.VehicleBrand;
import com.baomidou.mybatisplus.extension.service.IService;
import com.my.domain.vo.VehicleBrandVO;

import java.util.List;

/**
* @author helloworld
* @description 针对表【vehicle_brand(车辆品牌表)】的数据库操作Service
* @createDate 2025-03-11 21:24:23
*/
public interface VehicleBrandService extends IService<VehicleBrand> {

    /**
     * 新增车辆品牌
     * @param vehicleBrandAddRequest
     * @return
     */
    Long addVehicleBrand(VehicleBrandAddRequest vehicleBrandAddRequest);

    /**
     * 更新车辆品牌
     * @param vehicleBrandUpdateRequest
     * @return
     *
     */
    boolean updateVehicleBrand(VehicleBrandUpdateRequest vehicleBrandUpdateRequest);

    /**
     * 删除车辆品牌
     * @param deleteRequest
     * @return
     */
    boolean deleteVehicleBrand(DeleteRequest deleteRequest);

    /**
     * 列表查询
     * @return
     */
    List<VehicleBrandVO> listVehicleBrand();

    /**
     * 封装品牌信息
     * @param vehicleBrand
     * @return
     */
    VehicleBrandVO getVehicleBrandVO(VehicleBrand vehicleBrand);

    /**
     * 分页查询
     * @param vehicleBrandQueryRequest
     * @return
     */
    Page<VehicleBrandVO> listVehicleBrandByPage(VehicleBrandQueryRequest vehicleBrandQueryRequest);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    VehicleBrandVO getVehicleBrandById(Long id);
}
