package com.my.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.common.DeleteRequest;
import com.my.domain.dto.vehiclebrowsinghistory.BrowsHistoryAddRequest;
import com.my.domain.dto.vehiclebrowsinghistory.BrowsHistoryQueryRequest;
import com.my.domain.entity.VehicleBrowsingHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.my.domain.vo.VehicleBrowsingHistoryVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author Administrator
* @description 针对表【vehicle_browsing_history(车辆浏览历史表)】的数据库操作Service
* @createDate 2025-04-24 15:10:19
*/
public interface VehicleBrowsingHistoryService extends IService<VehicleBrowsingHistory> {

    /**
     * 添加浏览历史
     *
     * @param browsHistoryAddRequest
     * @param request
     * @return
     */
    Long addBrowsHistory(BrowsHistoryAddRequest browsHistoryAddRequest, HttpServletRequest request);

    /**
     * 删除单个浏览历史
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    Boolean deleteBrowsHistory(DeleteRequest deleteRequest, HttpServletRequest request);

    /**
     * 分页查询浏览历史
     *
     * @param browsHistoryQueryRequest
     * @param request
     * @return
     */
    Page<VehicleBrowsingHistoryVO> pageBrowseHistory(BrowsHistoryQueryRequest browsHistoryQueryRequest, HttpServletRequest request);

    /**
     * 清空浏览历史
     *
     * @param request
     * @return
     */
    Boolean clearBrowsHistory(HttpServletRequest request);
}
