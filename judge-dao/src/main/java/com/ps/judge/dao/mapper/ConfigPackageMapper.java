package com.ps.judge.dao.mapper;

import com.ps.common.query.PackageQuery;
import com.ps.common.query.QueryParams;
import com.ps.judge.dao.entity.ConfigPackageDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ConfigPackageMapper {
    @Select("select * from config_package where id=#{id}")
    @Results(id = "packageResultMap", value = {
            @Result(column = "product_code", property = "productCode"),
            @Result(column = "product_name", property = "productName"),
            @Result(column = "tenant_code", property = "tenantCode"),
            @Result(column = "tenant_name", property = "tenantName"),
            @Result(column = "package_name", property = "packageName"),
            @Result(column = "gmt_created", property = "gmtCreated"),
            @Result(column = "gmt_modified", property = "gmtModified")
    })
    ConfigPackageDO getById(Integer id);

    @Select({"<script>",
            "select count(*) from config_package",
            "where",
            "1=1",
            "<if test='query!=null'>",
            "<if test='query.packageName!=null'>",
            "and package_name=#{query.packageName}",
            "</if>",
            "<if test='query.status!=null'>",
            "and",
            "status=#{query.statusCode}",
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

            "<if test='query.tenantCode!=null'>",
            "and tenant_code=#{query.tenantCode}",
            "</if>",

            "<if test='query.productCode!=null'>",
            "and product_code=#{query.productCode}",
            "</if>",

            "</if>",
            "</script>"
    })
    int count(QueryParams<PackageQuery> q);


    @Select({"<script>",
            "select * from config_package",
            "where",
            "1=1",
            "<if test='query!=null'>",
            "<if test='query.packageName!=null'>",
            "and package_name=#{query.packageName}",
            "</if>",
            "<if test='query.status!=null'>",
            "and",
            "status=#{query.statusCode}",
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

            "<if test='query.tenantCode!=null'>",
            "and tenant_code=#{query.tenantCode}",
            "</if>",

            "<if test='query.productCode!=null'>",
            "and product_code=#{query.productCode}",
            "</if>",

            "</if>",
            "limit #{startNo},#{pageSize}",
            "</script>"
    })
    @ResultMap(value = "packageResultMap")
    List<ConfigPackageDO> query(QueryParams<PackageQuery> q);


    @Insert("insert into config_package (product_code, product_name,tenant_code,tenant_name," +
            "package_name,version,url,remark," +
            "status,operator,gmt_created,gmt_modified)" +
            " values " +
            "(#{productCode}, #{productName},#{tenantCode},#{tenantName}, " +
            "#{packageName},#{version}, #{url},#{remark},"+
            "#{status},#{operator}, #{gmtCreated},#{gmtModified})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(ConfigPackageDO packageDO);

    @Update("UPDATE config_package SET status = #{status},operator=#{operator}, gmt_modified=#{gmtModified} where id=#{id}")
    void updateStatus(ConfigPackageDO packageDO);


    @Delete("delete from config_package where id=#{id}")
    void delete(int id);

    @Select("select * from config_package where status=#{status}")
    @ResultMap(value = "packageResultMap")
    List<ConfigPackageDO> listByStatus(int status);

    @Update("UPDATE config_package SET product_code=#{productCode},product_name=#{productName},tenant_code=#{tenantCode},tenant_name=#{tenantName}," +
            "package_name=#{packageName},version=#{version},url=#{url},remark=#{remark},"+
            "status = #{status},operator=#{operator}, gmt_modified=#{gmtModified} where id=#{id}")
    void update(ConfigPackageDO convertToDO);
}
