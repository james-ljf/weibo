package com.demo.weibo.common.entity.mongo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Accessors(chain = true)
@Document("WeiboAll")
public class MicroblogPojo {

    @Id
    private Long uId;

    /**
     * 1 已点赞 0未点赞
     */
    private Integer code;

}
