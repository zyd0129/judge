package com.ps.judge.provider.service;

import com.ps.judge.provider.models.ConfigProductBO;

import java.util.List;

public interface ConfigProductService {
    List<ConfigProductBO> query(int pageNo, int size);

    ConfigProductBO getByProductCode(String productCode);

    ConfigProductBO getById(int id);

    void insert(ConfigProductBO configProductBO);

    void updateStatus(ConfigProductBO configProductBO);
}
