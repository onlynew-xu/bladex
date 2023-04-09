package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * iot_company_type
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class CompanyType implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 类型名称
     */
    @ApiModelProperty(value = "类型名称")
    private String name;

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