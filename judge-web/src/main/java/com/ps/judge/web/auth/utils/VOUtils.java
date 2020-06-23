package com.ps.judge.web.auth.utils;

import com.ps.judge.web.auth.objects.AuthAuthorityBO;
import com.ps.judge.web.auth.objects.AuthRoleBO;
import com.ps.judge.web.auth.objects.AuthUserBO;
import com.ps.judge.web.auth.req.AuthUserChangePassReq;
import com.ps.judge.web.auth.req.AuthUserModifyReq;
import com.ps.judge.web.auth.vo.AuthRoleVO;
import com.ps.judge.web.auth.vo.AuthUserVO;
import com.ps.judge.web.auth.vo.FirstMenu;
import com.ps.judge.web.auth.vo.SecondMenu;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VOUtils {
    public static List<FirstMenu> convertToMenuTree(List<AuthAuthorityBO> authAuthorityBOList) {
        if (authAuthorityBOList == null) {
            return null;
        }
        List<FirstMenu> firstMenus = new ArrayList<>();


        for (AuthAuthorityBO authAuthorityBO : authAuthorityBOList) {
            String firstMenuName = authAuthorityBO.getFirstMenu();
            String secondMenuName = authAuthorityBO.getSecondMenu();

            FirstMenu firstMenu = null;

            for (FirstMenu temp : firstMenus) {
                if (temp.getDisplayName().equals(firstMenuName)) {
                    firstMenu = temp;
                    break;
                }
            }
            if (firstMenu == null) {
                firstMenu = new FirstMenu(firstMenuName);
                firstMenus.add(firstMenu);
            }

            List<SecondMenu> secondMenus = firstMenu.getChildren();
            SecondMenu secondMenu = null;

            for (SecondMenu temp : secondMenus) {
                if (temp.getDisplayName().equals(secondMenuName)) {
                    secondMenu = temp;
                    break;
                }
            }
            if (secondMenu == null) {
                secondMenu = new SecondMenu(secondMenuName);
                secondMenus.add(secondMenu);
            }
            secondMenu.getChildren().add(authAuthorityBO);

        }

        return firstMenus;
    }

    public static AuthRoleVO convertToAuthRoleVO(AuthRoleBO authRoleBO) {
        if (authRoleBO == null) {
            return null;
        }
        AuthRoleVO authRoleVO = new AuthRoleVO();
        BeanUtils.copyProperties(authRoleBO, authRoleVO);
        if (authRoleBO.getAuthorities() != null) {
            authRoleVO.setAuthorities(convertToMenuTree(authRoleBO.getAuthorities()));
        }
        return authRoleVO;
    }

    public static List<AuthRoleVO> convertToAuthRoleVOs(List<AuthRoleBO> authRoleBOList) {
        if (authRoleBOList == null) {
            return null;
        }
        return authRoleBOList.stream().map(VOUtils::convertToAuthRoleVO).collect(Collectors.toList());
    }

    public static AuthUserVO convertToAuthUserVO(AuthUserBO authUserBO) {
        if (authUserBO == null) {
            return null;
        }
        AuthUserVO authUserVO = new AuthUserVO();
        BeanUtils.copyProperties(authUserBO, authUserVO);
        return authUserVO;
    }

    public static List<AuthUserVO> convertToAuthUserVOs(List<AuthUserBO> authUserBOList) {
        if (authUserBOList == null) {
            return null;
        }
        return authUserBOList.stream().map(VOUtils::convertToAuthUserVO).collect(Collectors.toList());
    }

    public static AuthUserBO convertToAuthUserBO(AuthUserVO authUserVO) {
        if (authUserVO == null) {
            return null;
        }
        AuthUserBO authUserBO = new AuthUserBO();
        BeanUtils.copyProperties(authUserVO, authUserBO);
        return authUserBO;
    }

    public static AuthUserBO convertToAuthUserBO(AuthUserModifyReq authUserVO) {
        if (authUserVO == null) {
            return null;
        }
        AuthUserBO authUserBO = new AuthUserBO();
        BeanUtils.copyProperties(authUserVO, authUserBO);
        return authUserBO;
    }

    public static AuthUserBO convertToAuthUserBO(AuthUserChangePassReq authUserVO) {
        if (authUserVO == null) {
            return null;
        }
        AuthUserBO authUserBO = new AuthUserBO();
        BeanUtils.copyProperties(authUserVO, authUserBO);
        return authUserBO;
    }
}