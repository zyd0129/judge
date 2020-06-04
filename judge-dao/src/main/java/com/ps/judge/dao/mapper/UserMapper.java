package com.ps.judge.dao.mapper;

import com.ps.judge.dao.entity.AuthUserDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {
    @Select("select * from auth_user where username=#{username}")
    @Results(id = "userResultMap", value = {
            @Result(column = "credentials_expired", property = "credentialsExpired"),
            @Result(column = "gmt_created", property = "gmtCreated"),
            @Result(column = "gmt_modified", property = "gmtModified")
    })
    AuthUserDO getByUsername(String username);

    @Select("select * from auth_user")
    @ResultMap(value = "userResultMap")
    List<AuthUserDO> queryAll();

    @Select("select * from auth_user where department=null")
    @ResultMap(value = "userResultMap")
    List<AuthUserDO> queryNullDepartment();

    @Select("select * from auth_user where department")
    @ResultMap(value = "userResultMap")
    List<AuthUserDO> queryByDepartment(String departmentName);


    @Insert("insert into auth_user (username,name, password,roles," +
            "mobile, tenants,department," +
            "operator,gmt_created,gmt_modified)" +
            " values " +
            "(#{username},#{name}, #{password},#{roles}, " +
            "#{mobile}, #{tenants},#{department}," +
            "#{operator}, #{gmtCreated},#{gmtModified})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(AuthUserDO authUserDO);

    @Update("UPDATE auth_user SET username = #{username},name = #{name},roles = #{roles}, operator = #{operator}, gmt_modified = #{gmtModified} where id=#{id}")
    void update(AuthUserDO authUserDO);

    @Update("UPDATE auth_user SET password = #{password}, operator = #{operator}, gmt_modified = #{gmtModified} where id=#{id}")
    void changePassword(AuthUserDO authUserDO);

    @Delete("delete from auth_user where id=#{id}")
    void delete(String id);
}
