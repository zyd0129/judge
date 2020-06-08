package com.ps.judge.dao.mapper;

import com.ps.judge.dao.entity.ConfigFlowDO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface ConfigFlowMapper {

    @Results(id = "flowResultMap", value = {
            @Result(property = "id", column = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "flow_code", property = "flowCode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "flow_name", property = "flowName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tenant_code", property = "tenantCode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "product_code", property = "productCode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "package_id", property = "packageId", jdbcType = JdbcType.INTEGER),
            @Result(column = "package_url", property = "packageUrl", jdbcType = JdbcType.VARCHAR),
            @Result(column = "status", property = "status", jdbcType = JdbcType.INTEGER),
            @Result(column = "operator", property = "operator", jdbcType = JdbcType.TINYINT),
            @Result(column = "gmt_created", property = "gmtCreated", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "gmt_modified", property = "gmtModified", jdbcType = JdbcType.TIMESTAMP)
    })

    @Select("select * from config_flow where flow_code=#{flowCode}")
    ConfigFlowDO getByFlowCode(String flowCode);

    @Select("select * from config_flow where id=#{id}")
    @ResultMap(value = "flowResultMap")
    ConfigFlowDO getById(int id);

    @Select("select * from config_flow order by id limit #{from},#{size}")
    @ResultMap(value = "flowResultMap")
    List<ConfigFlowDO> query(int from, int size);

    @Select("select * from config_flow")
    @ResultMap(value = "flowResultMap")
    List<ConfigFlowDO> getAll();

    @Select("select * from config_flow where status = 1")
    @ResultMap(value = "flowResultMap")
    List<ConfigFlowDO> getAllEnable();

    @Insert("insert into config_flow (flow_code, flow_name, tenant_code, product_code,"
            + "package_id, package_url, status, operator, gmt_created, gmt_modified)"
            + "values (#{flowCode}, #{flowName},#{tenantCode}, #{productCode}, #{packageId},"
            + "#{packageUrl}, #{status}, #{operator}, #{gmtCreated}, #{gmtModified})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(ConfigFlowDO configFlowDO);

    @Update("UPDATE config_flow SET status = #{status}, operator = #{operator}, gmt_modified = #{gmtModified} where id=#{id}")
    void updateStatus(ConfigFlowDO configFlowDO);

    @Update("UPDATE config_flow SET package_id = #{packageId},package_url=#{packageUrl},"
            + "operator = #{operator}, gmt_modified = #{gmtModified} where id = #{id}")
    void changePackage(ConfigFlowDO configFlowDO);

    @Update("UPDATE config_flow SET status = #{status}, operator = #{operator}, gmt_modified = #{gmtModified} where product_code=#{productCode}")
    void batchUpdateStatus(ConfigFlowDO configFlowDO);
}
