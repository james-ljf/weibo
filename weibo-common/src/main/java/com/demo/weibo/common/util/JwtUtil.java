package com.demo.weibo.common.util;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.demo.weibo.common.util.id.IdGenerator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * 生成Token和校验Token的工具类
 */
public class JwtUtil {

    /**
     * token 过期时间，这个值表示 30 天
     */
    private static final long TOKEN_EXPIRED_TIME = 1000 * 60 * 60 * 24L;

    /**
     * 签发ID
     */
    public static final String ISSUE_ID = "WEIBO:1830143010-sjxy/admin";

    /**
     * 加密/解密 密钥(自己都不知道是多少)
     */
    private static final String JWT_SECRET = "b17f24ff026d40949c85a24f4f375d42";

    /**
     * 创建JWT
     */
    public static String createJWT(Map<String, Object> claims, Long time) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; //指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        Date now = new Date(System.currentTimeMillis());

        long nowMillis = System.currentTimeMillis();
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setId(ISSUE_ID)
                .setIssuedAt(now)
                .signWith(signatureAlgorithm, JWT_SECRET);

        if (time >= 0) {
            long expMillis = nowMillis + time;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }


    /**
     * 验证Token，如果返回null说明token无效或过期
     *
     * @param token token令牌
     * @return
     */
    public static Claims verifyJwt(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 生成token
     *
     * @param data 载荷
     * @return
     */
    public static String generateToken(Map<String, Object> data) {
        return createJWT(data, TOKEN_EXPIRED_TIME);
    }

}
