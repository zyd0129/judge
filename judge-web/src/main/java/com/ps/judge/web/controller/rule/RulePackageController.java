package com.ps.judge.web.controller.rule;

import com.github.pagehelper.PageInfo;
import com.ps.common.ApiResponse;
import com.ps.judge.web.pojo.bo.ConfigRulePackageBO;
import com.ps.judge.web.pojo.query.PageQuery;
import com.ps.judge.web.pojo.query.RulePackageQuery;
import com.ps.judge.web.pojo.req.RulePackageCreateReq;
import com.ps.judge.web.pojo.req.RulePackageModifyReq;
import com.ps.judge.web.service.pkg.RulePackageService;
import com.ps.judge.web.service.pkg.RulePackageVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ruleManager/packages")
public class RulePackageController {

    @Autowired
    private RulePackageService rulePackageService;

    @Autowired
    private RulePackageVersionService rulePackageVersionService;

    @RequestMapping("query")
    public ApiResponse<PageInfo<ConfigRulePackageBO>> query(@RequestBody PageQuery<RulePackageQuery> queryVo) {
        PageInfo<ConfigRulePackageBO> configRulePackageBOPageInfo = rulePackageService.queryByPage(queryVo.getQuery(), queryVo.getCurPage(), queryVo.getPageSize());
        return ApiResponse.success(configRulePackageBOPageInfo);
    }


    @PostMapping("all")
    public ApiResponse<List<ConfigRulePackageBO>> all(@RequestBody RulePackageQuery rulePackageQuery) {
        List<ConfigRulePackageBO> variableBOList = rulePackageService.query(rulePackageQuery);
        return ApiResponse.success(variableBOList);
    }

    @PostMapping("create")
    public ApiResponse create(@Valid @RequestBody RulePackageCreateReq rulePackageCreateReq) {
        ConfigRulePackageBO rulePackageBO = rulePackageCreateReq.convertToBO();
        rulePackageService.create(rulePackageBO);
        return ApiResponse.success(rulePackageBO);
    }

    @RequestMapping("modify")
    public ApiResponse update(@Valid @RequestBody RulePackageModifyReq rulePackageModifyReq) {
        ConfigRulePackageBO rulePackageBO = rulePackageModifyReq.convertToBO();
        rulePackageService.modify(rulePackageBO);
        return ApiResponse.success();
    }
}
