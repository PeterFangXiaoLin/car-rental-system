package com.my.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.my.common.DeleteRequest;
import com.my.domain.dto.store.StoreCreateRequest;
import com.my.domain.dto.store.StoreQueryRequest;
import com.my.domain.dto.store.StoreUpdateRequest;
import com.my.domain.entity.Store;
import com.my.domain.vo.StoreVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author helloworld
* @description 针对表【store(门店表)】的数据库操作Service
* @createDate 2025-03-22 16:40:56
*/
public interface StoreService extends IService<Store> {

    /**
     * 创建门店
     *
     * @param storeCreateRequest
     * @return
     */
    Long createStore(StoreCreateRequest storeCreateRequest);

    /**
     * 更新门店
     *
     * @param storeUpdateRequest
     * @return
     */
    boolean updateStore(StoreUpdateRequest storeUpdateRequest);

    /**
     * 删除门店
     *
     * @param deleteRequest
     * @return
     */
    boolean deleteStore(DeleteRequest deleteRequest);

    /**
     * 根据id获取门店
     *
     * @param id
     * @return
     */
    StoreVO getStore(Long id);

    /**
     * 获取VO
     *
     * @param store
     * @return
     */
    StoreVO getStoreVO(Store store);

    /**
     * 分页获取门店列表
     *
     * @param storeQueryRequest
     * @return
     */
    Page<StoreVO> getStoreVOByPage(StoreQueryRequest storeQueryRequest);

    /**
     * 获取查询条件 QueryWrapper
     *
     * @param storeQueryRequest
     * @return
     */
    QueryWrapper<Store> getQueryWrapper(StoreQueryRequest storeQueryRequest);

    /**
     * 上传门店图片
     *
     * @param files
     * @param request
     * @return
     */
    List<String> uploadStoreImage(MultipartFile[] files, HttpServletRequest request);

    /**
     * 根据城市名获取门店列表
     *
     * @param cityName
     * @return
     */
    List<StoreVO> getStoreVOByCityName(String cityName);
}
