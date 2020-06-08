package com.ps.judge.web.service;


import com.ps.common.ApiResponse;
import com.ps.common.query.QueryParams;
import com.ps.judge.web.models.ConfigProductBO;
import com.ps.common.query.ProductQuery;

import java.util.List;

public interface ConfigProductService {
    ConfigProductBO getByProductCode(String productCode);

    ConfigProductBO getById(int id);

    void insert(ConfigProductBO configProductBO);

    void updateStatus(ConfigProductBO configProductBO);

    List<ConfigProductBO> query(QueryParams<ProductQuery> convertToQueryParam);


    int count(QueryParams<ProductQuery> queryQueryParams);

    void delete(int id);
}
