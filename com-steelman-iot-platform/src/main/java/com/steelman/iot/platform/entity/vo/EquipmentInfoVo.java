package com.steelman.iot.platform.entity.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author lsj
 * @DATE 2021/4/10 0010 10:02
 * @Description:
 */
@Data
public class EquipmentInfoVo extends EquipmentItemVo {
    private String consumeTypeName;
    private String energyTypeName;
    private String name;
    private String location;
    private List<Map<String, Object>> totalEnergy;
    private List<Map<String,Object>>trendEnergy;
    private List<Map<String,Object>> lastTrendEnergy;
}
