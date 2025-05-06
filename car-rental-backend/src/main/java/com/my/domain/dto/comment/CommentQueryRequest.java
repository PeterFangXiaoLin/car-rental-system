package com.my.domain.dto.comment;

import com.my.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 车辆评论查询请求
 */
@Data
public class CommentQueryRequest extends PageRequest implements Serializable {
    /**
     * 车辆ID
     */
    private Long vehicleId;
}
