package com.ps.judge.web.util;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PageUtils {
    public static <Do, Bo> PageInfo<Bo> convertPage(Page<Do> pageDO, Function<Do,Bo> convert) {
        PageInfo<Do> pageInfoDO = new PageInfo<>(pageDO);
        PageInfo<Bo> pageInfoBO = new PageInfo<>();
        BeanUtils.copyProperties(pageInfoDO, pageInfoBO);
        List<Bo> bos = pageInfoDO.getList().stream().map(convert).collect(Collectors.toList());
        pageInfoBO.setList(bos);
        return pageInfoBO;
    }
}
