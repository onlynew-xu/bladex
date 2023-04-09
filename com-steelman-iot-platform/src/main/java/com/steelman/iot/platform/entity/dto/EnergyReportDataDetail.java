package com.steelman.iot.platform.entity.dto;

import com.steelman.iot.platform.energyManager.dto.EquipmentMeasure;
import com.steelman.iot.platform.energyManager.dto.TotalMeasure;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyReportDataDetail {
    List<EquipmentReportInfo> equipmentReportInfos;
    private String yearTotal;
    private String jiTotal;
    private String dayTotal;
    private String yearCoTotal;
    private TotalMeasure totalMeasure;
    private List<EquipmentMeasure>  equipmentMeasure;
}
