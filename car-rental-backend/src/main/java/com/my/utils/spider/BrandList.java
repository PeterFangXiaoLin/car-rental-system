package com.my.utils.spider;

import lombok.Data;

import java.util.List;

@Data
public class BrandList {
    private Long total;

    private List<Brand> brandlist;
}
