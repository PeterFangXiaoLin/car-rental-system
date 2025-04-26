package com.my.domain.dto.rentalorder;

import com.my.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class RentalOrderPageRequest extends PageRequest implements Serializable {
    /**
     * 关键词
     */
    private String searchText;

}
