package com.ps.judge.dao.mapper;

import com.ps.judge.dao.entity.ConfigProductDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ConfigProductMapper {
    @Select("select * from config_product where product_code=#{productCode}")
    @Results(id = "productResultMap", value = {
            @Result(column = "product_code", property = "productCode"),
            @Result(column = "product_name", property = "productName"),
            @Result(column = "tenant_code", property = "tenantCode"),
            @Result(column = "gmt_created", property = "gmtCreated"),
            @Result(column = "gmt_modified", property = "gmtModified")
    })
    ConfigProductDO getByCode(String productCode);

    @Select("select * from config_product where id=#{id}")
    @ResultMap(value = "productResultMap")
    ConfigProductDO getById(int id);


    @Select("select * from config_product order by id limit #{from},#{size}")
    @ResultMap(value = "productResultMap")
    List<ConfigProductDO> query(int from, int size);


    @Insert("insert into config_product (product_code, product_name,tenant_code," +
            "status,operator,gmt_created,gmt_modified)" +
            " values " +
            "(#{productCode}, #{productName},#{tenantCode}, " +
            "#{status},#{operator}, #{gmtCreated},#{gmtModified})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(ConfigProductDO productDO);

    @Update("UPDATE config_product SET status = #{status},operator=#{operator}, gmt_modified=#{gmtModified} where id=#{id}")
    void updateStatus(ConfigProductDO productDO);
}
