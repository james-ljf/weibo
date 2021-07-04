package com.demo.weibo.common.entity.mongo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Accessors(chain = true)
@Document("Comment")
public class CommentMongo_1 {

    /**
     * 评论id
     */
    @Id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long commentId;

    /**
     * 用户id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long uId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论时间
     */
    private String date;

    /**
     * 评论点赞列表
     */
    private List<Long> likeList;

}
