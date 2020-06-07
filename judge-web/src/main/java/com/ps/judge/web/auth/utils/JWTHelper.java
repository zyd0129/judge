package com.ps.judge.web.auth.utils;

import com.alibaba.fastjson.JSONObject;
import com.ps.common.exception.BizException;
import com.ps.judge.web.auth.objects.AuthUserBO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

public class JWTHelper {

    /**
     * 使用私钥加密 token
     */
    public static String generateToken(AuthUserBO authUserBO, PrivateKey privateKey, int expirationSeconds) {
        return Jwts.builder()
                .setSubject(JSONObject.toJSONString(authUserBO))
                .setExpiration(new Date(System.currentTimeMillis() + expirationSeconds * 1000))
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    /**
     * 通过 公钥解密token
     *
     * @return
     */
    public static AuthUserBO parseToken(String token, PublicKey publicKey) {
        AuthUserBO authUserBO = null;

        Claims claims = Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(token).getBody();
        String subject = claims.getSubject();
        authUserBO = JSONObject.parseObject(subject, AuthUserBO.class);
        return authUserBO;
    }
}