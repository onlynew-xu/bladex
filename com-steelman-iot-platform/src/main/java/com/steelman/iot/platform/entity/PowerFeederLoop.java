package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * iot_power_feeder_loop
 * @author 
 */
@Data
public class PowerFeederLoop implements Serializable {
    private Long id;

    /**
     * 回路名称
     */
    private String name;

    /**
     * 总回路标志 0 不是总回路  1:是总回路
     */
    private Integer totalFlag;

    /**
     * 馈线柜Id
     */
    private Long feederId;

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