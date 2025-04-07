package com.my.utils.spider;

import lombok.Data;

import java.util.List;

@Data
public class BrandInfoResult {

    private BrandInfo brandInfo;
    private ItemList list;


    @Data
    public class BrandInfo {
        private Long brandId;
        private String name;
        private String imgUrl;
    }

    @Data
    public class ItemList {
        private String name;
        private List<Item> list;
    }

    @Data
    public class Item {
        private Long seriesId;
        private String name;
        private String imgUrl;
        private Integer levelId;
        private String levelName;
        private String price;
        private String paramIsShow;
    }
}
