package com.my.service;

import com.my.domain.entity.City;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
* @author Administrator
* @description 针对表【city(城市表)】的数据库操作Service
* @createDate 2025-03-14 17:25:38
*/
public interface CityService extends IService<City> {

    /**
     * 导入城市数据
     * @param file
     * @return
     */
    boolean importCityData(MultipartFile file) throws IOException;
}
