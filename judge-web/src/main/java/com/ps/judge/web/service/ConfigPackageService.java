package com.ps.judge.web.service;


import com.ps.common.enums.Status;
import com.ps.common.query.PackageQuery;
import com.ps.common.query.ProductQuery;
import com.ps.common.query.QueryParams;
import com.ps.judge.web.models.ConfigPackageBO;
import com.ps.judge.web.models.ConfigProductBO;

import java.util.List;

public interface ConfigPackageService {
    void updateStatus(ConfigPackageBO configPackageBO);

    void delete(int id);

    void insert(ConfigPackageBO configPackageBO);

    List<ConfigPackageBO> query(QueryParams<PackageQuery> convertToQueryParam);

    int count(QueryParams<PackageQuery> convertToQueryParam);

    List<ConfigPackageBO> all(Status status);

    void update(ConfigPackageBO configPackageBO);
}
