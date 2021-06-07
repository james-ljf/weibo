package com.demo.weibo.common.entity;

import com.demo.weibo.common.entity.mongo.UserAttentionMongo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Document("UserAttention") //使用mongodb哪个集合
public class UserAttention{

    /**
     * 当前用户id
     */
    @Id
    private Long u1Id;

    /**
     * 当前用户关注的用户数组，存放被关注的用户id和状态(1单向关注，2互关)
     */
    private List<UserAttentionMongo> attentionList;

}
