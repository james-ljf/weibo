package com.demo.weibo.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserVipExchange {

    /**
     * 兑换订单id
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
     * 物品id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long vId;

    /**
     * 物品图片
     */
    private String vImage;

    /**
     * 兑换时间
     */
    private String createTime;



}
