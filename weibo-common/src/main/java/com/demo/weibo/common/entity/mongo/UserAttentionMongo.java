package com.demo.weibo.common.entity.mongo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Document("UserAttention")
public class UserAttentionMongo {

    @Id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long u2Id;

    /**
     * 关注状态 0 未关注 、 1 单向关注  2互相关注
     * 粉丝 ； 只有 1 状态
     */
    @Field(value = "code")
    private String code;

}
