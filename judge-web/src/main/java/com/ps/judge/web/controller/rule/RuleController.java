package com.ps.judge.web.controller.rule;

import com.github.pagehelper.PageInfo;
import com.ps.common.ApiResponse;
import com.ps.judge.web.pojo.bo.ConfigRuleBO;
import com.ps.judge.web.pojo.query.PageQuery;
import com.ps.judge.web.pojo.query.RuleQuery;
import com.ps.judge.web.pojo.req.RuleCreateReq;
import com.ps.judge.web.pojo.req.RuleModifyReq;
import com.ps.judge.web.service.rule.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/ruleManager/rules")
public class RuleController {

    @Autowired
    RuleService ruleService;

    @PostMapping("create")
    public ApiResponse create(@RequestBody RuleCreateReq ruleCreateReq) {
        ConfigRuleBO ruleBO = ruleCreateReq.convertToBO();
        ruleService.create(ruleBO);
        return ApiResponse.success(ruleBO);
    }

    @PostMapping("modify")
    public ApiResponse modify(@RequestBody RuleModifyReq ruleModifyReq) {
        ConfigRuleBO ruleBO = ruleModifyReq.convertToBO();
        ruleService.modify(ruleBO);
        return ApiResponse.success();
    }

    @PostMapping("query")
    public ApiResponse query(@Valid @RequestBody PageQuery<RuleQuery> queryVo){
        PageInfo<ConfigRuleBO> configRulePackageBOPageInfo = ruleService.queryByPage(queryVo.getQuery(), queryVo.getCurPage(), queryVo.getPageSize());
        return ApiResponse.success(configRulePackageBOPageInfo);
    }
}
