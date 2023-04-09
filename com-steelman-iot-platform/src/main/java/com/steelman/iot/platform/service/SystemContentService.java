package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.SystemContent;

public interface SystemContentService {

    /**
     * 保存
     * @param systemContent
     * @return
     */
    int save(SystemContent systemContent);

    /**
     * 修改主页内容
     * @param systemContent
     */
    int updateById(SystemContent systemContent);

    /**
     * 获取项目系统主页详情
     * @param projectId
     * @param systemId
     * @return
     */
    SystemContent getByProjectIdAndSystemId(Long projectId, Long systemId);
}
