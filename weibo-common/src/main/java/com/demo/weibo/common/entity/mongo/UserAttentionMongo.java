package com.demo.weibo.common.entity.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Document("UserAttention")
public class UserAttentionMongo {

    @Id
    private Long u2Id;

    /**
     * 关注状态 0 未关注 、 1 单向关注  2互相关注
     */
    private Integer aCode;

}
