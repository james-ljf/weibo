package com.demo.weibo.common.entity.mongo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

/**
 * 微博二级评论
 */
@Data
@Accessors(chain = true)
public class CommentMongo_2 {

    /**
     * 用户id
     */
    @Id
    private Long uId;

    private String content;

    private String date;


}
