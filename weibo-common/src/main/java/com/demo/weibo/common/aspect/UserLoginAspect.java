package com.demo.weibo.common.aspect;


import com.demo.weibo.common.annotation.UserLoginAnnotation;
import com.demo.weibo.common.entity.User;
import com.demo.weibo.common.util.JwtUtil;
import com.demo.weibo.common.util.R;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 环绕增强
 */
@Component
@Aspect
@Order(-1)
public class UserLoginAspect {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * @annotation  被注解标记的方法是切点
     * @within  被注解标记的类中全部的方法都是切点
     */
    @Pointcut("@annotation(com.demo.weibo.common.annotation.UserLoginAnnotation) || @within(com.demo.weibo.common.annotation.UserLoginAnnotation)")
    public void userLogin(){}

    @Around("userLogin()")
    public Object verification(ProceedingJoinPoint joinPoint) throws  Throwable{
        //获得原方法对象
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        //通过原方法获得注解信息
        UserLoginAnnotation userLoginAnnotation = method.getDeclaredAnnotation(UserLoginAnnotation.class);
        //如果方法中没有注解信息，那么从原对象的类中获取
        if (userLoginAnnotation == null){
            Object target = joinPoint.getTarget();
            Class<?> c = target.getClass();
            userLoginAnnotation = c.getAnnotation(UserLoginAnnotation.class);
        }
        //需不需要提前登录
        boolean needLogin = userLoginAnnotation.needLogin();
        //获取原方法的参数
        Object[] args = joinPoint.getArgs();
        if (!(args[0] instanceof HttpServletRequest)){
            throw new Exception("没有按照约定第一个参数是HtpServletRequestt对象");
        }

        HttpServletRequest request = (HttpServletRequest) args[0];
        String token = request.getHeader("Token");
        if (needLogin && StringUtils.hasText(token)){
            Claims data = JwtUtil.verifyJwt(token);
            if (data == null){  //当token无效
                return R.r(2, "您还没有登录");
            }
            User user = (User) redisTemplate.opsForValue().get("loginUser:" + data.get("id"));
            if (user == null){
                return R.r(2, "您的登录状态已过期，请重新登录！");
            }

            //刷新用户的登录状态
            redisTemplate.expire("loginUser:" + data.get("id"), 12, TimeUnit.HOURS);
            //存入request
            request.setAttribute("weiboUser", user);
        }else if (needLogin && !StringUtils.hasText(token)){
            return R.r(2, "您还没有登录！");
        }else if (!needLogin && StringUtils.hasText(token)){
            Claims data = JwtUtil.verifyJwt(token);
            if (data != null) {  //当token有效
                User user = (User) redisTemplate.opsForValue().get("loginUser:" + data.get("id"));
                if (user != null){  //提取到user的登录状态
                    return R.r(3, "无效的请求");
                }
            }
        }
        //执行原方法并且获得结果
        Object result = joinPoint.proceed();
        return result;
    }

}
