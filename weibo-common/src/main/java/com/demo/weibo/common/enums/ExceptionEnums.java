package com.demo.weibo.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum ExceptionEnums {

    FIRST_IS_REQUEST(99, "没有按照约定第一个参数是HttpServletRequest类型变量"),
    OBJECT_NULL(101, "空指针异常，获取的值为空。"),
    MQ_ERROR(201, "消费者消费异常");

    private Integer state; //状态码
    private String message; //内容
}
