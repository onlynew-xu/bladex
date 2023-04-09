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
public class AlarmHandleStatus implements Serializable {
    /**
     * 已处理数量
     */
    private Integer handlerCount;
    /**
     * 未处理数量
     */
    private Integer inHandleCount;
    /**
     * 报警总数
     */
    private Integer total;
    /**
     * 已处理百分比
     */
    private String percent;
}
