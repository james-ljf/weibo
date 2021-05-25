package com.demo.weibo.common.util;

/**
 * 检验工具类
 */
public class FormatUtil {

    /**
     * 检验用户名格式
     * @param uName
     * @return
     */
    public static boolean checkAccount(String uName){
        String regex = "[a-zA-Z][a-zA-Z0-9_]{7,15}";
        return uName != null && uName.matches(regex);
    }

    /**
     * 检验邮箱格式
     * @param email
     * @return
     */
    public static boolean checkEmail(String email){
        String regex = "[a-zA-Z0-9][a-zA-Z0-9_]{1,19}@[a-zA-Z0-9]+(\\.com|\\.cn|\\.com\\.cn)";
        return email != null && email.matches(regex);
    }

    /**
     * 检验管理员的名称是否合规
     * @param name
     * @return
     */
    public static boolean checkAdminAccount(String name){
        String regex = "[a-zA-Z][a-zA-Z0-9_]{2,15}";
        return name != null && name.matches(regex);
    }

}
