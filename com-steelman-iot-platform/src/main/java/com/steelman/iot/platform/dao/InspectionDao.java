package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.Inspection;
import com.steelman.iot.platform.entity.dto.InspectionDto;
import com.steelman.iot.platform.entity.vo.InspectionInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public interface InspectionDao {
    int deleteByPrimaryKey(Long id);

    int insert(Inspection record);

    int insertSelective(Inspection record);

    Inspection selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Inspection record);

    int updateByPrimaryKey(Inspection record);

    List<InspectionInfo> selectByProjectId(Long projectId);

    List<T> selectByAll(int page, int size,Long userId, Long projectId);

    List<Inspection> getActiveInspectionInfo();

    List<InspectionDto> getDtoList(@Param("projectId") Long projectId);

    InspectionDto getDtoById(@Param("inspectionId") Long inspectionId);
}