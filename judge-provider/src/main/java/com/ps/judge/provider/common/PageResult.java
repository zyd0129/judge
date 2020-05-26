package com.ps.judge.provider.common;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    private int curPage;
    private int pageSize;

    private List<T> data;
}
