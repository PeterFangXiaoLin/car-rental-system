package com.my.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.common.ErrorCode;
import com.my.domain.entity.VerifyRecord;
import com.my.exception.BusinessException;
import com.my.service.VerifyRecordService;
import com.my.mapper.VerifyRecordMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
* @author Administrator
* @description 针对表【verify_record(认证记录表)】的数据库操作Service实现
* @createDate 2025-03-10 10:12:25
*/
@Service
public class VerifyRecordServiceImpl extends ServiceImpl<VerifyRecordMapper, VerifyRecord>
    implements VerifyRecordService{

    @Override
    public boolean addVerifyRecord(VerifyRecord verifyRecord, HttpServletRequest request) {
        // 1. 校验参数是否合法
        if (verifyRecord == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2. 插入数据

    }
}




