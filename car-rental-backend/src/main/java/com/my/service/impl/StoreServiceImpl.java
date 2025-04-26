package com.my.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.common.DeleteRequest;
import com.my.common.ErrorCode;
import com.my.constant.CommonConstant;
import com.my.domain.dto.store.StoreCreateRequest;
import com.my.domain.dto.store.StoreQueryRequest;
import com.my.domain.dto.store.StoreUpdateRequest;
import com.my.domain.entity.Store;
import com.my.domain.enums.StoreStatusEnum;
import com.my.domain.vo.StoreVO;
import com.my.exception.BusinessException;
import com.my.exception.ThrowUtils;
import com.my.manager.CosManager;
import com.my.mapper.StoreMapper;
import com.my.service.StoreService;
import com.qcloud.cos.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.my.constant.FileConstant.ALLOWED_FILE_SUFFIXES;

/**
* @author helloworld
* @description 针对表【store(门店表)】的数据库操作Service实现
* @createDate 2025-03-22 16:40:56
*/
@Service
@Slf4j
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store>
    implements StoreService{

    @Resource
    private StoreMapper storeMapper;

    @Resource
    private CosManager cosManager;

    @Override
    public Long createStore(StoreCreateRequest storeCreateRequest) {
        if (storeCreateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Store store = BeanUtil.toBean(storeCreateRequest, Store.class);
        validateStore(store, true);

        boolean save = this.save(store);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建门店失败");
        }
        return store.getId();
    }

    @Override
    public boolean updateStore(StoreUpdateRequest storeUpdateRequest) {
        if (storeUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Store store = BeanUtil.toBean(storeUpdateRequest, Store.class);
        validateStore(store, false);
        boolean update = this.updateById(store);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新门店失败");
        }
        return true;
    }

    @Override
    public boolean deleteStore(DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = deleteRequest.getId();
        Store oldStore = getById(id);
        if (oldStore == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "门店不存在");
        }
        boolean remove = this.removeById(id);
        if (!remove) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除门店失败");
        }
        return true;
    }

    @Override
    public StoreVO getStore(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Store store = this.getById(id);
        if (store == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "门店不存在");
        }
        return getStoreVO(store);
    }

    @Override
    public StoreVO getStoreVO(Store store) {
        if (store == null) {
            return null;
        }
        return BeanUtil.toBean(store, StoreVO.class);
    }

    @Override
    public Page<StoreVO> getStoreVOByPage(StoreQueryRequest storeQueryRequest) {
        if (storeQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long pageSize = storeQueryRequest.getPageSize();
        long current = storeQueryRequest.getCurrent();
        if (pageSize < 0 || current < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<Store> queryWrapper = getQueryWrapper(storeQueryRequest);
        Page<Store> storePage = storeMapper.selectPage(new Page<>(current, pageSize), queryWrapper);
        List<StoreVO> storeVOList = storePage.getRecords().stream().map(this::getStoreVO).collect(Collectors.toList());
        Page<StoreVO> storeVOPage = new Page<>(current, pageSize, storePage.getTotal());
        storeVOPage.setRecords(storeVOList);
        return storeVOPage;
    }

    @Override
    public QueryWrapper<Store> getQueryWrapper(StoreQueryRequest storeQueryRequest) {
        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();

        if (storeQueryRequest == null) {
            return queryWrapper;
        }

        Long id = storeQueryRequest.getId();
        String storeName = storeQueryRequest.getStoreName();
        String address = storeQueryRequest.getAddress();
        String contactPhone = storeQueryRequest.getContactPhone();
        LocalTime openTime = storeQueryRequest.getOpenTime();
        LocalTime closeTime = storeQueryRequest.getCloseTime();
        Integer status = storeQueryRequest.getStatus();
        String sortField = storeQueryRequest.getSortField();
        String sortOrder = storeQueryRequest.getSortOrder();

        queryWrapper.eq(id != null, "id", id);
        queryWrapper.like(StrUtil.isNotBlank(storeName), "storeName", storeName);
        queryWrapper.like(StrUtil.isNotBlank(address), "address", address);
        queryWrapper.like(StrUtil.isNotBlank(contactPhone), "contactPhone", contactPhone);
        queryWrapper.gt(openTime != null, "openTime", openTime);
        queryWrapper.lt(closeTime!= null, "closeTime", closeTime);
        queryWrapper.eq(status!= null, "status", status);
        queryWrapper.orderBy(StrUtil.isNotBlank(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);

        return queryWrapper;
    }

    @Override
    public List<String> uploadStoreImage(MultipartFile[] files, HttpServletRequest request) {
        if (files == null || files.length == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "上传图片为空");
        }
        // 校验文件大小，每个文件不超过1MB
        for (MultipartFile file : files) {
            if (file.getSize() > 1024 * 1024) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "图片不超过1MB");
            }
            // 校验文件类型
            String originalFilename = file.getOriginalFilename();
            String fileSuffix = FileUtil.getSuffix(originalFilename);
            if (!ALLOWED_FILE_SUFFIXES.contains(fileSuffix)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件格式不支持");
            }
        }

        // 上传文件
        List<String> urlList = new ArrayList<>();
        for (MultipartFile file : files) {
            String filePath = "store/%s.%s";
            String originalFilename = file.getOriginalFilename();
            String fileSuffix = FileUtil.getSuffix(originalFilename);
            String fileName = String.format(filePath, UUID.randomUUID(), fileSuffix);
            long fileSize = file.getSize();

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(fileSize);

            try {
                String url = cosManager.putObjectByStream(fileName, file.getInputStream(), objectMetadata);
                urlList.add(url);
            } catch (Exception e) {
                log.info("文件上传失败", e);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传失败");
            }
        }

        return urlList;
    }

    @Override
    public List<StoreVO> getStoreVOByCityName(String cityName) {
        if (StrUtil.isBlank(cityName)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "城市名称为空");
        }
        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("cityName", cityName);
        List<Store> storeList = storeMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(storeList)) {
            return new ArrayList<>();
        }
        return storeList.stream().map(this::getStoreVO).collect(Collectors.toList());
    }

    private void validateStore(Store store, boolean isAdd) {
        if (store == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if (!isAdd) {
            Long id = store.getId();
            if (id == null || id <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "门店id为空");
            }
            Store oldStore = getById(id);
            if (oldStore == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "门店不存在");
            }
        }

        String storeName = store.getStoreName();
        String address = store.getAddress();
        Integer status = store.getStatus();

        if (StrUtil.isBlank(storeName)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "门店名称为空");
        }

        if (status != null) {
            StoreStatusEnum storeStatusEnum = StoreStatusEnum.getEnumByValue(status);
            ThrowUtils.throwIf(storeStatusEnum == null, ErrorCode.PARAMS_ERROR, "门店状态不合法");
        }

        if (StrUtil.isBlank(address)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "门店地址为空");
        }
    }


}




