package com.ps.judge.web.auth.utils;

import com.ps.judge.web.auth.objects.AuthAuthorityBO;
import com.ps.judge.web.auth.vo.FirstMenu;
import com.ps.judge.web.auth.vo.SecondMenu;

import java.util.ArrayList;
import java.util.List;

public class AuthorityVOUtils {
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
                if (temp.getName().equals(firstMenuName)) {
                    firstMenu = temp;
                    break;
                }
            }
            if (firstMenu == null) {
                firstMenu = new FirstMenu(firstMenuName);
                firstMenus.add(firstMenu);
            }

            List<SecondMenu> secondMenus = firstMenu.getSecondMenus();
            SecondMenu secondMenu = null;

            for (SecondMenu temp : secondMenus) {
                if (temp.getName().equals(secondMenuName)) {
                    secondMenu = temp;
                    break;
                }
            }
            if (secondMenu == null) {
                secondMenu = new SecondMenu(secondMenuName);
                secondMenus.add(secondMenu);
            }
            secondMenu.getThirdMenus().add(authAuthorityBO);

        }

        return firstMenus;
    }
}