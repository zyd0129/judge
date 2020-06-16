package com.ps.judge.web.auth.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FirstMenu {
    private String displayName;
    private List<SecondMenu> children=new ArrayList<>();

    public FirstMenu(String displayName) {
        this.displayName = displayName;
    }
}