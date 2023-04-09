package com.steelman.iot.platform.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_factory_upload_configure
 * @author 
 */
@Data
public class FactoryUploadConfigure implements Serializable {
    private Long id;

    /**
     * 统一社会信用代码
     */
    private String enterpriseCode;

    /**
     * 行政区划代码
     */
    private String region;

    /**
     * 注册地址
     */
    private String registerUrl;

    /**
     * 端设备统一编码
     */
    private String deviceId;

    /**
     * 用能单位基础信息配置上传地址
     */
    private String centerInfoUrl;

    /**
     * 采集数据上传地址
     */
    private String centerDataUrl;

    /**
     * 基础数据下载地址
     */
    private String centerInfoDownloadUrl;

    /**
     * 采集数据下载地址
     */
    private String centerDataDownloadUrl;

    /**
     * 能源公司Id
     */
    private Long factoryId;

    /**
     * 项目Id
     */
    private Long projectId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}