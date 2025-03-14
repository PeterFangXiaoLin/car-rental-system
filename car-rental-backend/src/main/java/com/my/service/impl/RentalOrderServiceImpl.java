package com.my.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.domain.entity.RentalOrder;
import com.my.service.RentalOrderService;
import com.my.mapper.RentalOrderMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【rental_order(租赁订单表)】的数据库操作Service实现
* @createDate 2025-03-14 16:53:25
*/
@Service
public class RentalOrderServiceImpl extends ServiceImpl<RentalOrderMapper, RentalOrder>
    implements RentalOrderService{

}




