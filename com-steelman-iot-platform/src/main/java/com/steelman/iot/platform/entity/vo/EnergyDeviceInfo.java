package com.steelman.iot.platform.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author lsj
 * @DATE 2021/4/7 0007 16:40
 * @Description: 能耗设备排行榜 信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyDeviceInfo {
    private Long id;
    private String name;
    private String energyTypeName;
    private String consumeTypeName;
    private Float totalData;
    private String location;
}
