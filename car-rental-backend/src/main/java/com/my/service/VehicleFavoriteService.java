package com.my.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.my.domain.dto.vehiclefavorite.VehicleFavoriteAddRequest;
import com.my.domain.dto.vehiclefavorite.VehicleFavoriteCancelRequest;
import com.my.domain.dto.vehiclefavorite.VehicleFavoriteQueryRequest;
import com.my.domain.entity.VehicleFavorite;
import com.my.domain.vo.VehicleFavoriteVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author helloworld
* @description 针对表【vehicle_favorite(车辆收藏表)】的数据库操作Service
* @createDate 2025-04-25 21:12:09
*/
public interface VehicleFavoriteService extends IService<VehicleFavorite> {

    /**
     * 新增车辆收藏
     *
     * @param vehicleFavoriteAddRequest
     * @param request
     * @return
     */
    Long addVehicleFavorite(VehicleFavoriteAddRequest vehicleFavoriteAddRequest, HttpServletRequest request);

    /**
     * 取消收藏
     *
     * @param vehicleFavoriteCancelRequest
     * @param request
     * @return
     */
    Boolean cancelVehicleFavorite(VehicleFavoriteCancelRequest vehicleFavoriteCancelRequest, HttpServletRequest request);

    /**
     * 分页查询收藏列表
     *
     * @param vehicleFavoriteQueryRequest
     * @param request
     * @return
     */
    Page<VehicleFavoriteVO> pageVehicleFavorite(VehicleFavoriteQueryRequest vehicleFavoriteQueryRequest, HttpServletRequest request);

    /**
     * 检查车辆是否收藏
     *
     * @param vehicleId
     * @param request
     * @return
     */
    Boolean checkVehicleFavorite(Long vehicleId, HttpServletRequest request);
}
