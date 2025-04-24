package com.my.domain.dto.vehiclebrowsinghistory;

import com.my.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class BrowsHistoryQueryRequest extends PageRequest implements Serializable {

    /**
     * 搜索关键字
     */
    private String searchText;
}
