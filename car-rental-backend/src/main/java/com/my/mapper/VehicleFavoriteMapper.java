package com.my.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.domain.entity.VehicleFavorite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.domain.vo.VehicleFavoriteVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author helloworld
* @description 针对表【vehicle_favorite(车辆收藏表)】的数据库操作Mapper
* @createDate 2025-04-25 21:12:09
* @Entity com.my.domain.entity.VehicleFavorite
*/
public interface VehicleFavoriteMapper extends BaseMapper<VehicleFavorite> {

    Page<VehicleFavoriteVO> pageVehicleFavorite(Page<VehicleFavoriteVO> page, @Param("userId") Long userId, @Param("searchText") String searchText);

    /**
     * 根据用户ID查询收藏的车辆
     *
     * @param userId 用户ID
     * @return 收藏的车辆列表
     */
    List<VehicleFavorite> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询收藏的车辆ID
     *
     * @param userId 用户ID
     * @return 收藏的车辆ID列表
     */
    List<Long> selectVehicleIdsByUserId(@Param("userId") Long userId);
}




