package com.ps.judge.web.controller.rule;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ps.common.ApiResponse;
import com.ps.common.PageResult;
import com.ps.common.query.QueryVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

public abstract class BaseQueryController<T,W> {

    @RequestMapping("query")
    public ApiResponse<PageResult<T>> query(@RequestBody QueryVo<W> queryVo) {
        PageHelper.startPage(queryVo.getCurPage(), queryVo.getPageSize());

        W variableQuery = queryVo.getQuery();
        List<T> data = doQuery(variableQuery);

        PageInfo<T> page = new PageInfo<>(data);
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setCurPage(page.getPageNum());
        pageResult.setPageSize(page.getSize());
        pageResult.setTotal((int) page.getTotal());
        pageResult.setData(data);
        return ApiResponse.success(pageResult);
    }

    abstract public List<T> doQuery(W w);
}
