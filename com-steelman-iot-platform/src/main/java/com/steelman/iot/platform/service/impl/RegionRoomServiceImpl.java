package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.RoomDao;
import com.steelman.iot.platform.entity.Pager;
import com.steelman.iot.platform.entity.Room;
import com.steelman.iot.platform.entity.dto.EntityDto;
import com.steelman.iot.platform.service.RegionRoomService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tang
 * date 2020-11-25
 */
@Service("regionRoomService")
public class RegionRoomServiceImpl extends BaseService implements RegionRoomService {
    @Resource
    private RoomDao roomDao;

    public void insert(Room area) {
        roomDao.insert(area);
    }
    public void update(Room area) {
        roomDao.updateByPrimaryKeySelective(area);
    }
    public void deleteById(Long id) {
        roomDao.deleteByPrimaryKey(id);
    }
    public Pager<T> selectByAll(Map<String, Integer> params) {
        List<T> list = roomDao.selectByAll((params.get("page")-1)*params.get("size"),params.get("size"));
        Pager<T> pager = pagerText(params,list);
        return pager;
    }
    public void deleteByRoom(Room area) {
        roomDao.deleteByRoom(area);
    }

    @Override
    public List<Room> selectByProjectId(Long projectId) {
        return roomDao.selectByProjectId(projectId);
    }

    @Override
    public Room selectByPrimaryKey(Long roomId) {
        return roomDao.selectByPrimaryKey(roomId);
    }

    @Override
    public Room getByStoreyAndRoom(Long storeyId, Long roomId) {
        return roomDao.getByStoreyAndRoom(storeyId,roomId);
    }

    @Override
    public List<Room> selectByProjectIdAndStoreyId(Long projectId, Long storeyId) {
        return roomDao.selectByProjectIdAndStoreyId(projectId,storeyId);
    }

    @Override
    public Boolean deleteByRoomId(Long projectId, Long roomId) {
        //toDo
        roomDao.deleteByPrimaryKey(roomId);
        return true;
    }

    @Override
    public List<EntityDto> selectInfoByProjectId(Long projectId) {
        return roomDao.selectInfoByProjectId(projectId);
    }

    @Override
    public Map<Long, String> getRoomMap(Long projectId) {
       List<Room> roomList= roomDao.getByProjectId(projectId);
       Map<Long,String> roomMap=new LinkedHashMap<>();
       if(!CollectionUtils.isEmpty(roomList)){
           for (Room room : roomList) {
               roomMap.put(room.getId(),room.getName());
           }
       }
        return roomMap;
    }
}
