package com.my.service;

import com.my.domain.entity.VerifyRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author Administrator
* @description 针对表【verify_record(认证记录表)】的数据库操作Service
* @createDate 2025-03-10 10:12:25
*/
public interface VerifyRecordService extends IService<VerifyRecord> {

    /**
     * 提交认证
     *
     * @param verifyRecord
     * @param request
     * @return
     */
    boolean addVerifyRecord(VerifyRecord verifyRecord, HttpServletRequest request);
}
