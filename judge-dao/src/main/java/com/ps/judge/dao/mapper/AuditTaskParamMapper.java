package com.ps.judge.dao.mapper;

import com.ps.judge.dao.entity.AuditTaskParamDO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface AuditTaskParamMapper {
    @Results(id = "auditTaskParam", value={
            @Result(property = "id", column = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(property = "tenantCode", column = "tenant_code", jdbcType = JdbcType.VARCHAR),
            @Result(property = "taskId", column = "task_id", jdbcType = JdbcType.INTEGER),
            @Result(property = "applyId", column = "apply_id", jdbcType = JdbcType.VARCHAR),
            @Result(property = "inputRawParam", column = "input_raw_param", jdbcType = JdbcType.VARCHAR),
            @Result(property = "outputRawParam", column = "output_raw_param", jdbcType = JdbcType.VARCHAR),
            @Result(property = "gmtCreate", column = "gmt_create", jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "gmtModified", column = "gmt_modified", jdbcType = JdbcType.TIMESTAMP)
    })

    @Select("select * from audit_task_param where tenant_code = #{tenantCode,jdbcType=VARCHAR} and apply_id = #{applyId,jdbcType=VARCHAR}")
    AuditTaskParamDO getAuditTaskParam(@Param("tenantCode") String tenantCode, @Param("applyId") String applyId);

    @Insert("INSERT INTO audit_task_param (tenant_code, task_id, apply_id, input_raw_param, output_raw_param, gmt_create, gmt_modified)"
            + "VALUES(#{tenantCode}, #{taskId}, #{applyId}, #{inputRawParam}, #{outputRawParam}, #{gmtCreate}, #{gmtModified})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AuditTaskParamDO auditTaskParam);

    @Update("update audit_task_param set output_raw_param = #{outputRawParam,jdbcType=VARCHAR} where tenant_code = #{tenantCode,jdbcType=VARCHAR} and apply_id = #{applyId,jdbcType=VARCHAR}")
    int updateOutputRawParam(@Param("outputRawParam") String outputRawParam, @Param("tenantCode") String tenantCode, @Param("applyId") String applyId);

}
