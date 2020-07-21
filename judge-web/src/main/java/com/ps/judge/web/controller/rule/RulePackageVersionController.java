package com.ps.judge.web.controller.rule;

import com.ps.common.ApiResponse;
import com.ps.judge.web.pojo.bo.ConfigRulePackageBO;
import com.ps.judge.web.pojo.query.RulePackageQuery;
import com.ps.judge.web.pojo.req.RulePackageReq;
import com.ps.judge.web.service.rule.RulePackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ruleManager/package/versions")
public class RulePackageVersionController{

    @Autowired
    private RulePackageService rulePackageService;


    @PostMapping("all")
    public ApiResponse<List<ConfigRulePackageBO>> all(RulePackageQuery rulePackageQuery) {
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
