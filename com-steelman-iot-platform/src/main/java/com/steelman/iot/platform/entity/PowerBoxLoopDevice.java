package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * iot_power_box_loop_device
 * @author 
 */
@Data
public class PowerBoxLoopDevice implements Serializable {
    private Long id;

    /**
     * 配电箱Id
     */
    private Long boxId;

    /**
     * 回路Id
     */
    private Long boxLoopId;

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