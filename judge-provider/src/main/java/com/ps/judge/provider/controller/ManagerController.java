package com.ps.judge.provider.controller;

import com.google.protobuf.Api;
import com.ps.judge.provider.common.PageResult;
import com.ps.judge.provider.models.ConfigFlowBO;
import com.ps.judge.provider.models.ConfigPackageBO;
import com.ps.judge.provider.models.ConfigProductBO;
import com.ps.judge.provider.service.ConfigFlowService;
import com.ps.jury.api.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    private ConfigFlowService configFlowService;

    /**
     * query list
     * @return
     */
    @GetMapping("flows/query")
    public ApiResponse<PageResult<ConfigFlowBO>> queryFlow() {
        List<ConfigFlowBO> all = configFlowService.getAll();
        PageResult<ConfigFlowBO> pageResult = new PageResult<>();
        pageResult.setData(all);
        ApiResponse<PageResult<ConfigFlowBO>> apiResponse = ApiResponse.success(pageResult);
        return apiResponse;
    }

    @PostMapping("flows/add")
    public ApiResponse<ConfigFlowBO> addFlow(@RequestBody ConfigFlowBO configFlowBO) {
        configFlowService.insert(configFlowBO);
        ApiResponse apiResponse = ApiResponse.success(configFlowBO);
        return apiResponse;
    }

    @PostMapping("flows/delete")
    public ApiResponse<String> deleteFlow() {
        return null;
    }

    @PostMapping("flows/changeStatus")
    public ApiResponse<String> changeFlowStatus(@RequestBody ConfigFlowBO configFlowBO) {
        configFlowService.updateStatus(configFlowBO);
        return ApiResponse.success();
    }

    @PostMapping("flows/changePackage")
    public ApiResponse<String> changePackage(@RequestBody ConfigFlowBO configFlowBO) {
        configFlowService.changePackage(configFlowBO);
        return  ApiResponse.success();
    }

    @GetMapping("package/query")
    public ApiResponse<List<ConfigPackageBO>> queryPackage() {
        return null;
    }
    @PostMapping("package/add")
    public ApiResponse<List<ConfigPackageBO>> addPackage() {
        return null;
    }
}
