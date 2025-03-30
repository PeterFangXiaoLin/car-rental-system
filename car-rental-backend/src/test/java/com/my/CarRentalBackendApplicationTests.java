package com.my;

import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.Bucket;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class CarRentalBackendApplicationTests {

    @Resource
    private COSClient cosClient;

    @Test
    void contextLoads() {
        List<Bucket> buckets = cosClient.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(bucket.getName());
            System.out.println(bucket.getLocation());
        }
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
}
