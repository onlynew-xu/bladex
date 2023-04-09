package com.steelman.iot.platform.largescreen.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmCountInfo implements Serializable {
    private Long projectId;
    private Long systemId;
    private Long areaId;
    private String areaName;
    private Long buildingId;
    private String buildingName;
    private String year;
    private String month;
    private Integer count;
}
