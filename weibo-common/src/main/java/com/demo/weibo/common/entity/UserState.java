package com.demo.weibo.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class UserState {

    /**
     * id
     */
    @TableId
    private Integer id;

    /**
     * 用户id
     */
    private Long uId;

    /**
     * 登录状态
     */
    private Integer uCode;

    /**
     * 是否点击记住我功能
     */
    private Integer uRemember;

    /**
     * 用户最近一次登录时间
     */
    private Date loginTime;

    /**
     * 用户最近登录的设备的ip地址
     */
    private String ip;

    /**
     * 用户最近一次登录的设备的名称
     */
    private String hostName;

}
