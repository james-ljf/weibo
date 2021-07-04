package com.demo.weibo.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserVip {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "u_id")
    private Long uId;

    /**
     * vip到期时间
     */
    private String expireTime;

    /**
     * vip充值的时间
     */
    private Date rechargeTime;

    /**
     * vip积分
     */
    private Integer integral;

    /**
     * 会员状态（0 已过期， 1 正在使用）
     */
    private Integer vipCode;

}
