package com.ps.judge.web.controller.rule;

import com.github.pagehelper.PageInfo;
import com.ps.common.ApiResponse;
import com.ps.judge.web.pojo.bo.ConfigRuleConditionBO;
import com.ps.judge.web.pojo.query.ConditionQuery;
import com.ps.judge.web.pojo.query.PageQuery;
import com.ps.judge.web.pojo.req.RuleConditionCreateReq;
import com.ps.judge.web.pojo.req.RuleConditionModifyReq;
import com.ps.judge.web.service.rule.ConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/ruleManager/conditions")
public class RuleConditionController {

    @Autowired
    ConditionService conditionService;

    @PostMapping("create")
    public ApiResponse create(@RequestBody RuleConditionCreateReq ruleCreateReq) {
        ConfigRuleConditionBO ruleBO = ruleCreateReq.convertToBO();
        conditionService.create(ruleBO);
        return ApiResponse.success(ruleBO);
    }

    @PostMapping("modify")
    public ApiResponse modify(@RequestBody RuleConditionModifyReq ruleModifyReq) {
        ConfigRuleConditionBO ruleBO = ruleModifyReq.convertToBO();
        conditionService.modify(ruleBO);
        return ApiResponse.success();
    }

    @PostMapping("query")
    public ApiResponse query(@Valid @RequestBody PageQuery<ConditionQuery> pageQuery){
        PageInfo<ConfigRuleConditionBO> configRuleConditionPackageBOPageInfo = conditionService.queryByPage(pageQuery.getQuery(), pageQuery.getCurPage(), pageQuery.getPageSize());
        return ApiResponse.success(configRuleConditionPackageBOPageInfo);
    }
}
