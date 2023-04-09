package com.steelman.iot.platform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_power_box
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PowerBox implements Serializable {
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 配电箱类型 0 单回路配电箱 1 多回路配电箱
     */
    private Integer loopType;

    /**
     * 父类型 0 馈线柜 1 配电箱
     */
    private Integer parentType;

    /**
     * 父馈线柜Id
     */
    private Long feederId;

    /**
     * 父馈线柜回路
     */
    private Long feederLoopId;

    /**
     * 父配电箱Id
     */
    private Long boxId;

    /**
     * 父配电箱回路
     */
    private Long boxLoopId;

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