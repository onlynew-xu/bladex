package com.steelman.iot.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_power_picture
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "电房图片信息实体类")
public class PowerPicture implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 电房Id
     */
    @ApiModelProperty(value = "电房Id")
    private Long powerId;

    /**
     * 实景图
     */
    @ApiModelProperty(value = "实景图地址")
    private String livingPicture;

    /**
     * 系统图
     */
    @ApiModelProperty(value = "系统图地址")
    private String systemPicture;

    /**
     * 生成时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}