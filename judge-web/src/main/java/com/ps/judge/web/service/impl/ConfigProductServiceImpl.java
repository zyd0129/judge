package com.ps.judge.web.service.impl;

import com.ps.common.exception.BizException;
import com.ps.common.query.QueryParams;
import com.ps.judge.dao.entity.ConfigFlowDO;
import com.ps.judge.dao.entity.ConfigProductDO;
import com.ps.judge.dao.mapper.ConfigFlowMapper;
import com.ps.judge.dao.mapper.ConfigProductMapper;
import com.ps.judge.web.auth.objects.AuthUserBO;
import com.ps.judge.web.models.ConfigProductBO;
import com.ps.common.query.ProductQuery;
import com.ps.judge.web.service.ConfigProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigProductServiceImpl implements ConfigProductService {
    @Autowired
    ConfigProductMapper productMapper;

    @Autowired
    ConfigFlowMapper flowMapper;

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
        configProductBO.setStatus(ConfigProductBO.Status.STOPPED);
        ConfigProductDO configProductDO = convertToDO(configProductBO);
        configProductDO.setGmtCreated(LocalDateTime.now());
        configProductDO.setGmtModified(LocalDateTime.now());
        AuthUserBO authUserBO = (AuthUserBO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        configProductDO.setOperator(authUserBO.getUsername());
        productMapper.insert(configProductDO);
        configProductBO.setId(configProductDO.getId());
    }

    @Override
    @Transactional
    public void updateStatus(ConfigProductBO configProductBO) throws BizException {
        if(StringUtils.isEmpty(configProductBO.getProductCode())){
            ConfigProductBO configProductBO1 = getById(configProductBO.getId());
            configProductBO.setProductCode(configProductBO1.getProductCode());
        }
        ConfigProductDO configProductDO = convertToDO(configProductBO);
        AuthUserBO authUserBO = (AuthUserBO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        configProductDO.setOperator(authUserBO.getUsername());
        configProductDO.setGmtModified(LocalDateTime.now());
        productMapper.updateStatus(configProductDO);
        if(ConfigProductBO.Status.STOPPED.equals(configProductBO.getStatus())){
            ConfigFlowDO configFlowDO = new ConfigFlowDO();
            configFlowDO.setGmtModified(LocalDateTime.now());
            configFlowDO.setStatus(0);
            configFlowDO.setOperator(authUserBO.getUsername());
            configFlowDO.setProductCode(configProductBO.getProductCode());
            flowMapper.batchUpdateStatus(configFlowDO);
        }
        if(ConfigProductBO.Status.DISABLED.equals(configProductBO.getStatus())){
            ConfigFlowDO configFlowDO = new ConfigFlowDO();
            configFlowDO.setGmtModified(LocalDateTime.now());
            configFlowDO.setStatus(2);
            configFlowDO.setOperator(authUserBO.getUsername());
            configFlowDO.setProductCode(configProductBO.getProductCode());
            flowMapper.batchUpdateStatus(configFlowDO);
        }
    }

    @Override
    public List<ConfigProductBO> query(QueryParams<ProductQuery> convertToQueryParam) {
        convertParam(convertToQueryParam);
        return convertToBOList(productMapper.query(convertToQueryParam));
    }

    @Override
    public int count(QueryParams<ProductQuery> queryQueryParams) {
        convertParam(queryQueryParams);
        return productMapper.count(queryQueryParams);
    }

    @Override
    public void delete(int id) {
         productMapper.delete(id);
    }

    @Override
    public List<ConfigProductBO> listByTenantId(String tenantId) {
        return convertToBOList(productMapper.listByTenantId(tenantId));
    }

    private void convertParam(QueryParams<ProductQuery> queryQueryParams){
        ProductQuery query = queryQueryParams.getQuery();

        if(query!=null) {
            boolean fuzzy=false;
            if (!StringUtils.isEmpty(query.getProductCode())) {
                query.setProductCode("%" + query.getProductCode() + "%");
                fuzzy=true;
            }
            if (!StringUtils.isEmpty(query.getTenantName())) {
                query.setTenantName("%" + query.getTenantName() + "%");
                fuzzy=true;
            }
            if (!StringUtils.isEmpty(query.getProductName())) {
                query.setProductName("%" + query.getProductName() + "%");
                fuzzy=true;
            }
            query.setFuzzy(fuzzy);
        }
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
        for (ConfigProductDO configProductDO : configProductDOList) {
            ConfigProductBO configProductBO = convertToBO(configProductDO);
            configProductBOList.add(configProductBO);
        }
        return configProductBOList;
    }
}
