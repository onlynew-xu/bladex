package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.InspectionTask;
import com.steelman.iot.platform.entity.Pager;
import com.steelman.iot.platform.entity.dto.InspectionTaskDto;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

public interface InspectionTaskService {
    /**
     * 获取用户未处理的巡检任务的条数
     * @param projectId
     * @param userId
     * @return
     */
    Integer getInHandlerCount(Long projectId,Long userId );

    /**
     * 获取用户所有的任务
     * @param projectId
     * @param userId
     * @return
     */
    /*Map<String, List<InspectionTaskDto>> getAllTask(Long projectId, Long userId);*/

    /**
     * 更新任务的状态
     * @param projectId
     * @param userId
     * @param taskId
     * @param status
     * @return
     */
    /*Boolean updateStatus(Long projectId, Long userId, Long id, Integer status);*/

    /**
     * 删除任务
     * @param userId
     * @param taskId
     * @return
     */
    Boolean deleteTask(Long userId,Long taskId);
    /*查策略任务列表*/
    Pager<T> getAllTaskBy(Map<String, Integer> params,Long userId, Long projectId, Integer status);

    Boolean updateStatus(InspectionTask task);

    void save(InspectionTask inspectionTask);

    int saveList(List<InspectionTask> inspectionTaskList);

    List<InspectionTask> getInitTask();

    int updateStatusList(List<InspectionTask> taskList);

    List<InspectionTaskDto> getUserTask(Long projectId, Long userId, Integer status);

    Boolean updateTask(InspectionTask task);
}
