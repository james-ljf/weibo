package com.demo.weibo.common.entity.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Attention implements Serializable {

    @Field("_id")
    private Long u1Id;

    private Long u2Id;

    private String strategy;

}
