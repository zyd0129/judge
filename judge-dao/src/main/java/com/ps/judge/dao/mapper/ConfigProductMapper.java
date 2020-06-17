package com.ps.judge.dao.mapper;

import com.ps.common.query.ProductQuery;
import com.ps.common.query.QueryParams;
import com.ps.judge.dao.entity.ConfigProductDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ConfigProductMapper {
    @Select("select * from config_product where product_code=#{productCode}")
    @Results(id = "productResultMap", value = {
            @Result(column = "product_code", property = "productCode"),
            @Result(column = "product_name", property = "productName"),
            @Result(column = "tenant_code", property = "tenantCode"),
            @Result(column = "tenant_name", property = "tenantName"),
            @Result(column = "gmt_created", property = "gmtCreated"),
            @Result(column = "gmt_modified", property = "gmtModified")
    })
    ConfigProductDO getByCode(String productCode);

    @Select("select * from config_product where id=#{id}")
    @ResultMap(value = "productResultMap")
    ConfigProductDO getById(int id);

    @Select({"<script>",
            "select count(*) from config_product",
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

            "<if test='query.gmtModifiedFrom!=null'>",
            "and",
            "gmt_modified>=#{query.gmtModifiedFrom}",
            "</if>",

            "<if test='query.gmtModifiedTo!=null'>",
            "and",
            "gmt_modified<![CDATA[<=]]>#{query.gmtModifiedTo}",
            "</if>",

            "and ( 1<![CDATA[<>]]>1 " ,
            "<if test='query.tenantName!=null'>",
            "or tenant_name like #{query.tenantName}",
            "</if>",

            "<if test='query.productName!=null'>",
            "or product_name like #{query.productName}",
            "</if>",

            "<if test='query.productCode!=null'>",
            "or product_code like #{query.productCode}",
            "</if>",
            ")",

            "</if>",
            "</script>"
    })
    int count(QueryParams<ProductQuery> q);


    @Select({"<script>",
            "select * from config_product",
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

            "<if test='query.gmtModifiedFrom!=null'>",
            "and",
            "gmt_modified>=#{query.gmtModifiedFrom}",
            "</if>",

            "<if test='query.gmtModifiedTo!=null'>",
            "and",
            "gmt_modified<![CDATA[<=]]>#{query.gmtModifiedTo}",
            "</if>",
            "<if test='query.fuzzy!=false'>",
            "and ( 1<![CDATA[<>]]>1 " ,
            "<if test='query.tenantName!=null'>",
            "or tenant_name like #{query.tenantName}",
            "</if>",

            "<if test='query.productName!=null'>",
            "or product_name like #{query.productName}",
            "</if>",

            "<if test='query.productCode!=null'>",
            "or product_code like #{query.productCode}",
            "</if>",
            ")",
            "</if>",
            "</if>",
            "limit #{startNo},#{pageSize}",
            "</script>"
    })
    @ResultMap(value = "productResultMap")
    List<ConfigProductDO> query(QueryParams<ProductQuery> q);


    @Insert("insert into config_product (product_code, product_name,tenant_code,tenant_name,remark," +
            "status,operator,gmt_created,gmt_modified)" +
            " values " +
            "(#{productCode}, #{productName},#{tenantCode},#{tenantName},#{remark}, " +
            "#{status},#{operator}, #{gmtCreated},#{gmtModified})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(ConfigProductDO productDO);

    @Update("UPDATE config_product SET status = #{status},operator=#{operator}, gmt_modified=#{gmtModified} where id=#{id}")
    void updateStatus(ConfigProductDO productDO);

    @Delete("delete from config_product where id=#{id}")
    void delete(int id);

    @Select("select * from config_product where tenant_code=#{tenantCode}")
    @ResultMap(value = "productResultMap")
    List<ConfigProductDO> listByTenantId(String tenantCode);
}
