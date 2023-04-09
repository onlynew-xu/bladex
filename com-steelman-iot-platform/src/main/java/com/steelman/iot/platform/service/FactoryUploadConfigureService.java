package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.FactoryUploadConfigure;

public interface FactoryUploadConfigureService {
    /**
     * 获取能耗公司的配置信息
     * @param factoryInfoId
     * @return
     */
    FactoryUploadConfigure getByFactoryId(Long factoryInfoId);

    /**
     * 更新能耗数据
     * @param factoryUploadUpdate
     * @return
     */
    int updateFactoryUpload(FactoryUploadConfigure factoryUploadUpdate);
}
