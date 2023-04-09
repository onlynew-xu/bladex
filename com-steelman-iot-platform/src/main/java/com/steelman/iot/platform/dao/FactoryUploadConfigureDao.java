package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.FactoryUploadConfigure;
import org.apache.ibatis.annotations.Param;

public interface FactoryUploadConfigureDao {
    int deleteByPrimaryKey(Long id);

    int insert(FactoryUploadConfigure record);

    int insertSelective(FactoryUploadConfigure record);

    FactoryUploadConfigure selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FactoryUploadConfigure record);

    int updateByPrimaryKey(FactoryUploadConfigure record);

    /**
     * 获取能源公司上传配置信息
     * @param factoryInfoId
     * @return
     */
    FactoryUploadConfigure getByFactoryId(@Param("factoryInfoId") Long factoryInfoId);

    /**
     * 更新能源公司上传配置信息
     * @param factoryUploadUpdate
     * @return
     */
    int updateFactoryUpload(@Param("factoryUploadUpdate") FactoryUploadConfigure factoryUploadUpdate);
}