package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * iot_power_generator_device
 * @author 
 */
@Data
public class PowerGeneratorDevice implements Serializable {
    private Long id;

    /**
     * 进线柜Id
     */
    private Long generatorId;

    /**
     * 设备Id
     */
    private Long deviceId;

    /**
     * 配电房Id
     */
    private Long powerId;

    /**
     * 项目Id
     */
    private Long projectId;

    /**
     * 生成时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}