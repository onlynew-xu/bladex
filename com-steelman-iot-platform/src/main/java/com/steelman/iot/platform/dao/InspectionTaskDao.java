package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.InspectionTask;
import com.steelman.iot.platform.entity.dto.InspectionTaskDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

@Mapper
public interface InspectionTaskDao {
    int deleteByPrimaryKey(Long id);

    int insert(InspectionTask record);

    int insertSelective(InspectionTask record);

    InspectionTask selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InspectionTask record);

    int updateByPrimaryKey(InspectionTask record);

    /**
     * 获取用户未处理的巡检任务的次数
     * @param projectId
     * @param userId
     * @return
     */
    Integer getInHandlerCount(@Param("projectId") Long projectId, @Param("userId") Long userId);




    /**
     * 删除任务
     * @param userId
     * @param taskId
     * @return
     */
    Integer deleteByPrimaryKeyAndUserId(@Param("userId") Long userId,@Param("taskId") Long taskId);


    List<T> selectTaskListByAll(int page, int size,Long userId, Long projectId, Integer status);

    void updateStatus(InspectionTask task);

    int saveList(@Param("inspectionTaskList") List<InspectionTask> inspectionTaskList);

    List<InspectionTask> getInitTask();

    int updateStatusList(@Param("taskList") List<InspectionTask> taskList);

    List<InspectionTaskDto> getUserTask(@Param("projectId") Long projectId, @Param("userId") Long userId,  @Param("status") Integer status);

    int getInHandlerCountNum(Long projectId);
}