package com.steelman.iot.platform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_energy_upload
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyUpload implements Serializable {
    private Long id;

    /**
     * 名称
     */
    private Long projectId;

    /**
     * 系统上报类型 1:日 2 月
     */
    private Integer type;

    /**
     * 状态  -1 发生了异常 0 上传失败 1:上传成功
     */
    private Integer status;

    /**
     * 上报能耗的具体天 yyyy-MM-dd
     */
    private String day;

    /**
     * 上传时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}