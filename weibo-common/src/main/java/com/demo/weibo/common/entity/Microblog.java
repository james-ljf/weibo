package com.demo.weibo.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "weibo", indexStoreType = "microblog")
public class Microblog {

    /**
     * 微博id
     */
    @Id
    private Long id;

    /**
     * 用户id
     */
    private Long uId;

    /**
     * 微博内容
     */
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String cContent;

    /**
     * 微博发布的地址
     */
    private String cAddress;

    /**
     * 微博发布的时间
     */
    private Date cTime;

    /**
     * 微博热度
     */
    private Integer cHeat;

    /**
     * 微博浏览量
     */
    private Integer cRead;

    /**
     * 微博被点赞的数量
     */
    private Integer cLikes;

    /**
     * 微博被转发的数量
     */
    private Integer cForward;

    /**
     * 微博评论数量
     */
    private Integer cComment;


    /**
     * 微博图片url
     */
    private List<String> cImage;

    /**
     * 微博视频url
     */
    private String cVideo;


    /**
     * 微博附带话题(0内容没有#话题，1附带话题)
     */
    private Integer cTalk;

    /**
     * 是否是转发的微博(0不是，1是)
     */
    private Integer isMe;

    /**
     * 微博观看的权限（1为公开，2为粉丝，3为仅自己可见）
     */
    private Integer cPower;

    /**
     * 微博的状态(1正常，2已删除，3违规)
     */
    private Integer cState;

    /**
     * 微博是否可评论(0不可以，1可评论)
     */
    private Integer isComment;

}
