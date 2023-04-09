package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.InspectionTaskDao;
import com.steelman.iot.platform.entity.InspectionTask;
import com.steelman.iot.platform.entity.Pager;
import com.steelman.iot.platform.entity.dto.InspectionTaskDto;
import com.steelman.iot.platform.entity.dto.UserProjectRoleDto;
import com.steelman.iot.platform.service.InspectionTaskService;
import com.steelman.iot.platform.service.UserProjectRoleService;
import com.steelman.iot.platform.service.UserService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("inspectionTaskService")
public class InspectionTaskServiceImpl extends BaseService implements InspectionTaskService {
    @Resource
    private InspectionTaskDao inspectionTaskDao;
    @Resource
    private UserService userService;
    @Resource
    private UserProjectRoleService userProjectRoleService;

    @Override
    public Integer getInHandlerCount(Long projectId, Long userId) {
        UserProjectRoleDto userProjectRoleDto = userProjectRoleService.findByUserIdAndProjectId(userId, projectId);
        if(userProjectRoleDto.getRoleId().equals(1l)){
            return inspectionTaskDao.getInHandlerCountNum(projectId);
        }else{
            return inspectionTaskDao.getInHandlerCount(projectId, userId);
        }
    }

    /*@Override
    public Map<String, List<InspectionTaskDto>> getAllTask(Long projectId, Long userId) {
        List<InspectionTask> inspectionTaskList=inspectionTaskDao.getAllTask(projectId,userId);
        User user=userService.findById(userId);
        Map<String, List<InspectionTaskDto>> data=new LinkedHashMap<>();
        if(!CollectionUtils.isEmpty(inspectionTaskList)){
            List<InspectionTaskDto> allTask=new ArrayList<>();
            List<InspectionTaskDto> unDoingTask=new ArrayList<>();
            List<InspectionTaskDto> doingTask=new ArrayList<>();
            List<InspectionTaskDto> completeTask=new ArrayList<>();
            List<InspectionTaskDto> timeoutTask=new ArrayList<>();
            for (InspectionTask inspectionTask : inspectionTaskList) {
                Integer status = inspectionTask.getStatus();
                InspectionTaskDto inspectionTaskDto=new InspectionTaskDto();
                inspectionTaskDto.setId(inspectionTask.getId());
                inspectionTaskDto.setTitle(inspectionTask.getTitle());
                inspectionTaskDto.setContent(inspectionTask.getContent());
                inspectionTaskDto.setCreateTime(inspectionTask.getCreateTime());
                inspectionTaskDto.setDoingTime(inspectionTask.getDoingTime());
                inspectionTaskDto.setStatus(status);
                inspectionTaskDto.setUserId(user.getId());
                inspectionTaskDto.setUsername(user.getUsername());
                allTask.add(inspectionTaskDto);
                if(status.equals(0)){
                    unDoingTask.add(inspectionTaskDto);
                }else if(status.equals(1)){
                    doingTask.add(inspectionTaskDto);
                }else if(status.equals(2)){
                    completeTask.add(inspectionTaskDto);
                }else{
                    timeoutTask.add(inspectionTaskDto);
                }
            }
            data.put("all",allTask);
            if(!CollectionUtils.isEmpty(unDoingTask)){
                data.put("undo",unDoingTask);
            }
            if(!CollectionUtils.isEmpty(doingTask)){
                data.put("doing",unDoingTask);
            }
            if(!CollectionUtils.isEmpty(completeTask)){
                data.put("complete",completeTask);
            }
            if(!CollectionUtils.isEmpty(timeoutTask)){
                data.put("timeout",timeoutTask);
            }
            return data;
        }
        return null;
    }*/

   /* @Override
    public Boolean updateStatus(Long projectId, Long userId, Long taskId, Integer status) {
        Boolean flag=false;
        if(status.equals(0)||status.equals(1)){
            status= status+1;
            Date date=new Date();
            Integer update =inspectionTaskDao.updateStatus(projectId,userId,taskId,status,date);
            if(update>0){
                flag=true;
            }
        }
        return flag;
    }*/

    @Override
    public Boolean deleteTask(Long userId, Long taskId) {
        InspectionTask inspectionTask = inspectionTaskDao.selectByPrimaryKey(taskId);
        UserProjectRoleDto byUserIdAndProjectId = userProjectRoleService.findByUserIdAndProjectId(userId, inspectionTask.getProjectId());
        Long roleId = byUserIdAndProjectId.getRoleId();
        if(roleId.equals(1l)){
            Integer update = inspectionTaskDao.deleteByPrimaryKeyAndUserId(userId, taskId);
            if (update > 0) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Pager<T> getAllTaskBy(Map<String, Integer> params, Long userId, Long projectId, Integer status) {
        List<T> list = inspectionTaskDao.selectTaskListByAll((params.get("page") - 1) * params.get("size"), params.get("size"), userId, projectId, status);
        Pager<T> pager = pagerText(params, list);
        return pager;
    }

    @Override
    public Boolean updateStatus(InspectionTask task) {
        inspectionTaskDao.updateStatus(task);
        return true;
    }

    @Override
    public void save(InspectionTask inspectionTask) {
        inspectionTaskDao.insertSelective(inspectionTask);
    }

    @Override
    public int saveList(List<InspectionTask> inspectionTaskList) {
        return inspectionTaskDao.saveList(inspectionTaskList);
    }

    @Override
    public List<InspectionTask> getInitTask() {
        return inspectionTaskDao.getInitTask();
    }

    @Override
    public int updateStatusList(List<InspectionTask> taskList) {
        return inspectionTaskDao.updateStatusList(taskList);
    }

    @Override
    public List<InspectionTaskDto> getUserTask(Long projectId, Long userId, Integer status) {
        if(status.equals(4)){
            status=null;
        }
        UserProjectRoleDto byUserIdAndProjectId = userProjectRoleService.findByUserIdAndProjectId(userId, projectId);
        if(byUserIdAndProjectId.getRoleId().equals(1l)){
            userId=null;
        }
        List<InspectionTaskDto> userTask = inspectionTaskDao.getUserTask(projectId, userId, status);
        return  userTask;
    }

    @Override
    public Boolean updateTask(InspectionTask task) {
         inspectionTaskDao.updateByPrimaryKeySelective(task);
         return true;
    }
}
