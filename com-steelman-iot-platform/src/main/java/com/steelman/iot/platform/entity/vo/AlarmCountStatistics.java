package com.steelman.iot.platform.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tang
 * date 2020-12-02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmCountStatistics {
    private String weekTime;
    private String dateTime;
    private Integer count;
}
