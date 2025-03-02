package com.my.manager;

import com.my.config.CosClientConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;

@Component
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
    public String putObjectByFile(String key, File file) throws InterruptedException {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key, file);
        Upload upload = transferManager.upload(putObjectRequest);
        upload.waitForCompletion();
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
    public String putObjectByStream(String key, InputStream inputStream, ObjectMetadata objectMetadata) throws InterruptedException {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key, inputStream, objectMetadata);
        Upload upload = transferManager.upload(putObjectRequest);
        upload.waitForCompletion();
        return cosClientConfig.getHost() + key;
    }
}
