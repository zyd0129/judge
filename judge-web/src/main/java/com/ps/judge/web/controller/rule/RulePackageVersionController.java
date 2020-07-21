package com.ps.judge.web.controller.rule;

import com.ps.common.ApiResponse;
import com.ps.judge.web.pojo.bo.ConfigRulePackageVersionBO;
import com.ps.judge.web.pojo.req.RulePackageVersionCreateReq;
import com.ps.judge.web.pojo.req.RulePackageVersionModifyReq;
import com.ps.judge.web.service.pkg.RulePackageVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/ruleManager/packageVersions")
public class RulePackageVersionController {
    @Autowired
    private RulePackageVersionService rulePackageVersionService;

    @PostMapping("create")
    public ApiResponse create(@Valid @RequestBody RulePackageVersionCreateReq rulePackageVersionCreateReq) {
        ConfigRulePackageVersionBO rulePackageVersionBO = rulePackageVersionCreateReq.convertToBO();
        rulePackageVersionService.create(rulePackageVersionBO);
        return ApiResponse.success(rulePackageVersionBO);
    }

    @PostMapping("modify")
    public ApiResponse modify(@Valid @RequestBody RulePackageVersionModifyReq modifyReq) {
        ConfigRulePackageVersionBO rulePackageVersionBO = modifyReq.convertToBO();
        rulePackageVersionService.modify(rulePackageVersionBO);
        return ApiResponse.success();
    }

}
