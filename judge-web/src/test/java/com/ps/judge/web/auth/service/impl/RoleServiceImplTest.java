package com.ps.judge.web.auth.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ps.judge.web.auth.objects.AuthAuthorityBO;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoleServiceImplTest {

    @Test
    public void testArray() {
        String[] a = {};
        List<String> b = Arrays.asList(a);

        System.out.println(b);

    }

    @Test
    public void testAuthorities() {
        List<AuthAuthorityBO> authAuthorityBOList = new ArrayList<>();

        AuthAuthorityBO authAuthorityBO = new AuthAuthorityBO(1, "增加", "department_modify", "系统设置", "权限设置");
        AuthAuthorityBO authAuthorityBO2 = new AuthAuthorityBO(2, "查看", "department_add", "系统设置", "权限设置");
        AuthAuthorityBO authAuthorityBO3 = new AuthAuthorityBO(2, "查看", "department_list", "系统设置", "权限设置");

        authAuthorityBOList.add(authAuthorityBO);
        authAuthorityBOList.add(authAuthorityBO2);
        authAuthorityBOList.add(authAuthorityBO3);

        System.out.println(JSONObject.toJSON(authAuthorityBOList));
    }

}