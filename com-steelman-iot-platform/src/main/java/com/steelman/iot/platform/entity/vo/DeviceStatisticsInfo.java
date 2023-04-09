package com.steelman.iot.platform.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author tang
 * date 2020-12-02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceStatisticsInfo implements Serializable {
    /**
     * 设备类型Id
     */
    private Long deviceTypeId;
    /**
     * 设备类型的名称
     */
    private String deviceTypeName;
    /**
     * 设备在线数
     */
    private Integer InlineCount;
    /**
     * 设备离线数
     */
    private Integer outlineCount;
    /**
     * 设备总数
     */
    private Integer total;
    /**
     * 设备在线率
     */
    private String percent;
}
