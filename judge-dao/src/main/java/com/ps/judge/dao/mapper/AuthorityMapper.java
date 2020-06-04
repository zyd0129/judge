package com.ps.judge.dao.mapper;

import com.ps.judge.dao.entity.AuthAuthorityDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AuthorityMapper {
    @Select("select * from auth_authority")
    @Results(id = "departmentResultMap", value = {
            @Result(column = "display_name", property = "displayName"),
            @Result(column = "gmt_created", property = "gmtCreated"),
            @Result(column = "gmt_modified", property = "gmtModified")
    })
    List<AuthAuthorityDO> queryAll();
}
