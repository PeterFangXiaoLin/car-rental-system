package com.my.utils.spider;

import lombok.Data;

@Data
public class BrandResult {
    private Integer returncode;
    private String message;
    private BrandList result;
}
