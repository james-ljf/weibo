package com.demo.weibo.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@Accessors(chain = true)
public class UserOrder {

    /**
     * 订单编号
     */
    @Id
    private String oId;

    /**
     * 订单创建人
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long uId;

    /**
     * 订单名称
     */
    private String oName;

    /**
     * 订单描述
     */
    private String oDetail;

    /**
     * 订单套餐
     */
    private Integer oPackage;

    /**
     * 订单金额
     */
    private String oPrice;

    /**
     * 订单创建时间
     */
    private Date oCreateTime;

}
