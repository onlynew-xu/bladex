package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.CompanyType;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public interface CompanyTypeDao {
    int deleteByPrimaryKey(Long id);

    int insert(CompanyType record);

    int insertSelective(CompanyType record);

    CompanyType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CompanyType record);

    int updateByPrimaryKey(CompanyType record);

    List<T> selectByAll(int page, int size);

    List<CompanyType> findAll();
}