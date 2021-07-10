package com.demo.weibo.common.entity.msg;

import com.demo.weibo.common.entity.Microblog;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.util.List;


@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Weibo {

    /**
     * 微博id
     */
    @Id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 微博实体
     */
    private Microblog microblog;

    /**
     * 微博图片数组
     */
    private List<String> imageList;

    /**
     * 微博发布时间
     */
    private String time;

    /**
     * 微博热度
     */
    private Integer hot;

    /**
     * 是否已点赞 0 1
     */
    private Integer code;

    /**
     * 该用户是否充值了会员
     */
    private Integer vip = 0;

    /**
     * 是否已关注该用户 0 1
     */
    private Integer attention;
}
