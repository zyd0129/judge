package com.ps.judge.provider.service.impl;

import com.ps.judge.dao.entity.ConfigProductDO;
import com.ps.judge.dao.mapper.ConfigProductMapper;
import com.ps.judge.provider.drools.KSessionManager;
import com.ps.judge.provider.exception.BizException;
import com.ps.judge.provider.models.ConfigProductBO;
import com.ps.judge.provider.service.ConfigProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigProductServiceImpl implements ConfigProductService {
    @Autowired
    ConfigProductMapper productMapper;
    @Autowired
    private KSessionManager kSessionManager;

    @Override
    public List<ConfigProductBO> query(int pageNo, int size) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        List<ConfigProductDO> configProductDOList = productMapper.query((pageNo - 1) * size, size);

        return convertToBOList(configProductDOList);
    }

    @Override
    public ConfigProductBO getByProductCode(String flowCode) {
        ConfigProductDO configProductDO = productMapper.getByCode(flowCode);
        if (configProductDO == null) {
            return null;
        }
        return convertToBO(configProductDO);
    }

    @Override
    public ConfigProductBO getById(int id) {
        ConfigProductDO configProductDO = productMapper.getById(id);
        if (configProductDO == null) {
            return null;
        }
        return convertToBO(configProductDO);
    }

    @Override
    public void insert(ConfigProductBO configProductBO) {
        ConfigProductDO configProductDO = convertToDO(configProductBO);
        configProductDO.setGmtCreated(LocalDateTime.now());
        productMapper.insert(configProductDO);
        configProductBO.setId(configProductDO.getId());
    }

    @Override
    @Transactional
    public void updateStatus(ConfigProductBO configProductBO) throws BizException {
        ConfigProductDO ConfigProductDO = convertToDO(configProductBO);
        ConfigProductDO.setGmtModified(LocalDateTime.now());
        productMapper.updateStatus(ConfigProductDO);
    }
    

    private ConfigProductBO convertToBO(ConfigProductDO configProductDO) {
        if (configProductDO == null) {
            return null;
        }
        ConfigProductBO configProductBO = new ConfigProductBO();
        BeanUtils.copyProperties(configProductDO, configProductBO);
        configProductBO.setStatus(ConfigProductBO.Status.valueOf(configProductDO.getStatus()));
        return configProductBO;
    }

    private ConfigProductDO convertToDO(ConfigProductBO configProductBO) {
        if (configProductBO == null) {
            return null;
        }
        ConfigProductDO configProductDO = new ConfigProductDO();
        BeanUtils.copyProperties(configProductBO, configProductDO);
        configProductDO.setStatus(configProductBO.getStatus().getValue());
        return configProductDO;
    }

    private List<ConfigProductBO> convertToBOList(List<ConfigProductDO> configProductDOList) {
        if (configProductDOList == null) {
            return null;
        }
        List<ConfigProductBO> configProductBOList = new ArrayList<>();
        for (ConfigProductDO ConfigProductDO : configProductDOList) {
            ConfigProductBO configProductBO = convertToBO(ConfigProductDO);
            configProductBOList.add(configProductBO);
        }
        return configProductBOList;
    }
}
