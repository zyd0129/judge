package com.ps.judge.web.auth.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FirstMenu {
    private String name;
    private List<SecondMenu> children=new ArrayList<>();

    public FirstMenu(String name) {
        this.name = name;
    }
}