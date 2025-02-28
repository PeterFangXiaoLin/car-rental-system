package com.my.manager;

import com.my.common.ErrorCode;
import com.my.config.CosClientConfig;
import com.my.exception.BusinessException;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;

@Component
@Slf4j
public class CosManager {
    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private COSClient cosClient;

    @Resource
    private TransferManager transferManager;

    /**
     * 高级接口上传文件
     *
     * @param key
     * @param file
     * @return url
     */
    public String putObjectByFile(String key, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key, file);
        try {
            Upload upload = transferManager.upload(putObjectRequest);
            upload.waitForCompletion();
        } catch (InterruptedException e) {
            log.error("上传失败 = {}", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        }
        return cosClientConfig.getHost() + key;
    }

    /**
     * 高级接口流式上传
     *
     * @param key
     * @param inputStream
     * @param objectMetadata
     * @return
     */
    public String putObjectByStream(String key, InputStream inputStream, ObjectMetadata objectMetadata) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key, inputStream, objectMetadata);
        try {
            Upload upload = transferManager.upload(putObjectRequest);
            upload.waitForCompletion();
        } catch (InterruptedException e) {
            log.error("上传失败 = {}", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        }
        return cosClientConfig.getHost() + key;
    }
}
