package com.my.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.common.ErrorCode;
import com.my.domain.entity.City;
import com.my.domain.vo.CityVO;
import com.my.exception.BusinessException;
import com.my.service.CityService;
import com.my.mapper.CityMapper;
import com.my.utils.ExcelUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
* @author Administrator
* @description 针对表【city(城市表)】的数据库操作Service实现
* @createDate 2025-03-14 17:25:38
*/
@Service
public class CityServiceImpl extends ServiceImpl<CityMapper, City>
    implements CityService{

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

        List<CityVO> read = ExcelUtils.read(file, CityVO.class);
    }
}




