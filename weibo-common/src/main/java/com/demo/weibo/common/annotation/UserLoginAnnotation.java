package com.demo.weibo.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserLoginAnnotation {

    /**
     * 判断功能是否是登录才能使用
     * @return
     */
    boolean needLogin() default true;

}
