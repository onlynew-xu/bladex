package com.steelman.iot.platform.entity.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author lsj
 * @DATE 2021/4/13 0013 10:07
 * @Description:
 */
@Data
public class PowerDataVo {
    private Long id;
    private String name;
    private Long cabinetCount;
    private String temperature;
    private String humidity;
    private BigDecimal transLoad;

    private List<DeviceCenterVo> alarmList;
    private List<DeviceCenterVo> offLineList;
    private List<DeviceCenterVo> allList;
    private List<DeviceCenterVo>incomingList;
    private List<DeviceCenterVo> transformerList;
    private List<DeviceCenterVo> compensateList;
    private List<DeviceCenterVo> waveList;
    private List<DeviceCenterVo> singleFeederList;
    private List<DeviceCenterVo> multiFeederList;
    private List<DeviceCenterVo> matriculationList;
    private List<DeviceCenterVo> boxList;
    private List<DeviceCenterVo> generatorList;






}
