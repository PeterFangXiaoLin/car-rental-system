package com.my;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.domain.entity.VehicleBrand;
import com.my.service.VehicleBrandService;
import com.my.utils.spider.Brand;
import com.my.utils.spider.BrandResult;
import com.qcloud.cos.COSClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
class CarRentalBackendApplicationTests {

    @Resource
    private COSClient cosClient;

    @Resource
    private VehicleBrandService vehicleBrandService;

    @Test
    void contextLoads() {
        String res = HttpUtil.get("https://car-web-api.autohome.com.cn/car/brand/getbrand?sorttype=1");
        JSONObject jsonObject = JSONUtil.parseObj(res);
        BrandResult bean = JSONUtil.toBean(jsonObject, BrandResult.class);

        List<Brand> brandlist = bean.getResult().getBrandlist();
        List<VehicleBrand> list = new ArrayList<>();
        for (int i = 0; i < brandlist.size(); i++) {
            Brand brand = brandlist.get(i);
            VehicleBrand vehicleBrand = new VehicleBrand();
            vehicleBrand.setBrandName(brand.getName());
            vehicleBrand.setBrandLogo(brand.getLogo());
            vehicleBrand.setFirstLetter(brand.getFirstletter());
            list.add(vehicleBrand);

            // 每100个或最后一批执行插入
            if ((i + 1) % 100 == 0 || i == brandlist.size() - 1) {
                vehicleBrandService.saveBatch(list);
                list.clear();  // 清空列表准备下一批
            }
        }
        log.info("插入成功");
    }

    @Test
    void test() throws JsonProcessingException {
        Map<String, Object> params = new HashMap<>();
        params.put("key", "40106296d1d01fb313057e3d27ab34fb");
        params.put("keywords", "北京");
        params.put("subdistrict", "1");

        String res = HttpUtil.get("https://restapi.amap.com/v3/config/district", params);
        // 使用Jackson解析JSON
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(res);

        // 提取并打印行政区划信息
        JsonNode districts = rootNode.path("districts");
        for (JsonNode district : districts) {
            System.out.println("省/市: " + district.path("name").asText());
            System.out.println("中心坐标: " + district.path("center").asText());

            // 打印下级行政区
            JsonNode subDistricts = district.path("districts");
            for (JsonNode subDistrict : subDistricts) {
                System.out.println("  区/县: " + subDistrict.path("name").asText());
                System.out.println("  中心坐标: " + subDistrict.path("center").asText());
            }
        }
    }

    @Test
    void test1() {
        String res = HttpUtil.get("https://car-web-api.autohome.com.cn/car/brand/getbrand?sorttype=1");
        JSONObject jsonObject = JSONUtil.parseObj(res);
        BrandResult bean = JSONUtil.toBean(jsonObject, BrandResult.class);

        List<Brand> brandlist = bean.getResult().getBrandlist();
        for (Brand brand : brandlist) {
            
        }
    }
}
