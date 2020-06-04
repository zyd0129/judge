package com.ps.judge.dao.entity;

import java.time.LocalDateTime;

/**
 * 三级权限，一级菜单，二级菜单，按钮/数据层
 */
public class AuthAuthorityDO {
    private Integer id;
    private String name;
    private String firstMenu;
    private String secondMenu;

//    private String url;


    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
