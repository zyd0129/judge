package com.ps.judge.web.auth.vo;

import com.ps.judge.web.auth.objects.AuthAuthorityBO;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Data
public class SecondMenu{
    private String displayName;
    private Set<AuthAuthorityBO> children=new HashSet<>();

    public SecondMenu(String displayName) {
        this.displayName = displayName;
    }
}
