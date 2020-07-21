package com.ps.judge.web.controller.rule;

import com.ps.common.ApiResponse;
import com.ps.judge.web.models.ConfigRuleVariableBO;
import com.ps.judge.web.service.rule.VariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("variables")
public class VariableController {

    @Autowired
    private VariableService variableService;


    @RequestMapping("all")
    public ApiResponse<List<ConfigRuleVariableBO>> all() {
        List<ConfigRuleVariableBO> variableBOList = variableService.all();
        return ApiResponse.success(variableBOList);
    }

    @RequestMapping("create")
    public ApiResponse create() {
        return null;
    }

    @RequestMapping("update")
    public ApiResponse update() {
        return null;
    }
}
