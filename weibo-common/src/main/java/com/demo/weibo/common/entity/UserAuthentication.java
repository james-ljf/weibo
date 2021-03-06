package com.demo.weibo.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import static com.baomidou.mybatisplus.annotation.IdType.AUTO;

@Data
@Accessors(chain = true)
public class UserAuthentication {

    /**
     * id
     */
    @TableId(type = AUTO)
    private Integer id;

    /**
     * 认证信息
     */
    private String aContent;

    /**
     * 申请认证的理由
     */
    private String aReason;

    /**
     * 认证的状态（0未认证，1已认证，2正在审核中）
     */
    private Integer aState;

    /**
     * 申请认证的时间
     */
    private Date aTime;

    /**
     * 用户id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long uId;

}
