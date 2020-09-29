package com.ts.judge.provider.dao.mapper;

import com.ts.judge.provider.dao.entity.ProcessDefinitionDO;
import org.apache.ibatis.annotations.Param;

public interface ProcessDefinitionMapper {
    ProcessDefinitionDO getByCode(@Param("code") String code);
}
