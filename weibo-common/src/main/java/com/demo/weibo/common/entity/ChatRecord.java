package com.demo.weibo.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 聊天记录实体
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Document("ChatRecord")
public class ChatRecord {

    /**
     * 聊天记录id
     */
    @Id
    private Long chatId;

    /**
     * 发送者
     */
    private Long uId;

    /**
     * 接收者
     */
    private Long mId;

    private String message;

    private String date;

    private String code;

}
