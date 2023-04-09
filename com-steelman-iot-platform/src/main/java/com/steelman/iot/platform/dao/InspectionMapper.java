package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.Inspection;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface InspectionMapper {

    List<Inspection> getInspectionInfo();

    List<Inspection> getActiveInspectionInfo();
}
