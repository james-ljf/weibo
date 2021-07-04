package com.demo.weibo.common.entity.mongo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Accessors(chain = true)
@Document("WeiboAll")
public class MicroblogPojo {

    @Id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long uId;

    /**
     * 1 是    0 否
     */
    private Integer code;

}
