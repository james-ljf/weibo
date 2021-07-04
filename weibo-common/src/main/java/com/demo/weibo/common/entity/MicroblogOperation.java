package com.demo.weibo.common.entity;

import com.demo.weibo.common.entity.mongo.MicroblogPojo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 微博点赞、收藏、转发
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Document("WeiboAll")
public class MicroblogOperation {

    /**
     * 微博id
     */
    @Id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long cId;

    /**
     * 发布微博的用户id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long mId;

    /**
     * 微博的点赞列表
     */
    private List<MicroblogPojo> likeList;

    /**
     * 微博的转发列表
     */
    private List<MicroblogPojo> forwardList;

    /**
     * 微博的收藏列表
     */
    private List<MicroblogPojo> collectionList;

}
