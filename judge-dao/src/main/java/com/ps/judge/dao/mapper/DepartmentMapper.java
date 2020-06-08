package com.ps.judge.dao.mapper;

import com.ps.common.query.DepartmentQuery;
import com.ps.common.query.QueryParams;
import com.ps.judge.dao.entity.AuthDepartmentDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface DepartmentMapper {
    @Select({"<script>",
            "select * from auth_department",
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
    @Results(id = "departmentResultMap", value = {
            @Result(column = "gmt_created", property = "gmtCreated"),
            @Result(column = "gmt_modified", property = "gmtModified")
    })
    List<AuthDepartmentDO> query(QueryParams<DepartmentQuery> q);

    @Select("select * from auth_department where id=#{id}")
    @ResultMap(value = "departmentResultMap")
    AuthDepartmentDO getById(int id);

    @Insert("insert into auth_department (name, pic,members," +
            "operator,gmt_created,gmt_modified)" +
            " values " +
            "(#{name}, #{pic},#{members}, " +
            "#{operator}, #{gmtCreated},#{gmtModified})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(AuthDepartmentDO authDepartmentDO);


    @Update("UPDATE auth_department SET name = #{name}, pic = #{pic},members=#{members}, operator=#{operator},gmt_modified = #{gmtModified} where id=#{id}")
    void update(AuthDepartmentDO authDepartmentDO);

    @Delete("delete from auth_department where id=#{id}")
    void delete(int id);

    @Select({"<script>",
            "select count(*) from auth_department",
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
    int count(QueryParams<DepartmentQuery> q);
}
