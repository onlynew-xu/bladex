package com.steelman.iot.platform.entity.vo;

import com.steelman.iot.platform.entity.dto.DeviceDataSmartElecFloatDto;
import com.steelman.iot.platform.entity.dto.PowerFacility;
import com.steelman.iot.platform.entity.dto.TransformLoadDto;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author lsj
 * @DATE 2021/4/14 0014 10:00
 * @Description:
 */
@Data
public class PowerInfoVo {

   private List<TransformLoadDto> transformInfo;

   private Integer dataFlag;

    private DeviceDataSmartElecFloatDto  smartData;

    private Map<String,Object> voltDataMap;

    private Map<String,Object> ampDataMap;

    private Map<String,Object> factorDataMap;

    private List<PowerFacility> powerFacilityList;


}
