package com.my.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.common.DeleteRequest;
import com.my.common.ErrorCode;
import com.my.constant.CommonConstant;
import com.my.domain.dto.vehicle.VehicleAddRequest;
import com.my.domain.dto.vehicle.VehicleQueryRequest;
import com.my.domain.dto.vehicle.VehicleUpdateRequest;
import com.my.domain.entity.*;
import com.my.domain.enums.VehicleStatusEnum;
import com.my.domain.vo.LoginUserVO;
import com.my.domain.vo.VehicleVO;
import com.my.exception.BusinessException;
import com.my.mapper.RentalOrderMapper;
import com.my.mapper.VehicleBrowsingHistoryMapper;
import com.my.mapper.VehicleFavoriteMapper;
import com.my.mapper.VehicleMapper;
import com.my.service.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.mahout.cf.taste.common.NoSuchUserException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author Administrator
* @description 针对表【vehicle(车辆表)】的数据库操作Service实现
* @createDate 2025-03-12 16:44:26
*/
@Service
@Slf4j
public class VehicleServiceImpl extends ServiceImpl<VehicleMapper, Vehicle>
    implements VehicleService{

    @Resource
    private VehicleMapper vehicleMapper;

    @Resource
    private VehicleBrowsingHistoryMapper browsingHistoryMapper;

    @Resource
    private VehicleFavoriteMapper favoriteMapper;

    @Resource
    private UserService userService;

    @Resource
    private VehicleBrandService vehicleBrandService;

    @Resource
    private VehicleModelService vehicleModelService;

    @Resource
    private VehicleTypeDictService vehicleTypeDictService;

    @Resource
    private EnergyTypeDictService energyTypeDictService;

    @Resource
    private RentalOrderMapper rentalOrderMapper;

    // 推荐数量
    private static final int RECOMMEND_LIMIT = 8;

    // 相似度模型缓存
    private ItemSimilarity itemSimilarity;
    private DataModel dataModel;

    // 缓存上次更新时间
    private long lastModelUpdateTime = 0;

    // 模型更新间隔（12小时）
    private static final long MODEL_UPDATE_INTERVAL = 12 * 60 * 60 * 1000L;

    private void validate(Vehicle vehicle, boolean add) {
        if (vehicle == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long id = vehicle.getId();
        String vehicleNo = vehicle.getVehicleNo();
        Long brandId = vehicle.getBrandId();
        Long modelId = vehicle.getModelId();
        Long vehicleTypeId = vehicle.getVehicleTypeId();
        BigDecimal dailyPrice = vehicle.getDailyPrice();
        Integer status = vehicle.getStatus();

        if (!add) {
            if (id == null || id <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "车辆ID不能为空");
            }
            Vehicle oldVehicle = vehicleMapper.selectById(id);
            if (oldVehicle == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "车辆不存在");
            }
        }

        if (StrUtil.isBlank(vehicleNo)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "车牌号不能为空");
        }
        if (brandId == null || brandId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "品牌不存在");
        }
        boolean brandExists = vehicleBrandService.lambdaQuery().eq(VehicleBrand::getId, brandId).exists();
        if (!brandExists) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "品牌不存在");
        }

        if (modelId == null || modelId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "型号不存在");
        }
        boolean modelExists = vehicleModelService.lambdaQuery()
                .eq(VehicleModel::getId, modelId)
                .eq(VehicleModel::getBrandId, brandId)
                .exists();
        if (!modelExists) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "型号不存在");
        }
        if (vehicleTypeId == null || vehicleTypeId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "车辆类型不存在");
        }
        boolean vehicleTypeExists = vehicleTypeDictService.lambdaQuery().eq(VehicleTypeDict::getId, vehicleTypeId).exists();
        if (!vehicleTypeExists) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "车辆类型不存在");
        }

        if (dailyPrice == null || dailyPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "日租金不能为空");
        }
        if (status == null || VehicleStatusEnum.getEnumByValue(status) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "状态非法");
        }
    }

    @Override
    public Long addVehicle(VehicleAddRequest vehicleAddRequest) {
        if (vehicleAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 校验参数
        Vehicle vehicle = BeanUtil.toBean(vehicleAddRequest, Vehicle.class);
        validate(vehicle, true);

        // 插入数据
        boolean saveResult = this.save(vehicle);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "新增车辆失败");
        }
        return vehicle.getId();
    }

    @Override
    public boolean updateVehicle(VehicleUpdateRequest vehicleUpdateRequest) {
        if (vehicleUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 校验参数
        Vehicle vehicle = BeanUtil.toBean(vehicleUpdateRequest, Vehicle.class);
        validate(vehicle, false);

        // 更新数据
        boolean updateResult = this.updateById(vehicle);
        if (!updateResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新车辆失败");
        }
        return true;
    }

    @Override
    public boolean deleteVehicle(DeleteRequest deleteRequest) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = deleteRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "车辆ID不能为空");
        }
        // 校验存在
        Vehicle oldVehicle = vehicleMapper.selectById(id);
        if (oldVehicle == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "车辆不存在");
        }
        // 删除数据
        boolean deleteResult = this.removeById(id);
        if (!deleteResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除车辆失败");
        }
        return true;
    }

    @Override
    public VehicleVO getVehicleById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Vehicle vehicle = vehicleMapper.selectById(id);
        if (vehicle == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "车辆不存在");
        }
        return this.getVehicleVO(vehicle);
    }

    @Override
    public VehicleVO getVehicleVO(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        VehicleVO vehicleVO = BeanUtil.toBean(vehicle, VehicleVO.class);
        VehicleBrand vehicleBrand = vehicleBrandService.getById(vehicle.getBrandId());
        VehicleModel vehicleModel = vehicleModelService.getById(vehicle.getModelId());
        VehicleTypeDict vehicleTypeDict = vehicleTypeDictService.getById(vehicle.getVehicleTypeId());
        EnergyTypeDict energyTypeDict = energyTypeDictService.getById(vehicle.getEnergyTypeId());
        vehicleVO.setBrandName(Optional.ofNullable(vehicleBrand.getBrandName()).orElse(StrUtil.EMPTY));
        vehicleVO.setModelName(Optional.ofNullable(vehicleModel.getModelName()).orElse(StrUtil.EMPTY));
        vehicleVO.setVehicleTypeName(Optional.ofNullable(vehicleTypeDict.getTypeName()).orElse(StrUtil.EMPTY));
        vehicleVO.setEnergyTypeName(Optional.ofNullable(energyTypeDict.getTypeName()).orElse(StrUtil.EMPTY));
        return vehicleVO;
    }

    @Override
    public Page<VehicleVO> listVehicleByPage(VehicleQueryRequest vehicleQueryRequest) {
        if (vehicleQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long current = vehicleQueryRequest.getCurrent();
        long pageSize = vehicleQueryRequest.getPageSize();
        if (current <= 0 || pageSize <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分页参数不合法");
        }

        Page<VehicleVO> page = new Page<>(current, pageSize);
        return vehicleMapper.selectPageVO(page, vehicleQueryRequest);
    }

    @Override
    public QueryWrapper<Vehicle> getQueryWrapper(VehicleQueryRequest vehicleQueryRequest) {
        QueryWrapper<Vehicle> queryWrapper = new QueryWrapper<>();
        if (vehicleQueryRequest == null) {
            return queryWrapper;
        }

        Long id = vehicleQueryRequest.getId();
        String name = vehicleQueryRequest.getName();
        String vehicleNo = vehicleQueryRequest.getVehicleNo();
        Long brandId = vehicleQueryRequest.getBrandId();
        Long modelId = vehicleQueryRequest.getModelId();
        Long vehicleTypeId = vehicleQueryRequest.getVehicleTypeId();
        BigDecimal minDailyPrice = vehicleQueryRequest.getMinDailyPrice();
        BigDecimal maxDailyPrice = vehicleQueryRequest.getMaxDailyPrice();
        Integer status = vehicleQueryRequest.getStatus();
        String description = vehicleQueryRequest.getDescription();
        String sortField = vehicleQueryRequest.getSortField();
        String sortOrder = vehicleQueryRequest.getSortOrder();
        String searchText = vehicleQueryRequest.getSearchText();
        if (StrUtil.isNotBlank(searchText)) {
            queryWrapper.and(qw -> qw.like("name", searchText).or().like("description", searchText));
        }
        Long energyTypeId = vehicleQueryRequest.getEnergyTypeId();
        Integer seatCount = vehicleQueryRequest.getSeatCount();

        queryWrapper.eq(id != null, "id", id);
        queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        queryWrapper.like(StrUtil.isNotBlank(vehicleNo), "vehicleNo", vehicleNo);
        queryWrapper.eq(brandId != null, "brandId", brandId);
        queryWrapper.eq(modelId != null, "modelId", modelId);
        queryWrapper.eq(vehicleTypeId != null, "vehicleTypeId", vehicleTypeId);
        queryWrapper.eq(energyTypeId != null, "energyTypeId", energyTypeId);
        queryWrapper.eq(seatCount!= null, "seatCount", seatCount);
        queryWrapper.ge(minDailyPrice!= null, "dailyPrice", minDailyPrice);
        queryWrapper.le(maxDailyPrice!= null, "dailyPrice", maxDailyPrice);
        queryWrapper.eq(status!= null, "status", status);
        queryWrapper.like(StrUtil.isNotBlank(description), "description", description);
        queryWrapper.orderBy(StrUtil.isNotBlank(sortField), sortOrder.equalsIgnoreCase(CommonConstant.SORT_ORDER_ASC), sortField);

        return queryWrapper;
    }

    @Override
    public List<VehicleVO> recommendVehicle(HttpServletRequest request) {
        LoginUserVO loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return getPopularVehicles(); // 未登录用户返回热门推荐
        }

        Long userId = loginUser.getId();

        try {
            // 1. 检查并更新模型
            checkAndUpdateModel();

            // 2. 获取用户的交互历史
            List<VehicleBrowsingHistory> browsingHistories = browsingHistoryMapper.selectByUserId(userId);
            List<VehicleFavorite> favorites = favoriteMapper.selectByUserId(userId);

            // 3. 冷启动处理 - 如果是新用户没有交互记录
            if (CollUtil.isEmpty(browsingHistories) && CollUtil.isEmpty(favorites)) {
                return getNewUserRecommendations(userId);
            }

            // 4. 使用基于物品的协同过滤进行推荐
            List<Long> recommendedIds = getItemBasedRecommendations(userId);

            // 5. 如果推荐数量不足，补充基于内容的推荐
            if (recommendedIds.size() < RECOMMEND_LIMIT) {
                List<Long> contentBasedIds = getContentBasedRecommendations(userId,
                        RECOMMEND_LIMIT - recommendedIds.size(),
                        new HashSet<>(recommendedIds));
                recommendedIds.addAll(contentBasedIds);
            }

            // 6. 获取推荐车辆详情并返回
            return vehicleMapper.selectBatchIds(recommendedIds).stream().map(this::getVehicleVO).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("推荐车辆失败", e);
            // 推荐系统出错时降级为热门推荐
            return getPopularVehicles();
        }
    }

    /**
     * 检查并更新数据模型
     */
    private synchronized void checkAndUpdateModel() throws TasteException {
        long currentTime = System.currentTimeMillis();

        // 如果模型不存在或已经过期，则重新构建
        if (dataModel == null || (currentTime - lastModelUpdateTime) > MODEL_UPDATE_INTERVAL) {
            log.info("开始构建推荐模型...");

            // 从数据库获取所有用户-车辆交互数据
            List<UserVehiclePreference> preferences = collectAllUserPreferences();

            // 构建Mahout数据模型
            FastByIDMap<PreferenceArray> preferencesMap = new FastByIDMap<>();
            
            // 按用户ID分组偏好数据
            Map<Long, List<Preference>> userPreferences = preferences.stream()
                .map(p -> (Preference)new GenericPreference(p.getUserId(), p.getVehicleId(), p.getPreference()))
                .collect(Collectors.groupingBy(Preference::getUserID));
            
            // 为每个用户构建偏好数组
            for (Map.Entry<Long, List<Preference>> entry : userPreferences.entrySet()) {
                List<Preference> userPrefList = entry.getValue();
                PreferenceArray prefArray = new GenericUserPreferenceArray(userPrefList.size());
                
                for (int i = 0; i < userPrefList.size(); i++) {
                    prefArray.set(i, userPrefList.get(i));
                }
                
                preferencesMap.put(entry.getKey(), prefArray);
            }
            
            // 创建数据模型
            dataModel = new GenericDataModel(preferencesMap);

            // 使用对数似然相似度作为物品相似度度量
            itemSimilarity = new LogLikelihoodSimilarity(dataModel);

            lastModelUpdateTime = currentTime;
            log.info("推荐模型构建完成");
        }
    }

    /**
     * 收集所有用户-车辆交互数据
     */
    private List<UserVehiclePreference> collectAllUserPreferences() {
        List<UserVehiclePreference> preferences = new ArrayList<>();

        // 浏览历史数据
        List<VehicleBrowsingHistory> browsingHistories = browsingHistoryMapper.selectList(null);
        for (VehicleBrowsingHistory history : browsingHistories) {
            // 浏览行为评分为1.0
            double score = calculateTimeWeight(history.getCreateTime());
            preferences.add(new UserVehiclePreference(
                    history.getUserId(), history.getVehicleId(), (float) score));
        }

        // 收藏数据
        List<VehicleFavorite> favorites = favoriteMapper.selectList(null);
        for (VehicleFavorite favorite : favorites) {
            // 收藏行为评分为3.0
            double score = 3.0 * calculateTimeWeight(favorite.getCreateTime());
            preferences.add(new UserVehiclePreference(
                    favorite.getUserId(), favorite.getVehicleId(), (float)score));
        }

        // 订单数据(如果有)
        List<RentalOrder> orders = rentalOrderMapper.selectList(null);
        for (RentalOrder order : orders) {
            // 下单行为评分为5.0
            double score = 5.0 * calculateTimeWeight(order.getCreateTime());
            preferences.add(new UserVehiclePreference(
                    order.getUserId(), order.getVehicleId(), (float)score));
        }

        return preferences;
    }

    /**
     * 使用基于物品的协同过滤进行推荐
     */
    private List<Long> getItemBasedRecommendations(Long userId) throws TasteException {
        try {
            // 使用Mahout的物品推荐器
            ItemBasedRecommender recommender = new GenericItemBasedRecommender(dataModel, itemSimilarity);

            // 获取推荐结果
            List<RecommendedItem> recommendations = recommender.recommend(userId, RECOMMEND_LIMIT);

            // 转换为车辆ID列表
            return recommendations.stream()
                    .map(RecommendedItem::getItemID)
                    .collect(Collectors.toList());
        } catch (NoSuchUserException e) {
            // 用户没有足够的交互记录
            log.info("用户[{}]没有足够的交互记录，降级使用热门推荐", userId);
            return getPopularVehicleIds(RECOMMEND_LIMIT);
        }
    }

    /**
     * 为新用户提供推荐
     */
    private List<VehicleVO> getNewUserRecommendations(Long userId) {
        log.info("用户[{}]是新用户，使用冷启动推荐策略", userId);

        // 策略2: 如果无法获取用户信息，就使用最热门+随机推荐的组合
        List<VehicleVO> popularVehicles = vehicleMapper.selectHotVehicles(RECOMMEND_LIMIT / 2);
        List<VehicleVO> diverseVehicles = vehicleMapper.selectRandomVehicles(RECOMMEND_LIMIT / 2);

        List<VehicleVO> result = new ArrayList<>(popularVehicles);
        result.addAll(diverseVehicles);
        return result;
    }

    /**
     * 基于内容的推荐(根据车辆特征相似度)
     */
    private List<Long> getContentBasedRecommendations(Long userId, int limit, Set<Long> excludeIds) {
        // 1. 获取用户已交互的车辆
        List<Long> interactedVehicleIds = getUserInteractedVehicleIds(userId);
        if (interactedVehicleIds.isEmpty()) {
            // 用户没有交互记录，返回热门车辆
            return getPopularVehicleIds(limit);
        }

        // 2. 获取这些车辆的特征
        List<Vehicle> interactedVehicles = vehicleMapper.selectBatchIds(interactedVehicleIds);

        // 3. 为每个交互过的车辆找到相似车辆
        Map<Long, Double> vehicleSimilarityScores = new HashMap<>();

        // 获取所有可能的推荐车辆
        List<Vehicle> candidateVehicles = vehicleMapper.selectList(null).stream()
                .filter(v -> !interactedVehicleIds.contains(v.getId()) && !excludeIds.contains(v.getId()))
                .collect(Collectors.toList());

        // 计算每个候选车辆的相似度分数
        for (Vehicle interacted : interactedVehicles) {
            for (Vehicle candidate : candidateVehicles) {
                double similarity = calculateVehicleSimilarity(interacted, candidate);
                vehicleSimilarityScores.put(candidate.getId(),
                        vehicleSimilarityScores.getOrDefault(candidate.getId(), 0.0) + similarity);
            }
        }

        // 4. 排序并返回前N个
        return vehicleSimilarityScores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * 计算两个车辆的内容相似度
     */
    private double calculateVehicleSimilarity(Vehicle v1, Vehicle v2) {
        // 特征权重
        final double BRAND_WEIGHT = 0.3;
        final double MODEL_WEIGHT = 0.2;
        final double TYPE_WEIGHT = 0.15;
        final double ENERGY_WEIGHT = 0.15;
        final double SEAT_WEIGHT = 0.1;
        final double PRICE_WEIGHT = 0.1;

        double similarity = 0.0;

        // 品牌相似度
        if (v1.getBrandId() != null && v1.getBrandId().equals(v2.getBrandId())) {
            similarity += BRAND_WEIGHT;
        }

        // 车型相似度
        if (v1.getModelId() != null && v1.getModelId().equals(v2.getModelId())) {
            similarity += MODEL_WEIGHT;
        }

        // 车系相似度
        if (v1.getVehicleTypeId() != null && v1.getVehicleTypeId().equals(v2.getVehicleTypeId())) {
            similarity += TYPE_WEIGHT;
        }

        // 能源类型相似度
        if (v1.getEnergyTypeId() != null && v1.getEnergyTypeId().equals(v2.getEnergyTypeId())) {
            similarity += ENERGY_WEIGHT;
        }

        // 座位数相似度
        if (v1.getSeatCount() != null && v1.getSeatCount().equals(v2.getSeatCount())) {
            similarity += SEAT_WEIGHT;
        }

        // 价格相似度 (使用反比例函数)
        if (v1.getDailyPrice() != null && v2.getDailyPrice() != null) {
            double price1 = v1.getDailyPrice().doubleValue();
            double price2 = v2.getDailyPrice().doubleValue();
            double priceDiff = Math.abs(price1 - price2);
            double maxPrice = Math.max(price1, price2);
            double priceSimilarity = 1 - (priceDiff / maxPrice);
            similarity += PRICE_WEIGHT * priceSimilarity;
        }

        return similarity;
    }


    /**
     * 获取热门车辆
     */
    private List<VehicleVO> getPopularVehicles() {
        return vehicleMapper.selectHotVehicles(RECOMMEND_LIMIT);
    }

    /**
     * 获取热门车辆ID列表
     */
    private List<Long> getPopularVehicleIds(int limit) {
        return vehicleMapper.selectHotVehicleIds(limit);
    }

    /**
     * 获取用户交互过的车辆ID
     */
    private List<Long> getUserInteractedVehicleIds(Long userId) {
        List<Long> browsingIds = browsingHistoryMapper.selectVehicleIdsByUserId(userId);
        List<Long> favoriteIds = favoriteMapper.selectVehicleIdsByUserId(userId);

        // 合并并去重
        Set<Long> interactedIds = new HashSet<>(browsingIds);
        interactedIds.addAll(favoriteIds);

        return new ArrayList<>(interactedIds);
    }

    /**
     * 计算时间权重
     */
    private double calculateTimeWeight(Date createTime) {
        if (createTime == null) return 0.5;

        long now = System.currentTimeMillis();
        long created = createTime.getTime();
        long diff = now - created;

        // 7天内的记录有较高权重
        if (diff < 7 * 24 * 60 * 60 * 1000L) {
            return 1.0;
        }
        // 30天内的记录有中等权重
        else if (diff < 30 * 24 * 60 * 60 * 1000L) {
            return 0.7;
        }
        // 更早的记录权重较低
        else {
            return 0.3;
        }
    }

    /**
     * 定时任务: 每天凌晨重新计算模型
     */
    @Scheduled(cron = "0 0 2 * * ?")  // 每天凌晨2点执行
    public void refreshRecommendationModel() {
        try {
            log.info("开始重新构建推荐模型...");
            // 清空旧模型，强制下次使用时重建
            dataModel = null;
            itemSimilarity = null;
            lastModelUpdateTime = 0;
            log.info("推荐模型刷新任务完成");
        } catch (Exception e) {
            log.error("刷新推荐模型失败", e);
        }
    }

    /**
     * 用户-车辆偏好数据类
     */
    @Data
    @AllArgsConstructor
    private static class UserVehiclePreference {
        private long userId;
        private long vehicleId;
        private float preference;
    }
}




