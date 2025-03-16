package com.my.service;

import com.my.common.DeleteRequest;
import com.my.domain.dto.city.CityAddRequest;
import com.my.domain.dto.city.CityUpdateRequest;
import com.my.domain.entity.City;
import com.baomidou.mybatisplus.extension.service.IService;
import com.my.domain.vo.CityVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    /**
     * 添加城市
     * @param cityAddRequest
     * @return
     */
    Long addCity(CityAddRequest cityAddRequest);

    /**
     * 删除城市
     * @param deleteRequest
     * @return
     */
    boolean deleteCity(DeleteRequest deleteRequest);

    /**
     * 获取城市列表
     * @return
     */
    List<CityVO> listCity();

    /**
     * 获取城市VO
     * @param city
     * @return
     */
    CityVO getCityVO(City city);

    /**
     * 更新城市
     * @param cityUpdateRequest
     * @return
     */
    boolean updateCity(CityUpdateRequest cityUpdateRequest);
}
