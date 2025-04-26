package com.my.domain.dto.store;

import com.my.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class StoreQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 门店地址
     */
    private String address;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 开始营业时间
     */
    private LocalTime openTime;

    /**
     * 结束营业时间
     */
    private LocalTime closeTime;

    /**
     * 状态：0-关闭，1-营业中
     */
    private Integer status;
}
