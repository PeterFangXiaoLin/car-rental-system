package com.my;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.domain.entity.*;
import com.my.domain.enums.VehicleStatusEnum;
import com.my.service.*;
import com.my.utils.RandomUtils;
import com.qcloud.cos.COSClient;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
@Slf4j
class CarRentalBackendApplicationTests {

    @Resource
    private COSClient cosClient;

    @Resource
    private VehicleService vehicleService;

    @Resource
    private VehicleBrandService vehicleBrandService;

    @Resource
    private VehicleModelService vehicleModelService;

    @Resource
    private VehicleTypeDictService vehicleTypeDictService;

    @Resource
    private EnergyTypeDictService energyTypeDictService;

    @Resource
    private StoreService storeService;

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
    void test2() throws IOException {
        String url = "https://www.dongchedi.com/auto/library/x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-2-x-x";
        Document document = Jsoup.connect(url).get();
        Elements element = document.getElementsByClass("brand_brands__1VgjE");
        element.select("li").forEach(li -> {
            String brandName = li.select(".brand_name__1-UUM").text();
            String brandLogo = li.select(".brand_image__2yj7_ > img").attr("src");
            String[] split = li.select(".brand_link__3F8eK").attr("href").split("-");
            String brandId = split[split.length - 3];
            System.out.println(brandName);
            System.out.println(brandLogo);
            System.out.println(brandId);
            System.out.println("----------");

        });

    }

    @Test
    void test4() throws IOException {
        String url = "https://www.dongchedi.com/motor/pc/car/brand/select_series_v2?aid=1839&app_name=auto_web_pc";
        Map<String, Object> params = new HashMap<>();
        params.put("brand", 2);
        params.put("city_name", "广州");
        params.put("sort_new", "hot_desc");
        params.put("page", 1);
        params.put("limit", 150);
        String res = HttpUtil.post(url, params);
        JSONObject jsonObject = JSONUtil.parseObj(res);

        // 解析JSON获取series列表
        JSONObject data = jsonObject.getJSONObject("data");
        List<JSONObject> seriesList = data.getJSONArray("series").toList(JSONObject.class);

        // 遍历处理每个series
        for (JSONObject series : seriesList) {
            String brandName = series.getStr("brand_name");
            String seriesName = series.getStr("outter_name");
            String coverUrl = series.getStr("cover_url");
            List<Integer> carIds = series.getJSONArray("car_ids").toList(Integer.class);

            System.out.println("品牌: " + brandName);
            System.out.println("车系: " + seriesName);
            System.out.println("封面图: " + coverUrl);
            System.out.println("包含车型ID: " + carIds);
            System.out.println("----------------------");

            String carDetailUrl = "https://www.dongchedi.com/auto/params-carIds-%s";
            for (int carId : carIds) {
                String carUrl = String.format(carDetailUrl, carId);
                Document document = Jsoup.connect(carUrl).get();
                // 拿到座位数、能源类型，还有车型
                Elements div = document.select(".configuration_main__2NCwO");
                String carModel = div.select(".cell_normal__37nRi").text();
                String energyType = div.select(".cell_normal__37nRi").text();
            }
        }
    }

