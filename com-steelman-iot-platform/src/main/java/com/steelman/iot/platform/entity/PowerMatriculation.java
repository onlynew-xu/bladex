package com.steelman.iot.platform.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_power_matriculation
 * @author 
 */
@Data
public class PowerMatriculation implements Serializable {
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 母联柜状态 0 关闭 1 打开
     */
    private Integer status;

    /**
     * 1变压器Id
     */
    private Long firstTransformerId;

    /**
     * 2变压器Id
     */
    private Long secondTransformerId;

    /**
     * 配电房Id
     */
    private Long powerId;

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