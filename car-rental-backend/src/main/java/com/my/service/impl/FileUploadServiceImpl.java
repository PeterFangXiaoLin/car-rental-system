package com.my.service.impl;

import cn.hutool.core.io.FileUtil;
import com.my.common.ErrorCode;
import com.my.exception.BusinessException;
import com.my.manager.CosManager;
import com.my.service.FileUploadService;
import com.qcloud.cos.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.UUID;

import static com.my.constant.FileConstant.ALLOWED_FILE_SUFFIXES;
import static com.my.constant.FileConstant.ONE_MB;

@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    @Resource
    private CosManager cosManager;

    @Override
    public String uploadFile(MultipartFile file) {
        // 1. 校验参数
        if (file == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 校验文件大小
        long fileSize = file.getSize();
        if (fileSize > 5 * ONE_MB) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过5MB");
        }

        // 校验文件类型
        String originalFilename = file.getOriginalFilename();
        String fileSuffix = FileUtil.getSuffix(originalFilename);
        if (!ALLOWED_FILE_SUFFIXES.contains(fileSuffix)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件格式不支持");
        }

        // 2. 上传文件
        try {
            // 上传文件到 OSS，得到访问地址（这里假设你使用的是阿里云 OSS）
            String filePath = String.format("file/%s.%s", UUID.randomUUID(), fileSuffix);

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(fileSize);

            return cosManager.putObjectByStream(filePath, file.getInputStream(), objectMetadata);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传失败");
        }
    }
}
