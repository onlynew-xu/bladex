package com.steelman.iot.platform.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_factory_info
 * @author 
 */
@Data
public class FactoryInfo implements Serializable {
    private Long id;

    /**
     * 公司名称
     */
    private String name;

    /**
     * 统一社会信用代码
     */
    private String code;

    /**
     * 单位类型
     */
    private String typeCode;

    /**
     * 单位类型名称
     */
    private String typeName;

    /**
     * 行业编码
     */
    private String industryCode;

    /**
     * 区域编码
     */
    private String regionCode;

    /**
     * 地区名
     */
    private String regionName;

    /**
     * 是否央企 0为否 1为是
     */
    private String center;

    /**
     * 统一社会信用代码
     */
    private String corporationCode;

    /**
     * 是否能源加工转换类企业0为否 1为是
     */
    private String jgzh;

    /**
     * 能源消费分类 1:5000 吨以下, 2:5000~10000 吨，3:1 万-10 万吨，4:10 万-50 万吨，5:50 万-300 万吨，6:300 万吨以上
     */
    private String energyConsumeLevel;

    /**
     * 厂址中心纬度
     */
    private String latitude;

    /**
     * 厂址中心经度
     */
    private String longitude;

    /**
     * 企业联系电话
     */
    private String phone;

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