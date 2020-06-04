package com.ps.judge.web.models;

import java.time.LocalDateTime;

/**
 * 三级权限，一级菜单，二级菜单，按钮/数据层
 */
public class AuthAuthorityBO {
    private Integer id;
    private String name;
    private String firstMenu;
    private String secondMenu;

//    private String url;


    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
