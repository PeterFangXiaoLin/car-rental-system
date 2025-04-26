package com.my.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
     * 分页查询订单
     * @param page 分页参数
     * @param searchText 搜索文本
     * @param userId 用户ID
     * @return 订单分页结果
     */
    Page<RentalOrderVO> pageRentalOrder(Page<RentalOrderVO> page, @Param("searchText") String searchText, @Param("userId") Long userId);
}




