package com.steelman.iot.platform.largescreen.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceStatusStatistic {

    private Integer totalCount;

    private Integer inLineCount;

    private Integer outLineCount;

    private Integer alarmCount;

    private String handlerPer;
}
