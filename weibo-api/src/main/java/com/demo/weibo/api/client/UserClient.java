package com.demo.weibo.api.client;

import com.alibaba.fastjson.JSONObject;
import com.demo.weibo.common.entity.UserDetail;
import com.demo.weibo.common.entity.mongo.UserAttentionMongo;
import com.demo.weibo.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * From 用户 服务
 */
@FeignClient(value = "user-service")
public interface UserClient {

    /**
     * 插入email
     * @param userDetail
     * @return Object
     */
    @PostMapping("/user/info/add-info")
    Object addUserInfo(@RequestBody UserDetail userDetail);

    /**
     * 查询email是否已存在
     * @param email
     * @return Object
     */
    @GetMapping("/user/info/select-email")
    UserDetail selectUserEmail(@RequestParam("email") String email);

//    /**
//     * 根据u_id查询用户所有信息
//     * @param uId
//     * @return
//     */
//    @PostMapping("/user/info/all/{uId}")
//    UserDetail selectUserAll(@PathVariable("uId") Long uId);

    /**
     * 查询自己所有信息
     * @return
     */
    @PostMapping("/user/info/all")
    List<UserDetail> selectAll();

    /**
     * 查询自己所有关注
     * @param uId   自己的id
     * @return  List
     */
    @GetMapping("/user/attention/myself-attention")
    List<UserAttentionMongo> findAllMyAttention(@RequestParam("uId") Long uId);

}
