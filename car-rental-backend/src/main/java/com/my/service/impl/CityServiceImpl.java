package com.my.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.common.DeleteRequest;
import com.my.common.ErrorCode;
import com.my.domain.dto.city.CityAddRequest;
import com.my.domain.dto.city.CityUpdateRequest;
import com.my.domain.entity.City;
import com.my.domain.vo.CityVO;
import com.my.exception.BusinessException;
import com.my.mapper.CityMapper;
import com.my.service.CityService;
import com.my.utils.ExcelUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author Administrator
* @description 针对表【city(城市表)】的数据库操作Service实现
* @createDate 2025-03-14 17:25:38
*/
@Service
public class CityServiceImpl extends ServiceImpl<CityMapper, City>
    implements CityService{

    @Resource
    private CityMapper cityMapper;

    @Override
    public boolean importCityData(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件不存在");
        }
        String originalFilename = file.getOriginalFilename();
        String suffix = FileUtil.getSuffix(originalFilename);
        if (!"xlsx".equals(suffix) || !"xls".equals(suffix)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "只支持Excel文件");
        }

        List<City> cityList = ExcelUtils.read(file, City.class);
        boolean saveBatch = this.saveBatch(cityList);
        if (!saveBatch) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "导入失败");
        }
        return true;
    }

    @Override
    public Long addCity(CityAddRequest cityAddRequest) {
        if (cityAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        City city = BeanUtil.toBean(cityAddRequest, City.class);
        validateCity(city, true);
        boolean save = this.save(city);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "添加失败");
        }
        return city.getId();
    }

    @Override
    public boolean deleteCity(DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = deleteRequest.getId();
        // 判断是否存在
        City oldCity = getById(id);
        if (oldCity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        boolean remove = this.removeById(id);
        if (!remove) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return true;
    }

    @Override
    public List<CityVO> listCity() {
        List<City> cityList = cityMapper.selectList(null);
        return cityList.stream().map(this::getCityVO).collect(Collectors.toList());
    }

    @Override
    public CityVO getCityVO(City city) {
        return BeanUtil.toBean(city, CityVO.class);
    }

    @Override
    public boolean updateCity(CityUpdateRequest cityUpdateRequest) {
        if (cityUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        City city = BeanUtil.toBean(cityUpdateRequest, City.class);
        validateCity(city, false);
        boolean update = this.updateById(city);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "修改失败");
        }
        return true;
    }

    private void validateCity(City city, boolean add) {
        if (city == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if (!add) {
            Long id = city.getId();
            if (id == null || id <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            City oldCity = this.getById(id);
            if (oldCity == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "修改的城市不存在");
            }
        }

        String cityName = city.getCityName();
        String provinceName = city.getProvinceName();
        String adcode = city.getAdcode();
        String citycode = city.getCitycode();
        BigDecimal longitude = city.getLongitude();
        BigDecimal latitude = city.getLatitude();

        if (StrUtil.hasBlank(cityName, provinceName, adcode, citycode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if (ObjUtil.hasNull(longitude, latitude)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

    }
}




