package com.demo.weibo.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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

/**
 * 微博
 */
@Data
@Accessors(chain = true)
public class Microblog {

    /**
     * 微博id
     */
    @Id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 用户id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long uId;

    /**
     * 微博内容
     */
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
    private Integer cHeat = 0;

    /**
     * 微博浏览量
     */
    private Integer cRead = 0;

    /**
     * 微博被点赞的数量
     */
    private Integer cLikes = 0;

    /**
     * 微博被转发的数量
     */
    private Integer cForward = 0;

    /**
     * 微博评论数量
     */
    private Integer cComment = 0;


    /**
     * 微博图片url
     */
    private String cImage;

    /**
     * 微博视频url
     */
    private String cVideo;


    /**
     * 微博附带话题(0内容没有#话题，1附带话题)
     */
    private Integer cTalk = 0;

    /**
     * 是否是转发的微博(0不是，1是)
     */
    private Integer isMe = 0;

    /**
     * 微博观看的权限（1为公开，2为粉丝，3为仅自己可见）
     */
    private Integer cPower = 1;

    /**
     * 微博的状态(1正常，2已删除，3违规)
     */
    private Integer cState = 1;

    /**
     * 微博是否可评论(0不可以，1可评论)
     */
    private Integer isComment = 1;

}
