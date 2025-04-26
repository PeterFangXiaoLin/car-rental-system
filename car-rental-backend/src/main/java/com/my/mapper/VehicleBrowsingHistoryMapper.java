package com.my.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.domain.dto.vehiclebrowsinghistory.BrowsHistoryQueryRequest;
import com.my.domain.entity.VehicleBrowsingHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.domain.vo.VehicleBrowsingHistoryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Administrator
* @description 针对表【vehicle_browsing_history(车辆浏览历史表)】的数据库操作Mapper
* @createDate 2025-04-24 15:10:19
* @Entity com.my.domain.entity.VehicleBrowsingHistory
*/
public interface VehicleBrowsingHistoryMapper extends BaseMapper<VehicleBrowsingHistory> {

    /**
     * 分页查询浏览历史
     *
     * @param page
     * @param browsHistoryQueryRequest
     * @param userId
     * @return
     */
    Page<VehicleBrowsingHistoryVO> pageBrowseHistory(Page<VehicleBrowsingHistoryVO> page, @Param("req") BrowsHistoryQueryRequest browsHistoryQueryRequest, @Param("userId") Long userId);

    /**
     * 根据用户ID查询浏览历史
     *
     * @param userId
     * @return
     */
    List<VehicleBrowsingHistory> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询浏览的车辆ID
     *
     * @param userId
     * @return
     */
    List<Long> selectVehicleIdsByUserId(@Param("userId") Long userId);
}




