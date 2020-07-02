package com.ps.judge.dao.mapper;

import com.ps.common.query.QueryParams;
import com.ps.common.query.TaskQuery;
import com.ps.judge.dao.entity.AuditTaskDO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditTaskMapper {
    @Results(id = "auditTaskMap", value = {
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
        @Result(property = "completeTime", column = "complete_time", jdbcType = JdbcType.TIMESTAMP),
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

    @Update("update audit_task set task_status = #{taskStatus,jdbcType=TINYINT}, gmt_modified = #{gmtModified, jdbcType=TIMESTAMP} where id = #{id,jdbcType=INTEGER}")
    int updateTaskStatus(@Param("taskStatus") int taskStatus, @Param("id") int id, @Param("gmtModified") LocalDateTime gmtModified);

    @Update("UPDATE audit_task SET task_status = #{taskStatus,jdbcType=TINYINT}, audit_code = #{auditCode,jdbcType=VARCHAR},"
            + "callback_count = #{callbackCount,jdbcType=TINYINT}, complete_time = #{completeTime, jdbcType=TIMESTAMP},"
            + "gmt_modified = #{gmtModified, jdbcType=TIMESTAMP} where id=#{id}")
    void update(AuditTaskDO auditTask);

    @Select({"<script>",
            "select * from audit_task",
            "where",
            "1=1",
            "<if test='query!=null'>",
            "<if test='query.taskStatus!=null'>",
            "and",
            "task_status in",
            "<foreach collection='query.taskStatus' item='taskStatus' open='(' separator=',' close=')'>",
            "#{taskStatus}",
            "</foreach>",
            "</if>",

            "<if test='query.gmtCreatedFrom!=null'>",
            "and",
            "gmt_create>=#{query.gmtCreatedFrom}",
            "</if>",

            "<if test='query.gmtCreatedTo!=null'>",
            "and",
            "gmt_create<![CDATA[<=]]>#{query.gmtCreatedTo}",
            "</if>",

            "<if test='query.completeTimeFrom!=null'>",
            "and",
            "complete_time>=#{query.completeTimeFrom}",
            "</if>",

            "<if test='query.completeTimeTo!=null'>",
            "and",
            "complete_time<![CDATA[<=]]>#{query.completeTimeTo}",
            "</if>",

            "<if test='query.fuzzy!=false'>",
            "and ( 1<![CDATA[<>]]>1 ",
            "<if test='query.mobile!=null'>",
            "or mobile like #{query.mobile}",
            "</if>",

            "<if test='query.tenantCode!=null'>",
            "or tenant_code like #{query.tenantCode}",
            "</if>",

            "<if test='query.applyId!=null'>",
            "or apply_id like #{query.applyId}",
            "</if>",

            "<if test='query.idCard!=null'>",
            "or id_card like #{query.idCard}",
            "</if>",
            ")",
            "</if>",

            "</if>",
            "order by gmt_create desc",
            "limit #{startNo},#{pageSize}",
            "</script>"
    })
    @ResultMap(value = "auditTaskMap")
    List<AuditTaskDO> query(QueryParams<TaskQuery> queryParams);

    @Select({"<script>",
            "select count(*) from audit_task",
            "where",
            "1=1",
            "<if test='query!=null'>",
            "<if test='query.taskStatus!=null'>",
            "and",
            "task_status in",
            "<foreach collection='query.taskStatus' item='taskStatus' open='(' separator=',' close=')'>",
            "#{taskStatus}",
            "</foreach>",
            "</if>",

            "<if test='query.gmtCreatedFrom!=null'>",
            "and",
            "gmt_create>=#{query.gmtCreatedFrom}",
            "</if>",

            "<if test='query.gmtCreatedTo!=null'>",
            "and",
            "gmt_create<![CDATA[<=]]>#{query.gmtCreatedTo}",
            "</if>",

            "<if test='query.completeTimeFrom!=null'>",
            "and",
            "complete_time>=#{query.completeTimeFrom}",
            "</if>",

            "<if test='query.completeTimeTo!=null'>",
            "and",
            "complete_time<![CDATA[<=]]>#{query.completeTimeTo}",
            "</if>",

            "<if test='query.fuzzy!=false'>",
            "and ( 1<![CDATA[<>]]>1 ",
            "<if test='query.mobile!=null'>",
            "or mobile like #{query.mobile}",
            "</if>",

            "<if test='query.tenantCode!=null'>",
            "or tenant_code like #{query.tenantCode}",
            "</if>",

            "<if test='query.applyId!=null'>",
            "or apply_id like #{query.applyId}",
            "</if>",

            "<if test='query.idCard!=null'>",
            "or id_card like #{query.idCard}",
            "</if>",
            ")",
            "</if>",

            "</if>",
            "</script>"
    })
    int count(QueryParams<TaskQuery> queryParams);
}
