package com.demo.weibo.common.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserAttention {

    private Long id;

    private Long u1Id;

    private Long u2Id;

    private Integer aState;

}
