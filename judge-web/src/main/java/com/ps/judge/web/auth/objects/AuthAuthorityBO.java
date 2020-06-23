package com.ps.judge.web.auth.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 三级权限，一级菜单，二级菜单，按钮/数据层
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthAuthorityBO {
    private Integer id;
    private String displayName;
    private String name;
    private String firstMenu;
    private String secondMenu;
}
