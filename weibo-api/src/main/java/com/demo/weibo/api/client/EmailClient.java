package com.demo.weibo.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "email-service")
public interface EmailClient {

    @PostMapping("/email-service/send-email")
    Object sendEmail(@RequestBody Map<String, String> body);

}
