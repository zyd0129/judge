package com.ps.judge.web.auth.vo;

import com.ps.judge.web.auth.objects.AuthAuthorityBO;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Data
public class SecondMenu{
    private String name;
    private Set<AuthAuthorityBO> thirdMenus=new HashSet<>();

    public SecondMenu(String name) {
        this.name = name;
    }
}
