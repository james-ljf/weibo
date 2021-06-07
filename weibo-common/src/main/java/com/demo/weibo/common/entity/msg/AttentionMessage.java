package com.demo.weibo.common.entity.msg;

import com.demo.weibo.common.entity.msg.U2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 用户关注消息实体类
 * 前端进行轮询，将关注的未读消息通知到被关注用户
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Document("Message")
public class AttentionMessage {

    @Id
    private Long u1Id;

    private List<U2> msgList;

}
