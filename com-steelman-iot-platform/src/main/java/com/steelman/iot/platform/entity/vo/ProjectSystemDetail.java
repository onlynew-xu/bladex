package com.steelman.iot.platform.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSystemDetail implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 项目Id
     */
    @ApiModelProperty(value = "项目Id")
    private Long projectId;

    /**
     * 系统Id
     */
    @ApiModelProperty(value = "系统Id")
    private Long systemId;

    /**
     * 系统名称
     */
    @ApiModelProperty(value = "系统名称")
    private String systemName;

    /**
     * 状态 0 未启用 1 已启用
     */
    @ApiModelProperty(value = "状态 0 未启用 1 已启用")
    private Integer status;

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
