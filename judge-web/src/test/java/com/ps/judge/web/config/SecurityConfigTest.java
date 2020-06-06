package com.ps.judge.web.config;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.util.Arrays;

import static org.junit.Assert.*;

public class SecurityConfigTest {

    @Test
    public void password() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("admin"));
    }

    @Test
    public void jwtTest(){
//        JwtHelper.encode()

        String key_location = "xc.keystore";
        //密钥库密码
        String keystore_password = "admin123";
        //访问证书路径
        ClassPathResource resource = new ClassPathResource(key_location);
        //密钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource, keystore_password.toCharArray());
        //密钥的密码，此密码和别名要匹配
        String keypassword = "admin123";
        //密钥别名
        String alias = "ps";
        //密钥对（密钥和公钥）
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias,keypassword.toCharArray());
        //私钥
        System.out.println(keyPair.getPublic().getAlgorithm());

    }
}