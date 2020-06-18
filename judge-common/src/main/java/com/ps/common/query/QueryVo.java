package com.ps.common.query;

import lombok.Data;

@Data
public class QueryVo<T> {
    private T query;
    private int curPage;
    private int pageSize;

    public QueryParams<T> convertToQueryParam() {
        QueryParams<T> queryParams = new QueryParams<>();
        if (query!=null && query instanceof QueryConver) {
            ((QueryConver) query).convert();
        }
        queryParams.setQuery(query);
        queryParams.setPageSize(pageSize);
        curPage = Math.max(curPage, 1);
        pageSize = Math.min(pageSize, 10);
        queryParams.setStartNo((curPage - 1) * pageSize);
        return queryParams;
    }
}
