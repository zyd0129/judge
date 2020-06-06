package com.ps.judge.web.auth.utils;

import org.junit.Test;
import org.springframework.security.jwt.JwtHelper;

import static org.junit.Assert.*;

public class JWTHelperTest {

    @Test
    public void jwtTest() {
        System.out.println(JWTHelper.generateToken("hello world", 1000000));
        String jwtToken = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJoZWxsbyB3b3JsZCIsImV4cCI6MTU5MjQyNTgxNH0.d53e_VPR7IaBZcI4bm_qHKWYff0bcDhVS1zJHbRB1YQSl99rzDvfrEHZkcsuuc7mHri1wYtpXaBYLw7LYOIBGv8sDk7F9XqkEZBij9l5xaL8MgdWVOfXNbmwpurApMAUqoyK2Yd7qvm4np5ezny1dF73HwZuxUlAZnnyjPka7XULzmtd8lyb_6U4epWP3wWhOuTo9HWgGycCBgghGeubakE8syXf607QPJD07EZGrj71x3MJc6YZh4B3kN_d4koi-r4WitNzFyI_HlbQ76Ges-Hhe-OjzqAWwPm0muS5CAUVhAe2O1FXp2_rHzwnbgZ8j0eNl_7o1JUZNI4IgeeLlw";

        System.out.println(JWTHelper.parseToken(jwtToken));

    }

}