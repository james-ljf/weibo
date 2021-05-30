package com.demo.weibo.common.entity.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Attention {

    private Long u1Id;

    private Long u2Id;

    private Integer code;

}
