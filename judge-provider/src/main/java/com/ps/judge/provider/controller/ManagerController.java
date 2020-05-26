package com.ps.judge.provider.controller;

import com.ps.judge.provider.common.PageResult;
import com.ps.judge.provider.models.ConfigPackageBO;
import com.ps.judge.provider.models.ConfigProductBO;
import com.ps.jury.api.objects.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    /**
     * query list
     * @return
     */
    @GetMapping("flows/query")
    public ApiResponse<PageResult<ConfigProductBO>> queryFlow() {
        return null;
    }

    @PostMapping("flows/add")
    public ApiResponse<String> addFlow() {
        return null;
    }

    @PostMapping("flows/delete")
    public ApiResponse<String> deleteFlow() {
        return null;
    }

    @PostMapping("flows/changeStatus")
    public ApiResponse<String> changeFlowStatus() {
        return null;
    }

    @PostMapping("flows/changePackage")
    public ApiResponse<String> changePackage() {
        return null;
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
