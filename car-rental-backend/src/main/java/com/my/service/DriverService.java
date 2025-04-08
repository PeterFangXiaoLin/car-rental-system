package com.my.service;

import com.my.common.DeleteRequest;
import com.my.domain.dto.driver.DriverAddRequest;
import com.my.domain.dto.driver.DriverUpdateRequest;
import com.my.domain.entity.Driver;
import com.baomidou.mybatisplus.extension.service.IService;
import com.my.domain.vo.DriverVO;

/**
* @author Administrator
* @description 针对表【driver(司机表)】的数据库操作Service
* @createDate 2025-04-08 10:50:10
*/
public interface DriverService extends IService<Driver> {

    /**
     * 增加司机
     * @param driverAddRequest
     * @return
     */
    Long addDriver(DriverAddRequest driverAddRequest);

    /**
     * 更新司机信息
     * @param driverUpdateRequest
     * @return
     */
    boolean updateDriver(DriverUpdateRequest driverUpdateRequest);

    /**
     * 删除司机
     * @param deleteRequest
     * @return
     */
    boolean deleteDriver(DeleteRequest deleteRequest);

    /**
     * 根据id获取司机信息
     * @param id
     * @return
     */
    DriverVO getDriverVOById(Long id);
}
