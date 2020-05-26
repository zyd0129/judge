package com.ps.judge.provider.common;

import lombok.Data;

@Data
public class PageResult<T> {
    private int curPage;
    private int pageSize;

    private T data;
}
