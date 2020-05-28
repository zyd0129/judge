package com.ps.judge.dao.mapper;

import com.ps.judge.dao.entity.AuditTaskTriggeredRuleDO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface AuditTaskTriggeredRuleMapper {
    @Results(id="auditTask",value={
            @Result(property = "id", column = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(property = "tenantCode", column = "tenant_code", jdbcType = JdbcType.VARCHAR),
            @Result(property = "taskId", column = "task_id", jdbcType = JdbcType.INTEGER),
            @Result(property = "applyId", column = "apply_id", jdbcType = JdbcType.VARCHAR),
            @Result(property = "auditFlow", column = "audit_flow", jdbcType = JdbcType.VARCHAR),
            @Result(property = "rulePackageCode", column = "rule_package_code", jdbcType = JdbcType.VARCHAR),
            @Result(property = "rulePackageName", column = "rule_package_name", jdbcType = JdbcType.VARCHAR),
            @Result(property = "rulePackageVersion", column = "rule_package_version", jdbcType = JdbcType.VARCHAR),
            @Result(property = "ruleCode", column = "rule_code", jdbcType = JdbcType.VARCHAR),
            @Result(property = "ruleName", column = "rule_name", jdbcType = JdbcType.VARCHAR),
            @Result(property = "ruleVersion", column = "rule_version", jdbcType = JdbcType.VARCHAR),
            @Result(property = "expression", column = "expression", jdbcType = JdbcType.VARCHAR),
            @Result(property = "param", column = "param", jdbcType = JdbcType.VARCHAR),
            @Result(property = "gmtCreate", column = "gmt_create", jdbcType = JdbcType.TIMESTAMP)
    })

    @Select("select * from audit_task_triggered_rule where tenant_code = #{tenantCode,jdbcType=VARCHAR} and apply_id = #{applyId,jdbcType=VARCHAR}")
    List<AuditTaskTriggeredRuleDO> listTriggeredRuleLog(@Param("tenantCode") String tenantCode, @Param("applyId") String applyId);

    @Insert("INSERT INTO audit_task_triggered_rule (tenant_code, task_id, apply_id, audit_flow, rule_package_code,"
            + "rule_package_name, rule_package_version, rule_code, rule_name , rule_version, expression, param , gmt_create)"
            + "VALUES(#{tenantCode}, #{taskId}, #{applyId}, #{auditFlow}, #{rulePackageCode}, #{rulePackageName}, "
            + "#{rulePackageVersion}), #{ruleCode}, #{ruleName}, #{ruleVersion}, #{expression}, #{param}, #{gmtCreate}")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AuditTaskTriggeredRuleDO auditTaskTriggeredRule);
}
