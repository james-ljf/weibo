package com.demo.weibo.email.controller;

import com.demo.weibo.common.annotation.UserLoginAnnotation;
import com.demo.weibo.common.util.FormatUtil;
import com.demo.weibo.common.util.R;
import com.demo.weibo.email.service.EmailService;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/email-service")
public class EmailController {

    @Autowired
    private EmailService emailService;

    /**
     * 开闭原则
     * 代码在维护的时候不通过修改原来的代码，只增加新的代码就可以完成内容的增加
     * @param body
     * @return
     */
    @PostMapping("/send-email")
    public Object sendEmail(@RequestBody Map<String, String> body){
        String email = body.get("to");
        if (!FormatUtil.checkEmail(email)){
            return R.error("邮箱格式不正确！");
        }
        String strategy = body.get("strategy");
        if (!StringUtils.hasText(strategy)){
            return R.error("您的参数有误！");
        }
        String content = body.get("content");
        return emailService.sendEmail(email, strategy, content);
    }

}
