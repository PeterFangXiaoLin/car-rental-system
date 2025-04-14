package com.my.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

    /**
     * 上传文件
     *
     * @param file
     * @return fileUrl
     */
    String uploadFile(MultipartFile file);
}
