package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * iot_power_feeder_loop_device
 * @author 
 */
@Data
public class PowerFeederLoopDevice implements Serializable {
    private Long id;

    /**
     * 馈线柜Id
     */
    private Long feederId;

    /**
     * 回路id
     */
    private Long feederLoopId;

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
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}