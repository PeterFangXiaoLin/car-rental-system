package com.my.controller;

import com.my.common.BaseResponse;
import com.my.domain.dto.verifyrecord.VerifyRecordAddRequest;
import com.my.service.VerifyRecordService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.my.common.ResultUtils.success;

@RestController
@RequestMapping("/verify/record")
public class VerifyRecordController {

    @Resource
    private VerifyRecordService verifyRecordService;

    @ApiOperation(value = "提交审核")
    @PostMapping("/add")
    public BaseResponse<Boolean> addVerifyRecord(@RequestBody VerifyRecordAddRequest verifyRecordAddRequest, HttpServletRequest request) {
        return success(verifyRecordService.addVerifyRecord(verifyRecordAddRequest, request));
    }
}
