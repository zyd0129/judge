package com.ps.judge.web.controller.rule;

import com.ps.common.ApiResponse;
import com.ps.judge.web.pojo.bo.ConfigRuleVariableBO;
import com.ps.judge.web.pojo.query.VariableQuery;
import com.ps.judge.web.service.rule.VariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ruleManager/variables")
public class VariableController {

    @Autowired
    private VariableService variableService;


    @RequestMapping("query")
    public ApiResponse<List<ConfigRuleVariableBO>> query(@RequestBody VariableQuery variableQuery) {
        List<ConfigRuleVariableBO> variableBOList = variableService.query(variableQuery);
        return ApiResponse.success(variableBOList);
    }
}
