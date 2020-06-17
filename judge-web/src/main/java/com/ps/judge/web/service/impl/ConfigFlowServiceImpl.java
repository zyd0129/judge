package com.ps.judge.web.service.impl;


import com.ps.common.enums.Status;
import com.ps.common.exception.BizException;
import com.ps.common.query.FlowQuery;
import com.ps.common.query.QueryParams;
import com.ps.judge.api.JudgeApi;
import com.ps.judge.api.entity.LoadFlowVO;
import com.ps.judge.dao.entity.ConfigFlowDO;
import com.ps.judge.dao.mapper.ConfigFlowMapper;
import com.ps.judge.web.auth.objects.AuthUserBO;
import com.ps.judge.web.models.ConfigFlowBO;
import com.ps.judge.web.service.ConfigFlowService;
import com.ps.jury.api.common.ApiResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConfigFlowServiceImpl implements ConfigFlowService {
    @Autowired
    ConfigFlowMapper flowMapper;

    @Autowired
    JudgeApi judgeApi;


    @Override
    public List<ConfigFlowBO> getAll() {
        List<ConfigFlowDO> configFlowDOList = flowMapper.getAll();
        return convertToBOList(configFlowDOList);
    }

    @Override
    public List<ConfigFlowBO> query(QueryParams<FlowQuery> queryParams) {
        processQueryParams(queryParams);
        return convertToBOList(flowMapper.query(queryParams));
    }

    @Override
    public int count(QueryParams<FlowQuery> queryParams) {
        processQueryParams(queryParams);
        return flowMapper.count(queryParams);
    }

    @Override
    public void modify(ConfigFlowBO configFlowBO) {
        AuthUserBO authUserBO = (AuthUserBO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        configFlowBO.setOperator(authUserBO.getUsername());
        configFlowBO.setGmtModified(LocalDateTime.now());
        configFlowBO.setStatus(null);
        flowMapper.update(convertToDO(configFlowBO));
    }

    private void processQueryParams(QueryParams<FlowQuery> queryParams) {
        if (queryParams == null || queryParams.getQuery() == null)
            return;
        String flowName = queryParams.getQuery().getFlowName();
        String packageName = queryParams.getQuery().getPackageName();
        FlowQuery query = queryParams.getQuery();
        boolean fuzzy=false;
        if (!StringUtils.isEmpty(flowName)) {
            query.setFlowName("%" + flowName + "%");
            fuzzy=true;
        }
        if (!StringUtils.isEmpty(packageName)) {
            query.setPackageName("%" + packageName + "%");
            fuzzy=true;
        }
        query.setFuzzy(fuzzy);
    }


    @Override
    public ConfigFlowBO getByFlowCode(String flowCode) {
        ConfigFlowDO configFlowDO = flowMapper.getByFlowCode(flowCode);
        if (configFlowDO == null) {
            return null;
        }
        return convertToBO(configFlowDO);
    }

    @Override
    public ConfigFlowBO getById(int id) {
        ConfigFlowDO configFlowDO = flowMapper.getById(id);
        if (configFlowDO == null) {
            return null;
        }
        return convertToBO(configFlowDO);
    }

    @Override
    public void insert(ConfigFlowBO configFlowBO) {
        AuthUserBO authUserBO = (AuthUserBO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        configFlowBO.setOperator(authUserBO.getUsername());
        configFlowBO.setGmtCreated(LocalDateTime.now());
        configFlowBO.setGmtModified(LocalDateTime.now());
        configFlowBO.setStatus(Status.STOPPED);
        ConfigFlowDO configFlowDO = convertToDO(configFlowBO);
        configFlowDO.setGmtCreated(LocalDateTime.now());
        flowMapper.insert(configFlowDO);
        configFlowBO.setId(configFlowDO.getId());
    }

    @Override
    @Transactional
    public void updateStatus(ConfigFlowBO configFlowBO) throws BizException {
        AuthUserBO authUserBO = (AuthUserBO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        configFlowBO.setOperator(authUserBO.getUsername());
        configFlowBO.setGmtModified(LocalDateTime.now());
        ConfigFlowBO preFlow = convertToBO(flowMapper.getById(configFlowBO.getId()));
        flowMapper.update(convertToDO(configFlowBO));
        if (!Status.STARTED.equals(preFlow.getStatus()) && Status.STARTED.equals(configFlowBO.getStatus())) {
            ApiResponse<String> response = judgeApi.loadFlow(new LoadFlowVO(preFlow.getFlowCode(), true));
            if (!response.isSuccess()) {
                throw new BizException(60001, "judge部署失败");
            }
        } else if (Status.STARTED.equals(preFlow.getStatus()) && !Status.STARTED.equals(configFlowBO.getStatus())) {
            ApiResponse<String> response = judgeApi.loadFlow(new LoadFlowVO(preFlow.getFlowCode(), false));
            if (!response.isSuccess()) {
                throw new BizException(60001, "judge unload失败");
            }
        }

    }

    @Override
    public void delete(int id) {
        flowMapper.deleteById(id);
    }


    private ConfigFlowBO convertToBO(ConfigFlowDO configFlowDO) {
        if (configFlowDO == null) {
            return null;
        }
        ConfigFlowBO configFlowBO = new ConfigFlowBO();
        BeanUtils.copyProperties(configFlowDO, configFlowBO);
        configFlowBO.setStatus(Status.valueOf(configFlowDO.getStatus()));
        return configFlowBO;
    }

    private ConfigFlowDO convertToDO(ConfigFlowBO configFlowBO) {
        if (configFlowBO == null) {
            return null;
        }
        ConfigFlowDO configFlowDO = new ConfigFlowDO();
        BeanUtils.copyProperties(configFlowBO, configFlowDO);
        if (configFlowBO.getStatus() != null) {
            configFlowDO.setStatus(configFlowBO.getStatus().getValue());
        }
        return configFlowDO;
    }

    private List<ConfigFlowBO> convertToBOList(List<ConfigFlowDO> configFlowDOList) {
        if (configFlowDOList == null) {
            return null;
        }
        List<ConfigFlowBO> configFlowBOList = new ArrayList<>();
        for (ConfigFlowDO configFlowDO : configFlowDOList) {
            ConfigFlowBO configFlowBO = convertToBO(configFlowDO);
            configFlowBOList.add(configFlowBO);
        }
        return configFlowBOList;
    }
}
