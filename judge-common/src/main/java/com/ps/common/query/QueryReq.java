package com.ps.common.query;

import lombok.Data;

@Data
public class QueryReq<T> {
    private T domain;
    private int curPage;
    private int pageSize = 10;
}
