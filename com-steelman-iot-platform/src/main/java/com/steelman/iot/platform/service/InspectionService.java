package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.Inspection;
import com.steelman.iot.platform.entity.Pager;
import com.steelman.iot.platform.entity.dto.InspectionDto;
import com.steelman.iot.platform.entity.vo.InspectionInfo;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

/**
 * @Author lsj
 * @DATE 2021/3/31 0031 16:58
 * @Description:
 */
public interface InspectionService {
    void save(Inspection inspection);

    void revamp(Inspection inspection);

    void delete(Long id);

    List<InspectionInfo> getList(Long projectId);
    
    List<InspectionDto> getDtoList(Long projectId);

    Pager<T> selectByAll(Map<String, Integer> params,Long userId, Long projectId);

    List<Inspection> getActiveInspectionInfo();

    InspectionDto getById(Long inspectionId);
}
