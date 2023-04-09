package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.CompanyDao;
import com.steelman.iot.platform.entity.Company;
import com.steelman.iot.platform.entity.Pager;
import com.steelman.iot.platform.service.CompanyService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author tang
 * date 2020-11-25
 */
@Service("companyService")
public class CompanyServiceImpl extends BaseService implements CompanyService {
    @Resource
    private CompanyDao companyDao;

    public void insert(Company record) {
        companyDao.insert(record);
    }
    public void update(Company record) {
        companyDao.updateByPrimaryKeySelective(record);
    }
    public void deleteById(Long id) {
        companyDao.deleteByPrimaryKey(id);
    }
    public Pager<T> selectByAll(Map<String, Integer> params) {
        List<T> list =companyDao.selectByAll((params.get("page")-1)*params.get("size"),params.get("size"));
        Pager<T> pager = pagerText(params,list);
        return pager;
    }
    public Company findByid(Long id) {
       return companyDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Company> findByProjectId(Long projectId, Integer type) {
        if(type.equals(0)){
            type=null;
        }
        return companyDao.findByProjectId(projectId,type);
    }

    @Override
    public void removeById(Long companyId) {
         companyDao.deleteByPrimaryKey(companyId);
    }

    @Override
    public Company findByName(Long projectId, String companyName) {
        return companyDao.findByName(projectId,companyName);
    }
}
