package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.Storey;
import com.steelman.iot.platform.entity.dto.EntityDto;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public interface StoreyDao {
    int deleteByPrimaryKey(Long id);

    int insert(Storey record);

    int insertSelective(Storey record);

    Storey selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Storey record);

    int updateByPrimaryKey(Storey record);

    List<T> selectByAll(int page, int size, Long buildingId);

    int deleteByStorey(Storey record);

    List<Storey> selectByProjectId(@Param("projectId") Long projectId);

    Storey getByBuildingAndStorey(@Param("buildingId") Long buildingId, @Param("storeyId") Long storeyId);

    void deletePictureById(Storey record);

    void deletePictureCadById(Storey record);

    void addFloorCenterPicture(Storey storeypicture);

    /**
     * 根据楼 获取所有层信息
     * @param projectId
     * @param buildingId
     * @return
     */
    List<Storey> getByProjectAndBuildind(@Param("projectId") Long projectId, @Param("buildingId") Long buildingId);

    /**
     * 获取所有层信息
     * @param projectId
     * @return
     */
    List<EntityDto> getStoreyInfoList(@Param("projectId") Long projectId);

    List<Storey> getByProjectId(@Param("projectId") Long projectId);
}