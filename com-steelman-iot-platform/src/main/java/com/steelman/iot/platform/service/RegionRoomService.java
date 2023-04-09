package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.Pager;
import com.steelman.iot.platform.entity.Room;
import com.steelman.iot.platform.entity.dto.EntityDto;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

/**
 * @author tang
 * date 2020-11-23
 */
public interface RegionRoomService {

    void insert(Room area);
    void update(Room area);
    void deleteById(Long id);
    Pager<T> selectByAll(Map<String, Integer> params);
    void deleteByRoom(Room area);

    List<Room> selectByProjectId(Long projectId);

    /**
     * 根据主键 获取房间信息
     * @param roomId
     * @return
     */
    Room selectByPrimaryKey(Long roomId);

    /**
     * 获取房间信息
     * @param storeyId
     * @param roomId
     * @return
     */
    Room getByStoreyAndRoom(Long storeyId, Long roomId);

    List<Room> selectByProjectIdAndStoreyId(Long projectId, Long storeyId);

    /**
     * 删除房间信息
     * @param projectId
     * @param roomId
     * @return
     */
    Boolean deleteByRoomId(Long projectId, Long roomId);

    /**
     * 获取所有房间信息
     * @param projectId
     * @return
     */
    List<EntityDto> selectInfoByProjectId(Long projectId);

    Map<Long, String> getRoomMap(Long projectId);
}