    @Test
    void test3() {
        List<Integer> brandIdList = Arrays.asList(2, 3, 35, 1, 10012, 5, 40, 59, 165, 73, 84, 19, 15, 199, 238, 420, 18, 36, 515, 63, 348, 300, 112, 195, 67, 883);
        String baseUrl = "https://www.dongchedi.com/auto/library/x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-%s-x-x";
        for (int brandId : brandIdList) {
            String url = String.format(baseUrl, brandId);
            Document document = null;
            try {
                document = Jsoup.connect(url).get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    // 获取车辆信息
    @Test
    void test5() throws IOException {
        String url = "https://www.dongchedi.com/auto/params-carIds-110727";
        Document document = Jsoup.connect(url).get();
        // 拿到座位数、能源类型，还有车型
        Elements div = document.select(".configuration_main__2NCwO");

        // 获取级别信息
        String level = div.select("div[data-row-anchor=jb] div").get(1).text();

        // 获取能源类型
        String energyType = div.select("div[data-row-anchor=fuel_form] div").get(1).text();

        String seat = div.select("div[data-row-anchor=seat_count] div").get(1).text();

        String carName = document.select("a.cell_car__28WzZ.line-2").text();

        System.out.println("级别: " + level);
        System.out.println("能源类型: " + energyType);
        System.out.println("座位数: " + seat);
        System.out.println("车辆名称: " + carName);
    }

    // 获取车辆的图片
    @Test
    void test6() throws IOException {
        String url = "https://www.dongchedi.com/auto/series/2/model-110727";
        Document document = Jsoup.connect(url).get();
        String src = document.select("div.tw-rounded-2.tw-filter.tw-blur-20 img").attr("src");
        System.out.println(src);
    }

    @Test
    void testSpider() throws IOException {
        List<Integer> brandIdList = Arrays.asList(2, 3, 35, 1, 10012, 5, 40, 59, 165, 73, 84, 19, 15, 199, 238, 420, 18, 36, 515, 63, 348, 300, 112, 195, 67, 883);
        String baseUrl = "https://www.dongchedi.com/auto/library/x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-%s-x-x";
        for (int i = 10; i < brandIdList.size(); i++) {
            int brandId = brandIdList.get(i);
            String url = String.format(baseUrl, brandId);
            Document document = Jsoup.connect(url).get();
            Elements element = document.getElementsByClass("brand_brands__1VgjE");
            int finalI = i;
            element.select("li").forEach(li -> {
                String brandName = li.select(".brand_name__1-UUM").text();
                String brandLogo = li.select(".brand_image__2yj7_ > img").attr("src");
                String[] split = li.select(".brand_link__3F8eK").attr("href").split("-");
                String brand_id = split[split.length - 3];

                // 新增品牌
                VehicleBrand vehicleBrand = new VehicleBrand();
                vehicleBrand.setBrandName(brandName);
                vehicleBrand.setBrandLogo("https:" + brandLogo);
                vehicleBrand.setFirstLetter((char) (finalI + 'A') + "");
                vehicleBrandService.save(vehicleBrand);

                String getCarByBrand = "https://www.dongchedi.com/motor/pc/car/brand/select_series_v2?aid=1839&app_name=auto_web_pc";
                Map<String, Object> params = new HashMap<>();
                params.put("brand", brand_id);
                params.put("city_name", "广州");
                params.put("sort_new", "hot_desc");
                params.put("page", 1);
                params.put("limit", 10);
                String res = HttpUtil.post(getCarByBrand, params);
                JSONObject jsonObject = JSONUtil.parseObj(res);

                // 解析JSON获取series列表
                JSONObject data = jsonObject.getJSONObject("data");
                List<JSONObject> seriesList = data.getJSONArray("series").toList(JSONObject.class);

                // 遍历处理每个series
                for (JSONObject series : seriesList) {
                    String seriesName = series.getStr("outter_name");
                    String coverUrl = series.getStr("cover_url");
                    String concernId = series.getStr("concern_id");
                    List<Integer> carIds = series.getJSONArray("car_ids").toList(Integer.class);


                    // 新增车系
                    VehicleModel vehicleModel = new VehicleModel();
                    vehicleModel.setBrandId(vehicleBrand.getId());
                    vehicleModel.setModelName(seriesName);
                    vehicleModel.setModelLogo(coverUrl);
                    vehicleModelService.save(vehicleModel);

                    String carDetailUrl = "https://www.dongchedi.com/auto/params-carIds-%s";
                    String carPictureUrl = "https://www.dongchedi.com/auto/series/%s/model-%s";
                    int count = 0;
                    for (int carId : carIds) {
                        if (count == 3) {
                            break;
                        }
                        String carUrl = String.format(carDetailUrl, carId);
                        Document document1 = null;
                        try {
                            document1 = Jsoup.connect(carUrl).get();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        // 拿到座位数、能源类型，还有车型
                        Elements div = document1.select(".configuration_main__2NCwO");

                        // 获取级别信息
                        String level = div.select("div[data-row-anchor=jb] div").get(1).text();

                        // 获取能源类型
                        String energyType = div.select("div[data-row-anchor=fuel_form] div").get(1).text();

                        String seat = div.select("div[data-row-anchor=seat_count] div").get(1).text();

                        String carName = document1.select("a.cell_car__28WzZ.line-2").text();

                        // 获取车辆图片
                        String carPicture = String.format(carPictureUrl, concernId, carId);
                        Document document2 = null;
                        try {
                            document2 = Jsoup.connect(carPicture).get();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        String src = document2.select("div.tw-rounded-2.tw-filter.tw-blur-20 img").attr("src");

                        // 获取车辆年份
                        String carDetail = "https://www.dongchedi.com/motor/pc/car/series/car_dealer_price";
                        Map<String, Object> params1 = new HashMap<>();
                        params1.put("aid", 1839);
                        params1.put("app_name", "auto_web_pc");
                        params1.put("car_ids", carId);
                        params1.put("city_name", "广州");
                        String carDetailRes = HttpUtil.get(carDetail, params1);
                        JSONObject jsonObject1 = JSONUtil.parseObj(carDetailRes);
                        JSONObject data1 = jsonObject1.getJSONObject("data").getJSONObject(String.valueOf(carId));
                        String year = data1.getStr("year");

                        // 新增车辆
                        Vehicle vehicle = new Vehicle();
                        vehicle.setName(carName);
                        vehicle.setVehicleNo(RandomUtils.randomLicensePlate());
                        vehicle.setBrandId(vehicleBrand.getId());
                        vehicle.setModelId(vehicleModel.getId());
                        vehicle.setImageUrl("https:" + src);
                        if (StrUtil.isBlank(year)) {
                            vehicle.setProductionYear(2025);
                        } else {
                            vehicle.setProductionYear(Integer.parseInt(year));
                        }
                        try {
                            vehicle.setSeatCount(Integer.parseInt(seat));
                        } catch (NumberFormatException e) {
                            vehicle.setSeatCount(4);  // 转换失败时使用默认值
                        }
                        vehicle.setStatus(VehicleStatusEnum.AVAILABLE.getValue());
                        int price = new Random().nextInt(222) + 77;
                        vehicle.setDailyPrice(new BigDecimal(String.valueOf(price)));


                        List<VehicleTypeDict> vehicleTypeDictList = vehicleTypeDictService.list();
                        boolean flag = false;
                        for (VehicleTypeDict vehicleTypeDict : vehicleTypeDictList) {
                            if (vehicleTypeDict.getTypeName().equals(level)) {
                                vehicle.setVehicleTypeId(vehicleTypeDict.getId());
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            VehicleTypeDict vehicleTypeDict = new VehicleTypeDict();
                            vehicleTypeDict.setTypeName(level);
                            vehicleTypeDictService.save(vehicleTypeDict);
                            vehicle.setVehicleTypeId(vehicleTypeDict.getId());
                        }

                        List<EnergyTypeDict> energyTypeDictList = energyTypeDictService.list();
                        boolean flag1 = false;
                        for (EnergyTypeDict energyTypeDict : energyTypeDictList) {
                            if (energyTypeDict.getTypeName().equals(energyType)) {
                                vehicle.setEnergyTypeId(energyTypeDict.getId());
                                flag1 = true;
                                break;
                            }
                        }
                        if (!flag1) {
                            EnergyTypeDict energyTypeDict = new EnergyTypeDict();
                            energyTypeDict.setTypeName(energyType);
                            energyTypeDictService.save(energyTypeDict);
                            vehicle.setEnergyTypeId(energyTypeDict.getId());
                        }
                        vehicleService.save(vehicle);
                        log.info("添加车辆成功");
                        count++;
                    }
                }
            });
        }
    }

    @Test
    void testSpiderStore() throws IOException {
        String url = "https://service.zuche.com/";
        Document document = Jsoup.connect(url).get();
        Elements aList = document.select("dl.citySort dd a:not(.more-city)");

        Pattern pattern = Pattern.compile("deptId=(\\d+)");
        String urlTemplate = "https://service.zuche.com/api/gw.do?uri=/action/carrctapi/order/deptDetail/v1";
        String bodyTemplate = "data={\"deptId\":\"%s\"}";

        List<Store> storeList = new ArrayList<>();

        for (Element a : aList) {
            String href = a.attr("href");
            Document doc = Jsoup.connect(href).get();
            Elements ddLinks = doc.select("ul.depUlClass dd > a");
            for (Element ddLink : ddLinks) {
                String href1 = ddLink.attr("href");

                Matcher matcher = pattern.matcher(href1);
                if (matcher.find()) {
                    try {
                        String deptId = matcher.group(1);
                        String body = HttpRequest.post(urlTemplate)
                                .header(Header.REFERER, String.format("https://service.zuche.com/dept/detail.do?deptId=%s", deptId))
                                .body(String.format(bodyTemplate, deptId))
                                .execute()
                                .body();
                        JSONObject jsonObject = JSONUtil.parseObj(body);
                        JSONObject content = jsonObject.getJSONObject("content");

                        JSONArray largeImgs = content.getJSONArray("largeImgs");
                        String deptMobile = content.getStr("deptMobile");
                        String deptName = content.getStr("deptName");
                        String deptAddress = content.getStr("deptAddress");
                        String deptLon = content.getStr("deptLon");
                        String deptLat = content.getStr("deptLat");

                        String imgUrls = CollUtil.join(largeImgs, StrUtil.COMMA);
                        Store store = new Store();
                        store.setStoreName(deptName);
                        store.setAddress(deptAddress);
                        store.setMobile(deptMobile);
                        store.setImages(imgUrls);
                        store.setLongitude(new BigDecimal(deptLon));
                        store.setLatitude(new BigDecimal(deptLat));
                        storeList.add(store);
                    } catch (Exception e) {
                        log.info("爬取失败，跳过此条");
                    }
                }
            }
        }

        storeService.saveBatch(storeList);
        log.info("爬取成功");
    }

    @Test
    void testSpiderStore2() throws IOException {
        String url = "https://service.zuche.com/api/gw.do?uri=/action/carrctapi/order/deptDetail/v1";
        System.out.println(HttpRequest.post(url)
//                .header(Header.ACCEPT, "application/json, text/plain, */*")
//                        .header(Header.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9")
//                        .header(Header.CONNECTION, "keep-alive")
//                        .header(Header.CONTENT_TYPE, "application/x-www-form-urlencoded")
//                        .header(Header.COOKIE, "aliyungf_tc=3aaf8fe1dc883b9538af468e587cd675a09f918dc0a55bf415e5897c3d579c68; lctuid=37b58ca0320755b01340cef1c7bc2ed8; acw_tc=f5bc4421-16fd-4e02-bc68-d908ee1b3054907b9b4602d24acc7ac211f4c2ec945a; intranet-sessionid=a9e49952-9e78-4900-b022-d4a142cf974b")
//                        .header(Header.ORIGIN, "https://service.zuche.com")
                        .header(Header.REFERER, "https://service.zuche.com/dept/detail.do?deptId=1248")
//                        .header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36")
//                        .header("Sec-Fetch-Dest", "empty")
//                        .header("Sec-Fetch-Mode", "cors")
//                        .header("Sec-Fetch-Site", "same-origin")
//                        .header("sec-ch-ua", "\"Google Chrome\";v=\"135\", \"Not-A.Brand\";v=\"8\", \"Chromium\";v=\"135\"")
//                        .header("sec-ch-ua-mobile", "?0")
//                        .header("sec-ch-ua-platform", "\"Windows\"")
                        .body("data={\"deptId\":\"1248\"}")
                        .execute()
                        .body());

    }

    @Test
    void test7() {
        List<VehicleBrand> list = vehicleBrandService.list();
        for (VehicleBrand vehicleBrand : list) {
            String brandLogo = vehicleBrand.getBrandLogo();
            brandLogo = brandLogo.replace("https:////", "https://");
            vehicleBrand.setBrandLogo(brandLogo);
        }
        vehicleBrandService.updateBatchById(list);
    }

    @Test
    void test8() {
        List<Vehicle> vehicleList = vehicleService.query().eq("energyTypeId", 1910988426543300610L).list();
        vehicleService.removeBatchByIds(vehicleList);
    }
}
