package com.steelman.iot.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_inspection_task
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class InspectionTask implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 任务标题
     */
    @ApiModelProperty(value = "任务标题")
    private String title;

    /**
     * 任务内容
     */
    @ApiModelProperty(value = "任务内容")
    private String content;

    /**
     * 巡检策略Id
     */
    @ApiModelProperty(value = "巡检策略Id")
    private Long inspectionId;

    /**
     * 项目维保人员Id
     */
    @ApiModelProperty(value = "项目维保人员Id")
    private Long userId;

    /**
     * 任务状态 0:待执行 1 执行中 2 已完成 3 已过期
     */
    @ApiModelProperty(value = "任务状态 0:待执行 1 执行中 2 已完成 3 已过期")
    private Integer status;

    /**
     * 生成时间
     */
    @ApiModelProperty(value = "生成时间")
    private Date createTime;

    /**
     * 应该执行时间
     */
    @ApiModelProperty(value = "应该执行时间")
    private Date doingTime;

    /**
     * 开始执行时间
     */
    @ApiModelProperty(value = "执行时间")
    private Date beginTime;

    /**
     * 执行结束时间
     */
    @ApiModelProperty(value = "执行时间")
    private Date finishTime;

    /**
     * 项目Id
     */
    @ApiModelProperty(value = "项目Id")
    private Long projectId;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;



}