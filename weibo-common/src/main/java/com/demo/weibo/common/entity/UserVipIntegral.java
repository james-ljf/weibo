package com.demo.weibo.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * 会员兑换物品实体
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserVipIntegral {

    /**
     * 物品id
     */
    @Id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 物品名字
     */
    private String iName;

    /**
     * 物品图片url
     */
    private String iImage;

    /**
     * 物品详细信息
     */
    private String iDetail;

    /**
     * 物品上架时间
     */
    private Date iTime;

    /**
     * 物品数量
     */
    private Integer iNum;

    /**
     * 物品状态（1 已上架， 0 已下架）
     */
    private Integer iCode;



}
