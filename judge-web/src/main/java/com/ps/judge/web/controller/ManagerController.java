package com.ps.judge.web.controller;

import com.ps.common.ApiResponse;
import com.ps.common.PageResult;
import com.ps.judge.web.models.ConfigFlowBO;
import com.ps.judge.web.models.ConfigPackageBO;
import com.ps.judge.web.models.ConfigProductBO;
import com.ps.judge.web.service.ConfigFlowService;
import com.ps.judge.web.service.ConfigProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    private ConfigFlowService configFlowService;

    @Autowired
    private ConfigProductService configProductService;


    /**
     * query list
     *
     * @return
     */
    @GetMapping("flows/query")
    public ApiResponse<PageResult<ConfigFlowBO>> queryFlow(@RequestParam int pageNo, @RequestParam(defaultValue = "10") int size) {
        List<ConfigFlowBO> all = configFlowService.query(pageNo, size);
        PageResult<ConfigFlowBO> pageResult = new PageResult<>();
        pageResult.setCurPage(pageNo);
        pageResult.setPageSize(size);

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
        return ApiResponse.success();
    }

    @GetMapping("package/query")
    public ApiResponse<List<ConfigPackageBO>> queryPackage() {
        return null;
    }

    @PostMapping("package/add")
    public ApiResponse<List<ConfigPackageBO>> addPackage() {
        return null;
    }



    @GetMapping("products/query")
    public ApiResponse<PageResult<ConfigProductBO>> queryProduct(@RequestParam int pageNo, @RequestParam(defaultValue = "10") int size) {
        List<ConfigProductBO> all = configProductService.query(pageNo, size);
        PageResult<ConfigProductBO> pageResult = new PageResult<>();
        pageResult.setCurPage(pageNo);
        pageResult.setPageSize(size);

        pageResult.setData(all);
        ApiResponse<PageResult<ConfigProductBO>> apiResponse = ApiResponse.success(pageResult);
        return apiResponse;
    }

    @PostMapping("products/add")
    public ApiResponse<ConfigProductBO> addProduct(@RequestBody ConfigProductBO configProductBO) {
        configProductService.insert(configProductBO);
        ApiResponse apiResponse = ApiResponse.success(configProductBO);
        return apiResponse;
    }

    @PostMapping("products/delete")
    public ApiResponse<String> deleteProduct() {
        return null;
    }

    @PostMapping("products/changeStatus")
    public ApiResponse<String> changeProductStatus(@RequestBody ConfigProductBO configProductBO) {
        configProductService.updateStatus(configProductBO);
        return ApiResponse.success();
    }
}
