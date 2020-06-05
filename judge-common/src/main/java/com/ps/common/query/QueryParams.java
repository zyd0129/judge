package com.ps.common.query;

import lombok.Data;

@Data
public class QueryParams<T> {
    private T query;
    private int startNo;
    private int pageSize;
}
