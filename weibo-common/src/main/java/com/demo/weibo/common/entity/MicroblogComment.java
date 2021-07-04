package com.demo.weibo.common.entity;

import com.demo.weibo.common.entity.mongo.CommentMongo_1;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 微博评论
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Document("Comment")
public class MicroblogComment {

    /**
     * 微博id
     */
    @Id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long cId;

    /**
     * 微博评论列表
     */
    private List<CommentMongo_1> commentList;

}
