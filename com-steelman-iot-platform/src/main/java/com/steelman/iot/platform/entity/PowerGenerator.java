package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * iot_power_generator
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class PowerGenerator implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String brand;

    /**
     * 发电量
     */
    @ApiModelProperty(value = "发电量")
    private String powerGeneration;

    /**
     * 发电机状态 0 关闭 1 打开
     */
    @ApiModelProperty(value = "发电机状态 0 关闭 1 打开")
    private Integer status;

    /**
     * 变压器Id
     */
    @ApiModelProperty(value = "变压器Id")
    private Long transformerId;

    /**
     * 配电房Id
     */
    @ApiModelProperty(value = "配电房Id")
    private Long powerId;

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