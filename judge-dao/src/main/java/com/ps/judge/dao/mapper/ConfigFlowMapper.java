package com.ps.judge.dao.mapper;

import com.ps.judge.dao.entity.ConfigFlowDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ConfigFlowMapper {
    @Select("select * from config_flow where flow_code=#{flowCode}")
    @Results(id = "flowResultMap", value = {
            @Result(column = "flow_code", property = "flowCode"),
            @Result(column = "flow_name", property = "flowName"),
            @Result(column = "tenant_code", property = "tenantCode"),
            @Result(column = "product_code", property = "productCode"),
            @Result(column = "gmt_created", property = "gmtCreated"),
            @Result(column = "gmt_modified", property = "gmtModified"),
            @Result(column = "cur_package_version", property = "curPackageVersion"),
            @Result(column = "cur_package_artifact", property = "curPackageArtifact"),
            @Result(column = "cur_package_group", property = "curPackageGroup"),
            @Result(column = "cur_package_id", property = "curPackageId")
    })
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


    @Insert("insert into config_flow (flow_code, flow_name,tenant_code,product_code," +
            "cur_package_id,cur_package_group,cur_package_artifact,cur_package_version,status,operator,gmt_created,gmt_modified)" +
            " values " +
            "(#{flowCode}, #{flowName},#{tenantCode}, #{productCode}, " +
            "#{curPackageId}, #{curPackageGroup}, #{curPackageArtifact}, #{curPackageVersion}, #{status},#{operator}, #{gmtCreated},#{gmtModified})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(ConfigFlowDO configFlowDO);

    @Update("UPDATE config_flow SET status = #{status},operator=#{operator}, gmt_modified=#{gmtModified} where id=#{id}")
    void updateStatus(ConfigFlowDO configFlowDO);

    @Update("UPDATE config_flow SET cur_package_id = #{curPackageId},cur_package_group=#{curPackageGroup}, " +
            "cur_package_artifact=#{curPackageArtifact},cur_package_version=#{curPackageVersion}," +
            "operator=#{operator},gmt_modified=#{gmtModified} where id=#{id}")
    void changePackage(ConfigFlowDO configFlowDO);
}
