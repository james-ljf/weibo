package com.demo.weibo.common.util;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class R {

    /**
     * 状态码 1表示成功，0表示失败
     */
    private Integer code;

    /**
     * 相应信息
     */
    private String message;

    /**
     * 封装数据
     */
    private Map<String, Object> data = new HashMap<>();

    /**
     * 操作成功的状态
     * @param message  信息
     * @return  R对象
     */
    public static R ok(String message){
        R r = new R();
        r.setCode(1);
        r.setMessage(message);
        return r;
    }

    /**
     * 无message的成功状态
     * @return
     */
    public static R ok(){
        return ok("");
    }


    /**
     * 操作失败的状态
     * @param message  信息
     * @return  R对象
     */
    public static R error(String message){
        R r = new R();
        r.setCode(0);
        r.setMessage(message);
        return r;
    }

    /**
     * 无message的失败状态
     * @return
     */
    public static R error(){
        return error("");
    }

    /**
     * 自定义返回状态
     * @param code  状态码
     * @param message   信息
     * @return  R实例
     */
    public static R r(Integer code, String message){
        R r = new R();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }

    /**
     * 链式增加data数据的方法
     * @param key   数据的键
     * @param value 数据的值
     * @return  R对象
     */
    public R addData(String key, Object value){
        this.data.put(key, value);
        return this;
    }

}
