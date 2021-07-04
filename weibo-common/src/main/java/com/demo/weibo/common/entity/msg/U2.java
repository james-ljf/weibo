package com.demo.weibo.common.entity.msg;

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
@Document("Message")
/**
 * 消息实体类
 */
public class U2 {

    /**
     * 消息来自的用户
     */
    @Id
    private Long u2Id;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息状态  0未读  1已读
     */
    private Integer code;

}
