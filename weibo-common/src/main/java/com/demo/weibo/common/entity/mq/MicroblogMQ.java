package com.demo.weibo.common.entity.mq;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class MicroblogMQ implements Serializable {

    private Long uId;

    private Long cId;

    private Integer strategy;

}
