package com.my.domain.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserReviewRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 状态：0-待审核，1-通过，2-拒绝 (要修改的状态)
     */
    private Integer reviewStatus;

    /**
     * 拒绝原因
     */
    private String rejectReason;

    private static final long serialVersionUID = 1L;
}
