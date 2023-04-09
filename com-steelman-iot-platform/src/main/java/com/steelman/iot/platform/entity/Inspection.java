package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * iot_inspection
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class Inspection implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 任务标题
     */
    @ApiModelProperty(value = "任务标题")
    private String title;

    /**
     * 循环类型 0:每周 1:每月
     */
    @ApiModelProperty(value = "循环类型 0:每周 1:每月")
    private Integer evenType;

    /**
     * 每周
     */
    @ApiModelProperty(value = "每周")
    private String evenWeek;

    /**
     * 每月
     */
    @ApiModelProperty(value = "每月")
    private String evenMonth;

    /**
     * 任务内容
     */
    @ApiModelProperty(value = "任务内容")
    private String content;

    /**
     * 项目维保员Id
     */
    @ApiModelProperty(value = "项目维保员Id")
    private Long userId;

    /**
     * 生效日期
     */
    @ApiModelProperty(value = "生效日期")
    private Date beginTime;

    /**
     * 失效日期
     */
    @ApiModelProperty(value = "失效日期")
    private Date endTime;

    /**
     * 状态 0:未启用 1:已启用
     */
    @ApiModelProperty(value = "状态 0:未启用 1:已启用")
    private Integer status;

    /**
     * 项目Id
     */
    @ApiModelProperty(value = "项目Id")
    private Long projectId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}