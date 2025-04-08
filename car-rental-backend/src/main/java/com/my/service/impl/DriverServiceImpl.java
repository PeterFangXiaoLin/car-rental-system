package com.my.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.common.DeleteRequest;
import com.my.common.ErrorCode;
import com.my.common.PageRequest;
import com.my.constant.UserConstant;
import com.my.domain.dto.driver.DriverAddRequest;
import com.my.domain.dto.driver.DriverUpdateRequest;
import com.my.domain.entity.Driver;
import com.my.domain.vo.DriverVO;
import com.my.exception.BusinessException;
import com.my.exception.ThrowUtils;
import com.my.service.DriverService;
import com.my.mapper.DriverMapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import static com.my.constant.RedisConstant.DRIVER_ADD_PREFIX;

/**
* @author Administrator
* @description 针对表【driver(司机表)】的数据库操作Service实现
* @createDate 2025-04-08 10:50:10
*/
@Service
@Slf4j
public class DriverServiceImpl extends ServiceImpl<DriverMapper, Driver>
    implements DriverService{

    @Resource
    private DriverMapper driverMapper;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public Long addDriver(DriverAddRequest driverAddRequest) {
        ThrowUtils.throwIf(driverAddRequest == null, ErrorCode.PARAMS_ERROR);
        Driver driver = BeanUtil.toBean(driverAddRequest, Driver.class);

        // 校验
        validateDriver(driver, true);

        // 插入
        boolean save = this.save(driver);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "添加失败");
        }
        return driver.getId();

//        // 插入
//        String lockKey = DRIVER_ADD_PREFIX + driver.getDriverAccount();
//        RLock lock = redissonClient.getLock(lockKey);
//        try {
//            // 尝试获取锁，最多等待 5 秒，上锁后 30 秒自动解锁
//            boolean locked = lock.tryLock(5, 30, TimeUnit.SECONDS);
//
//            if (!locked) {
//                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "添加失败，系统繁忙");
//            }
//
//            // 校验账号是否重复
//            long count = this.lambdaQuery()
//                    .eq(Driver::getDriverAccount, driver.getDriverAccount())
//                    .count();
//            if (count > 0) {
//                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
//            }
//
//            // 插入
//            boolean saveResult = this.save(driver);
//            if (!saveResult) {
//                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "添加失败");
//            }
//            return driver.getId();
//        } catch (InterruptedException e) {
//            log.error("获取锁失败", e);
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "获取锁失败");
//        } finally {
//            // 释放锁
//            if (lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
    }

    @Override
    public boolean updateDriver(DriverUpdateRequest driverUpdateRequest) {
        ThrowUtils.throwIf(driverUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        Driver driver = BeanUtil.toBean(driverUpdateRequest, Driver.class);
        // 校验
        validateDriver(driver, false);

        // 校验存在
        Long id = driver.getId();
        Driver oldDriver = this.getById(id);
        ThrowUtils.throwIf(oldDriver == null, ErrorCode.NOT_FOUND_ERROR, "司机不存在");
        // 插入
        boolean result = this.updateById(driver);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "修改失败");
        }
        return true;
    }

    @Override
    public boolean deleteDriver(DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null, ErrorCode.PARAMS_ERROR);
        Long id = deleteRequest.getId();
        Driver driver = this.getById(id);
        ThrowUtils.throwIf(driver == null, ErrorCode.NOT_FOUND_ERROR, "司机不存在");
        // 删除
        boolean result = this.removeById(id);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return true;
    }

    @Override
    public DriverVO getDriverVOById(Long id) {
        Driver driver = this.getById(id);
        if (driver == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "司机不存在");
        }
        return BeanUtil.toBean(driver, DriverVO.class);
    }

    private void validateDriver(Driver driver, boolean add) {
        if (driver == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String driverName = driver.getDriverName();
        if (StrUtil.isBlank(driverName) || driverName.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "司机姓名不合法");
        }
    }
}




