package com.ps.judge.dao.mapper;

import com.ps.judge.dao.entity.AuthDepartmentDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface DepartmentMapper {
    @Select("select * from auth_department limit #{startNo},#{pageSize}")
    @Results(id = "departmentResultMap", value = {
            @Result(column = "gmt_created", property = "gmtCreated"),
            @Result(column = "gmt_modified", property = "gmtModified")
    })
    List<AuthDepartmentDO> queryAll(QueryReq q);

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
    void delete(String id);
}
