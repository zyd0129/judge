package com.ps.judge.dao.mapper;

import com.ps.common.ApiResponse;
import com.ps.common.query.FlowQuery;
import com.ps.common.query.QueryParams;
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
            @Result(column = "package_name", property = "packageName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "package_version", property = "packageVersion", jdbcType = JdbcType.VARCHAR),
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

    @Select({"<script>",
            "select * from config_flow",
            "where",
            "1=1",
            "<if test='query!=null'>",
            "<if test='query.status!=null'>",
            "and",
            "status=#{query.status}",
            "</if>",

            "<if test='query.gmtCreatedFrom!=null'>",
            "and",
            "gmt_created>=#{query.gmtCreatedFrom}",
            "</if>",

            "<if test='query.gmtCreatedTo!=null'>",
            "and",
            "gmt_created<![CDATA[<=]]>#{query.gmtCreatedTo}",
            "</if>",

            "<if test='query.tenantCode!=null'>",
            "and tenant_code=#{query.tenantCode}",
            "</if>",

            "<if test='query.productCode!=null'>",
            "and product_code=#{query.productCode}",
            "</if>",

            "and ( 1<![CDATA[<>]]>1 ",
            "<if test='query.flowName!=null'>",
            "or flow_name like #{query.flowName}",
            "</if>",

            "<if test='query.packageName!=null'>",
            "or package_name like #{query.packageName}",
            "</if>",
            ")",

            "</if>",
            "limit #{startNo},#{pageSize}",
            "</script>"
    })
    @ResultMap(value = "flowResultMap")
    List<ConfigFlowDO> query(QueryParams<FlowQuery> queryParams);

    @Select({"<script>",
            "select count(*) from config_flow",
            "where",
            "1=1",
            "<if test='query!=null'>",
            "<if test='query.status!=null'>",
            "and",
            "status=#{query.status}",
            "</if>",

            "<if test='query.gmtCreatedFrom!=null'>",
            "and",
            "gmt_created>=#{query.gmtCreatedFrom}",
            "</if>",

            "<if test='query.gmtCreatedTo!=null'>",
            "and",
            "gmt_created<![CDATA[<=]]>#{query.gmtCreatedTo}",
            "</if>",

            "<if test='query.tenantCode!=null'>",
            "and tenant_code=#{query.tenantCode}",
            "</if>",

            "<if test='query.productCode!=null'>",
            "and product_code=#{query.productCode}",
            "</if>",

            "and ( 1<![CDATA[<>]]>1 ",
            "<if test='query.flowName!=null'>",
            "or flow_name like #{query.flowName}",
            "</if>",

            "<if test='query.packageName!=null'>",
            "or package_name like #{query.packageName}",
            "</if>",
            ")",

            "</if>",
            "limit #{startNo},#{pageSize}",
            "</script>"
    })
    int count(QueryParams<FlowQuery> queryParams);

    @Select("select * from config_flow")
    @ResultMap(value = "flowResultMap")
    List<ConfigFlowDO> getAll();

    @Select("select * from config_flow where status = 1")
    @ResultMap(value = "flowResultMap")
    List<ConfigFlowDO> getAllEnable();

    @Insert("insert into config_flow (flow_code, flow_name, tenant_code,tenant_name, product_code,product_name,remark,"
            + "package_id, package_url,package_name,package_version, status, operator, gmt_created, gmt_modified)"
            + "values (#{flowCode}, #{flowName},#{tenantCode}, #{tenantName}, #{productCode},#{productName}, #{remark},#{packageId},"
            + "#{packageUrl}, #{packageName},#{packageVersion},#{status}, #{operator}, #{gmtCreated}, #{gmtModified})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(ConfigFlowDO configFlowDO);

    @Update({"<script>",
            "UPDATE config_flow SET ",
            "<if test='flowName!=null'>",
            "flow_name=#{flowName},",
            "</if>",
            "<if test='tenantCode!=null'>",
            "tenant_code=#{tenantCode},",
            "</if>",
            "<if test='tenantName!=null'>",
            "tenant_name=#{tenantName},",
            "</if>",
            "<if test='productCode!=null'>",
            "product_code=#{productCode},",
            "</if>",
            "<if test='productName!=null'>",
            "product_name=#{productName},",
            "</if>",
            "<if test='remark!=null'>",
            "remark=#{remark},",
            "</if>",
            "<if test='packageId!=null'>",
            "package_id=#{packageId},",
            "</if>",
            "<if test='packageUrl!=null'>",
            "package_url=#{packageUrl},",
            "</if>",
            "<if test='packageName!=null'>",
            "package_name=#{packageName},",
            "</if>",
            "<if test='packageVersion!=null'>",
            "package_version=#{packageVersion},",
            "</if>",
            "<if test='status!=null'>",
            "status=#{status},",
            "</if>",
            "operator = #{operator}, gmt_modified = #{gmtModified}",
            "where id=#{id}",
            "</script>",
    })
    void update(ConfigFlowDO configFlowDO);

    @Update("UPDATE config_flow SET status = #{status}, operator = #{operator}, gmt_modified = #{gmtModified} where product_code=#{productCode}")
    void batchUpdateStatus(ConfigFlowDO configFlowDO);

    @Delete("delete from config_flow where id=#{id}")
    void deleteById(int id);
}
