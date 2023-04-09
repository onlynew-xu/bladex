package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.Company;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public interface CompanyDao {
    int deleteByPrimaryKey(Long id);

    int insert(Company record);

    int insertSelective(Company record);

    Company selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Company record);

    int updateByPrimaryKey(Company record);

    List<T> selectByAll(int page, int size);

    /**
     * 根据项目获取所有公司
     * @param projectId
     * @param type
     * @return
     */
    List<Company> findByProjectId(@Param("projectId") Long projectId,@Param("type") Integer type);

    /**
     * 根据公司名称 获取公司
     * @param projectId
     * @param companyName
     * @return
     */
    Company findByName(@Param("projectId") Long projectId,@Param("companyName") String companyName);
}