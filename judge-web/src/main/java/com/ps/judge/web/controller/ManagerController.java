package com.ps.judge.web.controller;

import com.ps.common.ApiResponse;
import com.ps.common.PageResult;
import com.ps.common.enums.Status;
import com.ps.common.query.*;
import com.ps.judge.web.models.ConfigFlowBO;
import com.ps.judge.web.models.ConfigPackageBO;
import com.ps.judge.web.models.ConfigProductBO;
import com.ps.judge.web.models.StoragePath;
import com.ps.judge.web.service.ConfigFlowService;
import com.ps.judge.web.service.ConfigPackageService;
import com.ps.judge.web.service.ConfigProductService;
import com.ps.judge.web.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    private ConfigFlowService configFlowService;

    @Autowired
    private ConfigProductService configProductService;

    @Autowired
    private ConfigPackageService configPackageService;

    @Autowired
    StorageService storageService;

    @GetMapping("flows/all")
    public ApiResponse<List<ConfigFlowBO>> allFlow() {
        List<ConfigFlowBO> all = configFlowService.getAll();
        return ApiResponse.success(all);
    }

    @PostMapping(value = "flows/query", params = "query=true")
    public ApiResponse<PageResult<ConfigFlowBO>> queryFlow(@RequestBody QueryVo<FlowQuery> reqQueryVo) {
        QueryParams<FlowQuery> flowQueryQueryParams = reqQueryVo.convertToQueryParam();
        List<ConfigFlowBO> all = configFlowService.query(flowQueryQueryParams);
        int total = configFlowService.count(flowQueryQueryParams);
        PageResult<ConfigFlowBO> pageResult = new PageResult<>();
        pageResult.setCurPage(reqQueryVo.getCurPage());
        pageResult.setPageSize(reqQueryVo.getPageSize());
        pageResult.setTotal(total);
        pageResult.setData(all);
        return ApiResponse.success(pageResult);
    }

    @PostMapping(value = "flows/query", params = "query=false")
    public ApiResponse<PageResult<ConfigFlowBO>> listFlow(@RequestBody QueryVo<FlowQuery> packageQuery) {
        packageQuery.setQuery(null);
        return queryFlow(packageQuery);
    }

    @PostMapping("flows/add")
    public ApiResponse<ConfigFlowBO> addFlow(@RequestBody ConfigFlowBO configFlowBO) {
        configFlowService.insert(configFlowBO);
        return ApiResponse.success(configFlowBO);
    }

    @PostMapping("flows/modify")
    public ApiResponse<ConfigFlowBO> modifyFlow(@RequestBody ConfigFlowBO configFlowBO) {
        configFlowService.modify(configFlowBO);
        return ApiResponse.success(configFlowBO);
    }

    @PostMapping("flows/delete")
    public ApiResponse deleteFlow(@RequestParam Integer id) {
        configFlowService.delete(id);
        return ApiResponse.success();
    }

    @PostMapping("flows/changeStatus")
    public ApiResponse<String> changeFlowStatus(@RequestBody ConfigFlowBO configFlowBO) {
        configFlowService.updateStatus(configFlowBO);
        return ApiResponse.success();
    }

    @PostMapping(value = "packages/query", params = "query=true")
    public ApiResponse<PageResult<ConfigPackageBO>> queryPackage(@RequestBody QueryVo<PackageQuery> reqQueryVo) {
        List<ConfigPackageBO> all = configPackageService.query(reqQueryVo.convertToQueryParam());
        int total = configPackageService.count(reqQueryVo.convertToQueryParam());
        PageResult<ConfigPackageBO> pageResult = new PageResult<>();
        pageResult.setCurPage(reqQueryVo.getCurPage());
        pageResult.setPageSize(reqQueryVo.getPageSize());
        pageResult.setTotal(total);
        pageResult.setData(all);
        return ApiResponse.success(pageResult);
    }

    @PostMapping(value = "packages/query", params = "query=false")
    public ApiResponse<PageResult<ConfigPackageBO>> listPackage(@RequestBody QueryVo<PackageQuery> packageQuery) {
        packageQuery.setQuery(null);
        return queryPackage(packageQuery);
    }

    @PostMapping("packages/add")
    public ApiResponse addPackage(@RequestBody ConfigPackageBO configPackageBO) {
        configPackageService.insert(configPackageBO);
        return ApiResponse.success(configPackageBO);
    }

    @PostMapping("packages/modify")
    public ApiResponse modifyPackage(@RequestBody ConfigPackageBO configPackageBO) {
        configPackageService.update(configPackageBO);
        return ApiResponse.success();
    }

    @PostMapping("packages/delete")
    public ApiResponse deletePackage(int id) {
        configPackageService.delete(id);
        return ApiResponse.success();
    }

    @PostMapping("packages/changeStatus")
    public ApiResponse changePackageStatus(@RequestBody ConfigPackageBO configPackageBO) {
        configPackageService.updateStatus(configPackageBO);
        return ApiResponse.success();
    }

    @GetMapping("packages/all")
    public ApiResponse changePackageStatus(@RequestParam Status status) {
        List<ConfigPackageBO> all = configPackageService.all(status);
        return ApiResponse.success(all);
    }


    @PostMapping(value = "products/query", params = "query=true")
    public ApiResponse<PageResult<ConfigProductBO>> queryProduct(@RequestBody QueryVo<ProductQuery> reqQueryVo) {
        QueryParams<ProductQuery> productQueryQueryParams = reqQueryVo.convertToQueryParam();
        List<ConfigProductBO> all = configProductService.query(productQueryQueryParams);
        int total = configProductService.count(productQueryQueryParams);
        PageResult<ConfigProductBO> pageResult = new PageResult<>();
        pageResult.setCurPage(reqQueryVo.getCurPage());
        pageResult.setPageSize(reqQueryVo.getPageSize());
        pageResult.setTotal(total);
        pageResult.setData(all);
        return ApiResponse.success(pageResult);
    }

    @PostMapping(value = "products/query", params = "query=false")
    public ApiResponse<PageResult<ConfigProductBO>> listProduct(@RequestBody QueryVo<ProductQuery> reqQueryVo) {
        reqQueryVo.setQuery(null);
        return queryProduct(reqQueryVo);
    }

    @PostMapping("products/add")
    public ApiResponse addProduct(@RequestBody ConfigProductBO configProductBO) {

        configProductService.insert(configProductBO);
        return ApiResponse.success(configProductBO);
    }

    @PostMapping("products/delete")
    public ApiResponse deleteProduct(int id) {
        configProductService.delete(id);
        return ApiResponse.success();
    }

    @PostMapping("products/changeStatus")
    public ApiResponse<String> changeProductStatus(@RequestBody ConfigProductBO configProductBO) {
        configProductService.updateStatus(configProductBO);
        return ApiResponse.success();

    }

    @GetMapping("products/all")
    public ApiResponse<List<ConfigProductBO>> changeProductStatus(@RequestParam String tenantCode) {
        List<ConfigProductBO> all = configProductService.listByTenantId(tenantCode);
        return ApiResponse.success(all);
    }

    @PostMapping("/packages/upload")
    public ApiResponse upload(MultipartFile file) {
        System.out.println("upload");
        System.out.println(file);
        try (InputStream inputStream = file.getInputStream()) {
            String originalFilename = file.getOriginalFilename();
            StoragePath upload = this.storageService.upload(inputStream, file.getSize(), originalFilename.substring(originalFilename.lastIndexOf(".")), null);
            System.out.println(upload.getFile());
            System.out.println(upload.getHost());
            System.out.println(upload.getPath());
            System.out.println(upload.getUrl());
            return ApiResponse.success(upload.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResponse.error(6001, "文件上传失败");
    }

}
