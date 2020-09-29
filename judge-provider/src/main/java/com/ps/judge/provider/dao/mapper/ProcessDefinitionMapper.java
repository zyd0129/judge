package com.ps.judge.provider.dao.mapper;

import com.ps.judge.provider.dao.entity.ProcessDefinitionDO;
import org.apache.ibatis.annotations.Select;

public interface ProcessDefinitionMapper {
    ProcessDefinitionDO getByCode(String processCode);
}
