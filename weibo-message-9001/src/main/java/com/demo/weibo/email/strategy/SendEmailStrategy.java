package com.demo.weibo.email.strategy;


import com.demo.weibo.common.util.R;

public interface SendEmailStrategy {

    /**
     * 判断当前的策略是不是“我的策略”
     * @param strategy 字符串的策略值
     * @return
     */
    boolean isMyStrategy(String strategy);

    /**
     * 当是我的策略的时候执行的内容
     * @param to        发送给谁
     * @param content   发送的内容
     * @return
     */
    R callback(String to, String content);

}
