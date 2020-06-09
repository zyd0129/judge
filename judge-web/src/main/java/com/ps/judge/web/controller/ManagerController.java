package com.ps.judge.web.controller;

import com.ps.common.ApiResponse;
import com.ps.common.PageResult;
import com.ps.common.enums.Status;
import com.ps.common.query.PackageQuery;
import com.ps.common.query.QueryVo;
import com.ps.judge.web.models.ConfigFlowBO;
import com.ps.judge.web.models.ConfigPackageBO;
import com.ps.judge.web.models.ConfigProductBO;
import com.ps.judge.web.models.StoragePath;
import com.ps.common.query.ProductQuery;
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

    @PostMapping("packages/all")
    public ApiResponse changePackageStatus(@RequestParam Status status) {
        List<ConfigPackageBO> all =configPackageService.all(status);
        return ApiResponse.success(all);
    }


    @PostMapping(value = "products/query", params = "query=true")
    public ApiResponse<PageResult<ConfigProductBO>> queryProduct(@RequestBody QueryVo<ProductQuery> reqQueryVo) {
        List<ConfigProductBO> all = configProductService.query(reqQueryVo.convertToQueryParam());
        int total = configProductService.count(reqQueryVo.convertToQueryParam());
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
