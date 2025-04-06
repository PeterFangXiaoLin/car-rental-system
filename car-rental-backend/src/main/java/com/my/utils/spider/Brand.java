package com.my.utils.spider;

import lombok.Data;

@Data
public class Brand {
    private Long id;
    private String name;
    private String logo;
    private String firstletter;
    private String country;
    private Integer state;
    private Integer rank;
    private Integer uvrank;
    private Integer luxury;
    private Integer havenewenergy;
    private Integer orderrank;
}
