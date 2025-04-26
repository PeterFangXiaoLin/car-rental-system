package com.my.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.domain.dto.vehicle.VehicleQueryRequest;
import com.my.domain.entity.Vehicle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.domain.vo.VehicleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Administrator
* @description 针对表【vehicle(车辆表)】的数据库操作Mapper
* @createDate 2025-03-12 16:44:26
* @Entity com.my.domain.entity.Vehicle
*/
public interface VehicleMapper extends BaseMapper<Vehicle> {

    /**
     * 分页查询
     *
     * @param page
     * @param vehicleQueryRequest
     * @return
     */
    Page<VehicleVO> selectPageVO(Page<VehicleVO> page, @Param("req") VehicleQueryRequest vehicleQueryRequest);

    /**
     * 热门车辆
     *
     * @param count
     * @return
     */
    List<VehicleVO> selectHotVehicles(@Param("count") int count);

    /**
     * 随机车辆
     *
     * @param count
     * @return
     */
    List<VehicleVO> selectRandomVehicles(@Param("count") int count);

    /**
     * 热门车辆ID
     *
     * @param limit
     * @return
     */
    List<Long> selectHotVehicleIds(@Param("limit") int limit);
}




