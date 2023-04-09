package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.Room;
import com.steelman.iot.platform.entity.dto.EntityDto;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public interface RoomDao {
    int deleteByPrimaryKey(Long id);

    int insert(Room record);

    int insertSelective(Room record);

    Room selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Room record);

    int updateByPrimaryKey(Room record);

    List<T> selectByAll(int page, int size);

    int deleteByRoom(Room record);

    List<Room> selectByProjectId(@Param("projectId") Long projectId);

    Room getByStoreyAndRoom(@Param("storeyId") Long storeyId, @Param("roomId") Long roomId);

    /**
     * 获取某层的房间信息
     * @param projectId
     * @param storeyId
     * @return
     */
    List<Room> selectByProjectIdAndStoreyId(@Param("projectId") Long projectId, @Param("storeyId") Long storeyId);

    /**
     * 获取房间信息
     * @param projectId
     * @return
     */
    List<EntityDto> selectInfoByProjectId(@Param("projectId") Long projectId);

    List<Room> getByProjectId(@Param("projectId") Long projectId);
}