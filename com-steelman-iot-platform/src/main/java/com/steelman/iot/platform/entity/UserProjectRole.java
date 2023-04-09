package com.steelman.iot.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * sys_user_project_role
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="表描述", description="")
public class UserProjectRole implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 用户Id
     */
    @ApiModelProperty(value = "用户Id")
    private Long userId;

    /**
     * 项目Id
     */
    @ApiModelProperty(value = "项目Id")
    private Long projectId;

    /**
     * 角色Id
     */
    @ApiModelProperty(value = "角色Id")
    private Long roleId;

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