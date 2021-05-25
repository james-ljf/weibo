package com.demo.weibo.api.client;

import com.demo.weibo.common.entity.UserDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * From user-service 服务
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

    /**
     * 根据u_id查询用户所有信息
     * @param uId
     * @return
     */
    @PostMapping("/user/info/all")
    UserDetail selectUserAll(@RequestParam("uId") Long uId);
}
