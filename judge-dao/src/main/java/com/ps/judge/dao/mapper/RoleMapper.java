package com.ps.judge.dao.mapper;

import com.ps.common.query.QueryParams;
import com.ps.common.query.QueryVo;
import com.ps.common.query.RoleQuery;
import com.ps.judge.dao.entity.AuthRoleDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface RoleMapper {
    @Select("select * from auth_role")
    @Results(id = "ruleResultMap", value = {
            @Result(column = "gmt_created", property = "gmtCreated"),
            @Result(column = "gmt_modified", property = "gmtModified")
    })
    List<AuthRoleDO> listAll();

    @Select({"<script>",
            "select * from auth_role",
            "where",
            "1=1",
            "<if test='query!=null'>",
            "<if test='query.name!=null'>",
            "and",
            "name=#{query.name}",
            "</if>",
            "</if>",
            "limit #{startNo},#{pageSize}",
            "</script>"
    })
    @ResultMap(value = "ruleResultMap")
    List<AuthRoleDO> query(QueryParams<RoleQuery> queryReq);

    @Select("select * from auth_role where id=#{id}")
    @ResultMap(value = "ruleResultMap")
    AuthRoleDO getById(int id);

    @Insert("insert into auth_role (name, authorities," +
            "operator,gmt_created,gmt_modified)" +
            " values " +
            "(#{name}, #{authorities}, " +
            "#{operator}, #{gmtCreated},#{gmtModified})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(AuthRoleDO authRoleDO);


    @Update("UPDATE auth_role SET name = #{name}, authorities = #{authorities}, operator=#{operator},gmt_modified = #{gmtModified} where id=#{id}")
    void update(AuthRoleDO authRoleDO);

    @Delete("delete from auth_role where id=#{id}")
    void delete(int id);

    @Select({
            "<script>",
            "select * from auth_role",
            "where name in",
            "<foreach collection='names' item='name' open='(' separator=',' close=')'>",
            "#{name}",
            "</foreach>",
            "</script>"
    })
    @ResultMap(value = "ruleResultMap")
    List<AuthRoleDO> queryByNames(@Param("names") String [] names);


    @Select({"<script>",
            "select count(*) from auth_role",
            "where",
            "1=1",
            "<if test='query!=null'>",
            "<if test='query.name!=null'>",
            "and",
            "name=#{query.name}",
            "</if>",
            "</if>",
            "limit #{startNo},#{pageSize}",
            "</script>"
    })
    int total(QueryParams<RoleQuery> query);
}
