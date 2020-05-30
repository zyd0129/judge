package com.ps.judge.web.service;


import com.ps.judge.web.models.ConfigProductBO;

import java.util.List;

public interface ConfigProductService {
    List<ConfigProductBO> query(int pageNo, int size);

    ConfigProductBO getByProductCode(String productCode);

    ConfigProductBO getById(int id);

    void insert(ConfigProductBO configProductBO);

    void updateStatus(ConfigProductBO configProductBO);
}
