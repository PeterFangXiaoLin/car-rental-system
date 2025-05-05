package com.my.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.domain.dto.rentalorder.RentalOrderAdminPageRequest;
import com.my.domain.dto.rentalorder.RentalOrderPageRequest;
import com.my.domain.entity.RentalOrder;
import com.my.domain.vo.RentalOrderVO;
import org.apache.ibatis.annotations.Param;

/**
* @author Administrator
* @description 针对表【rental_order(租赁订单表)】的数据库操作Mapper
* @createDate 2025-04-08 09:38:16
* @Entity com.my.domain.entity.RentalOrder
*/
public interface RentalOrderMapper extends BaseMapper<RentalOrder> {

    /**
     * 分页查询我的订单
     *
     * @param page        分页参数
     * @param pageRequest 查询参数
     * @param userId      用户ID
     * @return 订单分页结果
     */
    Page<RentalOrderVO> pageMyRentalOrder(Page<RentalOrderVO> page, @Param("req")RentalOrderPageRequest pageRequest, @Param("userId") Long userId);

    /**
     * 根据订单ID查询订单详情
     * @param orderId 订单ID
     * @return 订单详情
     */
    RentalOrderVO getRentalOrderVO(@Param("orderId") Long orderId);

    /**
     * 分页查询订单
     *
     * @param page        分页参数
     * @param pageRequest 查询参数
     * @return 订单分页结果
     */
    Page<RentalOrderVO> pageRentalOrder(Page<RentalOrderVO> page,@Param("req") RentalOrderAdminPageRequest pageRequest);
}




