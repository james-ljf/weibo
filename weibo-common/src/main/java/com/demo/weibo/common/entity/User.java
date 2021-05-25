package com.demo.weibo.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class User {

    /**
     * 用户id
     */
    @TableId
    private Long id;

    /**
     * 用户账号
     */
    private String account;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户注册时间
     */
    private Date create_time;

    /**
     * 用户账号的状态(1可用，0已注销，2已封号)
     */
    private Integer state;


}
