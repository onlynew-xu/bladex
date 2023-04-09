package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.Company;
import com.steelman.iot.platform.entity.Pager;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

/**
 * @author tang
 * date 2020-11-23
 */
public interface CompanyService {

    void insert(Company record);
    void update(Company record);
    void deleteById(Long id);
    Pager<T> selectByAll(Map<String, Integer> params);
    Company findByid(Long id);

    /**
     * 根据项目id 获取所有公司
     * @param projectId
     * @return
     */
    List<Company> findByProjectId(Long projectId, Integer type);

    void removeById(Long companyId);

    Company findByName(Long projectId, String companyName);
}
