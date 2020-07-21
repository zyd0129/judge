package com.ps.judge.web.controller.rule;

import com.github.pagehelper.PageInfo;
import com.ps.common.ApiResponse;
import com.ps.common.query.QueryVo;
import com.ps.judge.web.pojo.bo.ConfigRulePackageBO;
import com.ps.judge.web.pojo.query.RulePackageQuery;
import com.ps.judge.web.pojo.req.RulePackageReq;
import com.ps.judge.web.service.rule.RulePackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ruleManager/packages")
public class RulePackageController {

    @Autowired
    private RulePackageService rulePackageService;

    @RequestMapping("query")
    public ApiResponse<PageInfo<ConfigRulePackageBO>> query(@RequestBody QueryVo<RulePackageQuery> queryVo) {
        PageInfo<ConfigRulePackageBO> configRulePackageBOPageInfo = rulePackageService.queryByPage(queryVo.getQuery(), queryVo.getCurPage(), queryVo.getPageSize());
        return ApiResponse.success(configRulePackageBOPageInfo);
    }


    @PostMapping("all")
    public ApiResponse<List<ConfigRulePackageBO>> all(@RequestBody RulePackageQuery rulePackageQuery) {
        List<ConfigRulePackageBO> variableBOList = rulePackageService.query(rulePackageQuery);
        return ApiResponse.success(variableBOList);
    }

    @PostMapping("create")
    public ApiResponse create(@Valid @RequestBody RulePackageReq rulePackageReq) {
        ConfigRulePackageBO rulePackageBO = rulePackageReq.convertToBO();
        rulePackageService.create(rulePackageBO);
        return ApiResponse.success(rulePackageBO);
    }

    @RequestMapping("update")
    public ApiResponse update() {
        return null;
    }
}
