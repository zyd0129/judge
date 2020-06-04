package com.ps.judge.dao.mapper;

import com.ps.judge.dao.entity.AuditTaskDO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface AuditTaskMapper {
    @Results(id = "auditTask", value = {
        @Result(property = "id", column = "id", jdbcType = JdbcType.INTEGER, id = true),
        @Result(property = "tenantCode", column = "tenant_code", jdbcType = JdbcType.VARCHAR),
        @Result(property = "productCode", column = "product_code", jdbcType = JdbcType.VARCHAR),
        @Result(property = "flowCode", column = "flow_code", jdbcType = JdbcType.VARCHAR),
        @Result(property = "applyId", column = "apply_id", jdbcType = JdbcType.VARCHAR),
        @Result(property = "userId", column = "user_id", jdbcType = JdbcType.VARCHAR),
        @Result(property = "userName", column = "user_name", jdbcType = JdbcType.VARCHAR),
        @Result(property = "mobile", column = "mobile", jdbcType = JdbcType.VARCHAR),
        @Result(property = "idCard", column = "id_card", jdbcType = JdbcType.VARCHAR),
        @Result(property = "orderId", column = "order_id", jdbcType = JdbcType.VARCHAR),
        @Result(property = "ip", column = "ip", jdbcType = JdbcType.VARCHAR),
        @Result(property = "deviceFingerPrint", column = "device_finger_print", jdbcType = JdbcType.VARCHAR),
        @Result(property = "transactionTime", column = "transaction_time", jdbcType = JdbcType.TIMESTAMP),
        @Result(property = "taskStatus", column = "task_status", jdbcType = JdbcType.TINYINT),
        @Result(property = "auditScore", column = "audit_score", jdbcType = JdbcType.SMALLINT),
        @Result(property = "auditCode", column = "audit_code", jdbcType = JdbcType.VARCHAR),
        @Result(property = "callbackUrl", column = "callback_url", jdbcType = JdbcType.VARCHAR),
        @Result(property = "callbackCount", column = "callback_count", jdbcType = JdbcType.TINYINT),
        @Result(property = "gmtCreate", column = "gmt_create", jdbcType = JdbcType.TIMESTAMP),
        @Result(property = "gmtModified", column = "gmt_modified", jdbcType = JdbcType.TIMESTAMP)
    })

    @Select("select * from audit_task where id = #{id,jdbcType=INTEGER}")
    AuditTaskDO getAuditTaskById(@Param("id") int id);

    @Select("select * from audit_task where tenant_code = #{tenantCode,jdbcType=VARCHAR} and apply_id = #{applyId,jdbcType=VARCHAR}")
    AuditTaskDO getAuditTask(@Param("tenantCode") String tenantCode, @Param("applyId") String applyId);

    @Select("select * from audit_task where task_status = #{taskStatus, jdbcType=TINYINT}")
    List<AuditTaskDO> listAuditTaskByTaskStatus(@Param("taskStatus") int taskStatus);

    @Insert("INSERT INTO audit_task (tenant_code, product_code, flow_code, apply_id, user_id, user_name, mobile, id_card,"
            + "order_id, ip, device_finger_print, transaction_time, task_status, callback_url, gmt_create, gmt_modified)"
            + "VALUES(#{tenantCode}, #{productCode},  #{flowCode}, #{applyId}, #{userId}, #{userName}, #{mobile}, #{idCard}, #{orderId},"
            + "#{ip}, #{deviceFingerPrint}, #{transactionTime}, #{taskStatus}, #{callbackUrl}, #{gmtCreate}, #{gmtModified})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AuditTaskDO auditTask);

    @Update("update audit_task set task_status = #{taskStatus,jdbcType=TINYINT}, gmt_modified = now() where id = #{id,jdbcType=INTEGER}")
    int updateTaskStatus(@Param("taskStatus") int taskStatus, @Param("id") int id);

    @Update("update audit_task set audit_code = #{auditCode,jdbcType=VARCHAR}, gmt_modified = now() where id = #{id,jdbcType=INTEGER}")
    int updateAuditCode(@Param("auditCode") String auditCode, @Param("id") int id);

    @Update("update audit_task set callback_count = #{callbackCount,jdbcType=TINYINT}, gmt_modified = now() where id = #{id,jdbcType=INTEGER}")
    int updateCallbackCount(@Param("callbackCount") int callbackCount, @Param("id") int id);
}
