package com.ts.judge.provider.dao.ddao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface ConfigRulePackageVersionSequenceMapper {
    @Insert("insert into config_rule_package_version_sequence(package_id,current_version) values(#{packageId},0) ON DUPLICATE KEY UPDATE current_version=0")
    void insertByPackageId(@Param("packageId") Integer packageId);

    @Select("select current_version from config_rule_package_version_sequence where package_id=#{packageId} for update")
    int getSequenceByPackageId(@Param("packageId") Integer packageId);

    @Update("update config_rule_package_version_sequence set current_version=#{sequence} where package_id=#{packageId}")
    void setSequenceByPackageId(@Param("packageId") Integer packageId, @Param("sequence") Integer sequence);
}
