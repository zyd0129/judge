package com.ps.judge.web.auth.utils;

import com.alibaba.fastjson.JSONObject;
import com.ps.judge.dao.entity.AuthUserDO;
import com.ps.judge.web.auth.objects.AuthTenantBO;
import com.ps.judge.web.auth.objects.AuthUserBO;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BOUtitls {
    public static AuthUserDO convertToDO(AuthUserBO authUserBO) {
        if (authUserBO == null) {
            return null;
        }
        AuthUserDO authUserDO = new AuthUserDO();
        BeanUtils.copyProperties(authUserBO, authUserDO);
        if (authUserBO.getTenants() != null) {
            authUserDO.setTenants(JSONObject.toJSONString(authUserBO.getTenants()));
        }
        return authUserDO;
    }

    public static AuthUserBO convertToBO(AuthUserDO authUserDO) {
        if (authUserDO == null) {
            return null;
        }
        AuthUserBO authUserBO = new AuthUserBO();
        BeanUtils.copyProperties(authUserDO, authUserBO);
        if (!StringUtils.isEmpty(authUserDO.getTenants())) {
            authUserBO.setTenants(JSONObject.parseArray(authUserDO.getTenants(), AuthTenantBO.class));
        }
        return authUserBO;
    }

    public static List<AuthUserBO> convertToBOs(List<AuthUserDO> authUserDOList) {
        if (authUserDOList == null) {
            return null;
        }

        return authUserDOList.stream().map(BOUtitls::convertToBO).collect(Collectors.toList());
    }
}
