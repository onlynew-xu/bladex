package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.CompanyTypeDao;
import com.steelman.iot.platform.entity.CompanyType;
import com.steelman.iot.platform.entity.Pager;
import com.steelman.iot.platform.service.CompanyTypeService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author tang
 * date 2020-11-25
 */
@Service("companyTypeService")
public class CompanyTypeServiceImpl extends BaseService implements CompanyTypeService {
    @Resource
    private CompanyTypeDao companyTypeDao;

    public void insert(CompanyType record) {
        companyTypeDao.insert(record);
    }
    public void update(CompanyType record) {
        companyTypeDao.updateByPrimaryKeySelective(record);
    }
    public void deleteById(Long id) {
        companyTypeDao.deleteByPrimaryKey(id);
    }
    public Pager<T> selectByAll(Map<String, Integer> params) {
        List<T> list =companyTypeDao.selectByAll((params.get("page")-1)*params.get("size"),params.get("size"));
        Pager<T> pager = pagerText(params,list);
        return pager;
    }

    @Override
    public List<CompanyType> findAll() {
        return companyTypeDao.findAll();
    }
}
