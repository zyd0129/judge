package com.ps.judge.web.service.impl;

import com.ps.common.enums.Status;
import com.ps.common.query.PackageQuery;
import com.ps.common.query.QueryParams;
import com.ps.judge.dao.entity.ConfigPackageDO;
import com.ps.judge.dao.mapper.ConfigPackageMapper;
import com.ps.judge.web.auth.objects.AuthUserBO;
import com.ps.judge.web.models.ConfigPackageBO;
import com.ps.judge.web.models.ConfigPackageBO;
import com.ps.judge.web.service.ConfigPackageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigPackageServiceImpl implements ConfigPackageService {
    @Autowired
    ConfigPackageMapper packageMapper;
    
    @Override
    public void updateStatus(ConfigPackageBO configPackageBO) {
        AuthUserBO authUserBO = (AuthUserBO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        configPackageBO.setOperator(authUserBO.getUsername());
        configPackageBO.setGmtModified(LocalDateTime.now());
        packageMapper.updateStatus(convertToDO(configPackageBO));
    }

    @Override
    public void delete(int id) {
        packageMapper.delete(id);
    }

    @Override
    public void insert(ConfigPackageBO configPackageBO) {
        AuthUserBO authUserBO = (AuthUserBO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        configPackageBO.setOperator(authUserBO.getUsername());
        configPackageBO.setGmtModified(LocalDateTime.now());
        configPackageBO.setGmtCreated(LocalDateTime.now());
        configPackageBO.setStatus(Status.STOPPED);
        ConfigPackageDO configPackageDO = convertToDO(configPackageBO);
        packageMapper.insert(configPackageDO);
        configPackageBO.setId(configPackageDO.getId());
    }

    @Override
    public List<ConfigPackageBO> query(QueryParams<PackageQuery> convertToQueryParam) {
        return convertToBOList(packageMapper.query(convertToQueryParam));
    }

    @Override
    public int count(QueryParams<PackageQuery> convertToQueryParam) {
        return packageMapper.count(convertToQueryParam);
    }

    @Override
    public List<ConfigPackageBO> all(Status status) {
        return convertToBOList(packageMapper.listByStatus(status.getValue()));
    }

    private ConfigPackageBO convertToBO(ConfigPackageDO configPackageDO) {
        if (configPackageDO == null) {
            return null;
        }
        ConfigPackageBO configPackageBO = new ConfigPackageBO();
        BeanUtils.copyProperties(configPackageDO, configPackageBO);
        configPackageBO.setStatus(Status.valueOf(configPackageDO.getStatus()));
        return configPackageBO;
    }

    private ConfigPackageDO convertToDO(ConfigPackageBO configPackageBO) {
        if (configPackageBO == null) {
            return null;
        }
        ConfigPackageDO configPackageDO = new ConfigPackageDO();
        BeanUtils.copyProperties(configPackageBO, configPackageDO);
        configPackageDO.setStatus(configPackageBO.getStatus().getValue());
        return configPackageDO;
    }

    private List<ConfigPackageBO> convertToBOList(List<ConfigPackageDO> configPackageDOList) {
        if (configPackageDOList == null) {
            return null;
        }
        List<ConfigPackageBO> configPackageBOList = new ArrayList<>();
        for (ConfigPackageDO configPackageDO : configPackageDOList) {
            ConfigPackageBO configPackageBO = convertToBO(configPackageDO);
            configPackageBOList.add(configPackageBO);
        }
        return configPackageBOList;
    }
}
