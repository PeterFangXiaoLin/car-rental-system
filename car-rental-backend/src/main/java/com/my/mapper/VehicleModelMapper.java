package com.my.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.domain.dto.vehiclemodel.VehicleModelQueryRequest;
import com.my.domain.entity.VehicleModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.domain.vo.VehicleModelVO;
import org.apache.ibatis.annotations.Param;

/**
* @author Administrator
* @description 针对表【vehicle_model(车辆型号表)】的数据库操作Mapper
* @createDate 2025-03-12 09:50:47
* @Entity com.my.domain.entity.VehicleModel
*/
public interface VehicleModelMapper extends BaseMapper<VehicleModel> {

    /**
     * 分页获取车辆型号列表
     *
     * @param page
     * @param vehicleModelQueryRequest
     * @return
     */
    Page<VehicleModelVO> selectPageVO(Page<VehicleModelVO> page, @Param("req") VehicleModelQueryRequest vehicleModelQueryRequest);
}




