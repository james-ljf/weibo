package com.demo.weibo.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = "signup-service")
public interface SignClient {

    @PostMapping("/sign/account/select-all")
    List<Long> selectAllId();

}
