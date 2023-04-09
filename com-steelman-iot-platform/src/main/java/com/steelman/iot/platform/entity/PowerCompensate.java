package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * iot_power_compensate
 * @author 
 */
@Data
public class PowerCompensate implements Serializable {
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 变压器Id
     */
    private Long transformerId;

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