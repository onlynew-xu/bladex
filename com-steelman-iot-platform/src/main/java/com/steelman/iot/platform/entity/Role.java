package com.steelman.iot.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * sys_role
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class Role implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String name;
    /**
     * 角色名称(中文)
     */
    @ApiModelProperty(value = "角色名称(中文显示使用)")
    private String definition;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 级别 1  管理员  2 项目管理员(可操作) 3 项目管理员(不可操作项目和人员) 4 项目操作员  5 项目监察员 6运维人员
     */
    @ApiModelProperty(value = "级别 1  管理员  2 项目管理员(可操作) 3 项目管理员(不可操作项目和人员) 4 项目操作员  5 项目监察员 6运维人员")
    private Integer level;

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