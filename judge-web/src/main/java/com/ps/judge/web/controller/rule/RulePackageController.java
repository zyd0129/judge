package com.ps.judge.web.controller.rule;

import com.github.pagehelper.PageInfo;
import com.ps.common.ApiResponse;
import com.ps.common.query.QueryVo;
import com.ps.judge.web.pojo.bo.ConfigRulePackageBO;
import com.ps.judge.web.pojo.bo.ConfigRulePackageVersionBO;
import com.ps.judge.web.pojo.query.RulePackageQuery;
import com.ps.judge.web.pojo.req.RulePackageCreateReq;
import com.ps.judge.web.pojo.req.RulePackageModifyReq;
import com.ps.judge.web.pojo.req.RulePackageVersionCreateReq;
import com.ps.judge.web.service.rule.RulePackageService;
import com.ps.judge.web.service.rule.RulePackageVersionService;
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

    /**
     * 创建版本
     * @param rulePackageVersionCreateReq
     * @return
     */

    @PostMapping("createVersion")
    public ApiResponse create(@Valid @RequestBody RulePackageVersionCreateReq rulePackageVersionCreateReq) {
        ConfigRulePackageVersionBO rulePackageVersionBO = rulePackageVersionCreateReq.convertToBO();
        rulePackageVersionService.create(rulePackageVersionBO);
        return ApiResponse.success(rulePackageVersionBO);
    }

    @PostMapping("modifyVersion")
    public ApiResponse modify(@Valid @RequestBody RulePackageVersionCreateReq rulePackageVersionCreateReq) {
        ConfigRulePackageVersionBO rulePackageVersionBO = rulePackageVersionCreateReq.convertToBO();
        rulePackageVersionService.create(rulePackageVersionBO);
        return ApiResponse.success(rulePackageVersionBO);
    }
}
