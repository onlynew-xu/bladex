package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.CompanyType;
import com.steelman.iot.platform.entity.Pager;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

/**
 * @author tang
 * date 2020-11-23
 */
public interface CompanyTypeService {

    void insert(CompanyType record);
    void update(CompanyType record);
    void deleteById(Long id);
    Pager<T> selectByAll(Map<String, Integer> params);

    List<CompanyType> findAll();
}
