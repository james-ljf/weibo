package com.demo.weibo.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.Map;

@Data
@Accessors(chain = true)
public class UserDetail {

    /**
     * 用户信息id
     */
    @TableId
    private Integer id;

    /**
     * 用户id
     */
    private Long uId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户年龄
     */
    private Integer age;

    /**
     * 用户性别
     */
    private String sex;

    /**
     * 用户出生日期
     */
    private Date birthday;

    /**
     * 用户的省份
     */
    private String province;

    /**
     * 用户的城市
     */
    private String city;

    /**
     * 用户的区
     */
    private String area;

    /**
     * 用户的头像url
     */
    private String avatar;

    /**
     * 用户资料片封面图片url
     */
    private String cover;

    /**
     * 用户个性签名
     */
    private String personality;

    /**
     * 用户粉丝数量
     */
    private Integer fans;

    /**
     * 用户发布的微博的数量
     */
    private Integer article;

    /**
     * 用户关注人数
     */
    private Integer attention;

    /**
     * 用户是否已认证(0没有，1已认证)
     */
    private Integer authentication;

    /**
     * 用户是否开通了vip(0未开通，1已开通)
     */
    private Integer vip;

    /**
     * 用户是否创建了话题(0没有，1有)
     */
    private Integer is_talk;

}
