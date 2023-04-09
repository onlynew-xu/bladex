package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.Inspection;
import com.steelman.iot.platform.entity.InspectionTask;
import com.steelman.iot.platform.entity.dto.EntityDto;
import com.steelman.iot.platform.entity.dto.InspectionDto;
import com.steelman.iot.platform.service.InspectionService;
import com.steelman.iot.platform.service.InspectionTaskService;
import com.steelman.iot.platform.service.UserService;
import com.steelman.iot.platform.utils.CommonUtils;
import com.steelman.iot.platform.utils.ExceptionUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import com.steelman.iot.platform.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Author lsj
 * @DATE 2021/3/31 0031 16:30
 * @Description:
 */
@Api(tags = "巡检管理")
@RequestMapping("/api/inspection")
@Slf4j
@RestController
@Component
public class InspectionController extends BaseController {

    @Resource
    private InspectionService inspectionService;
    @Resource
    private InspectionTaskService taskService;
    @Resource
    private UserService userService;


    @PostMapping(value = "/user/list", produces = CommonUtils.MediaTypeJSON)
    public String userList(@RequestBody Map<String,Object> paramMap){
        Result<List<EntityDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
               result= Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                List<EntityDto> entityDtoList=userService.getUserProject(projectId);
                if(CollectionUtils.isEmpty(entityDtoList)){
                    result=Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(entityDtoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/inspection/user/list"));
        }
        Type type=new TypeToken<Result<List<EntityDto>>>(){}.getType();
        return  JsonUtils.toJson(result,type);
    }




    @ApiOperation("添加巡检策略")
    @PostMapping(value = "/save", produces = CommonUtils.MediaTypeJSON)
    public String saveInspection(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object titleObj = paramMap.get("title");
            Object evenTypeObj = paramMap.get("evenType");
            Object evenWeekObj = paramMap.get("evenWeek");
            Object evenMonthObj = paramMap.get("evenMonth");
            Object contentObj = paramMap.get("content");
            Object userIdObj = paramMap.get("userId");
            Object beginTimeObj = paramMap.get("beginTime");
            Object endTimeObj = paramMap.get("endTime");
            Object projectIdObj = paramMap.get("projectId");
            if(titleObj==null||evenTypeObj==null||contentObj==null||userIdObj==null||beginTimeObj==null||endTimeObj==null||projectIdObj==null){
                result=Result.paramError(result);
            }else{
                String title=titleObj.toString();
                Integer evenType=Integer.parseInt(evenTypeObj.toString());
                Long projectId=Long.parseLong(projectIdObj.toString());
                Long userId=Long.parseLong(userIdObj.toString());
                String  beginTime=beginTimeObj.toString();
                String  endTime=endTimeObj.toString();
                String  content=contentObj.toString();
                Inspection inspection = new Inspection();
                inspection.setTitle(title);
                inspection.setProjectId(projectId);
                inspection.setTitle(title);
                inspection.setUserId(userId);
                inspection.setEvenType(evenType);
                inspection.setContent(content);
                inspection.setStatus(1); //未开启
                List<InspectionTask> inspectionTaskList=new ArrayList<>();
                if(evenType.equals(0)) {
                    String evenWeek=evenWeekObj.toString();
                    LocalDate now = LocalDate.now();
                    LocalDate tomorrow = now.plusDays(1);
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String format = dateTimeFormatter.format(now);
                    if(format.equals(beginTime)){
                        String[] split = evenWeek.split(",");
                        Set<Integer> weekSet=new HashSet<>();
                        for (String s : split) {
                            weekSet.add(Integer.parseInt(s));
                        }
                        Integer value = now.getDayOfWeek().getValue();
                        if(weekSet.contains(value)){
                            Date date=new Date();
                            InspectionTask inspectionTask=new InspectionTask();
                            inspectionTask.setTitle(title);
                            inspectionTask.setProjectId(projectId);
                            inspectionTask.setStatus(0);
                            inspectionTask.setUserId(userId);
                            inspectionTask.setContent(content);
                            inspectionTask.setDoingTime(date);
                            inspectionTask.setCreateTime(date);
                            inspectionTask.setUpdateTime(date);
                            inspectionTaskList.add(inspectionTask);
                        }
                        Integer tomorrowValue = tomorrow.getDayOfWeek().getValue();
                        if(weekSet.contains(tomorrowValue)){
                            Date date=new Date();
                            Calendar c = Calendar.getInstance();
                            c.setTime(date);
                            c.add(Calendar.DAY_OF_MONTH, 1);
                            Date tomorrowl = c.getTime();
                            InspectionTask inspectionTask=new InspectionTask();
                            inspectionTask.setTitle(title);
                            inspectionTask.setProjectId(projectId);
                            inspectionTask.setStatus(0);
                            inspectionTask.setUserId(userId);
                            inspectionTask.setContent(content);
                            inspectionTask.setDoingTime(tomorrowl);
                            inspectionTask.setCreateTime(date);
                            inspectionTask.setUpdateTime(date);
                            inspectionTaskList.add(inspectionTask);
                        }
                    }
                    inspection.setEvenWeek(evenWeek);
                }else{
                    LocalDate now = LocalDate.now();
                    LocalDate localDate = now.plusDays(1);
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String format = dateTimeFormatter.format(now);
                    String evenMonth=evenMonthObj.toString();
                    String format1 = dateTimeFormatter.format(localDate);
                    if(format.equals(beginTime)){
                        String[] split = evenMonth.split(",");
                        Set<Integer> monthSet=new HashSet<>();
                        for (String s : split) {
                            monthSet.add(Integer.parseInt(s));
                        }
                        Integer dayOfMonth = now.getDayOfMonth();
                        Integer dayOfMonth1 = localDate.getDayOfMonth();
                        if(monthSet.contains(dayOfMonth)){
                            Date date=new Date();
                            InspectionTask inspectionTask = new InspectionTask();
                            inspectionTask.setTitle(title);
                            inspectionTask.setProjectId(projectId);
                            inspectionTask.setStatus(0);
                            inspectionTask.setUserId(userId);
                            inspectionTask.setContent(content);
                            inspectionTask.setCreateTime(date);
                            inspectionTask.setUpdateTime(date);
                            inspectionTask.setDoingTime(date);
                            inspectionTaskList.add(inspectionTask);
                        }
                        if(monthSet.contains(dayOfMonth1)){
                            Date date=new Date();
                            Calendar c = Calendar.getInstance();
                            c.setTime(date);
                            c.add(Calendar.DAY_OF_MONTH, 1);
                            Date tomorrowl = c.getTime();
                            InspectionTask inspectionTask = new InspectionTask();
                            inspectionTask.setTitle(title);
                            inspectionTask.setProjectId(projectId);
                            inspectionTask.setStatus(0);
                            inspectionTask.setUserId(userId);
                            inspectionTask.setContent(content);
                            inspectionTask.setCreateTime(date);
                            inspectionTask.setUpdateTime(date);
                            inspectionTask.setDoingTime(tomorrowl);
                            inspectionTaskList.add(inspectionTask);
                        }

                    }
                    inspection.setEvenMonth(evenMonth);
                }
                Date beginDate = DateUtils.parseDate(beginTime, "yyyy-MM-dd");
                inspection.setBeginTime(beginDate);
                Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd");
                inspection.setEndTime(endDate);
                Date date = new Date();
                inspection.setCreateTime(date);
                inspection.setUpdateTime(date);
                inspectionService.save(inspection);
                if(!CollectionUtils.isEmpty(inspectionTaskList)){
                    for (InspectionTask inspectionTask : inspectionTaskList) {
                        inspectionTask.setInspectionId(inspection.getId());
                    }
                    taskService.saveList(inspectionTaskList);
                }
                result.setCode(1);
                result.setData(1);

            }
        } catch (ParseException e) {
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 根据策略定时生成任务
     */
    @Scheduled(cron = "0 30 2 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void addTask() {
        //获取活跃的巡检策略
        List<Inspection> inspections = inspectionService.getActiveInspectionInfo();
        //根据巡查策略集合遍历获取每一个值
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime tomorrowTime = localDateTime.plusDays(1);
        Date date = new Date();
        log.info("根据策略定时生成任务执行了,此时的时间"+date.toString());
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = c.getTime();//这是明天
        List<InspectionTask> inspectionTaskList = new ArrayList<>();
        for (Inspection inspectionInfo : inspections) {
            Integer evenType = inspectionInfo.getEvenType();
            Date beginTime = inspectionInfo.getBeginTime();
            long beginMill = beginTime.getTime();
            long tomorrowMill = tomorrowTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
            if (tomorrowMill > beginMill || tomorrowMill == beginMill) {
                if (evenType.equals(0)) {
                    Set<Integer> weekSet = new HashSet<>();
                    String evenWeek = inspectionInfo.getEvenWeek();
                    String[] split = evenWeek.split(",");
                    for (String s : split) {
                        weekSet.add(Integer.parseInt(s));
                    }
                    Integer dayOfWeek = tomorrowTime.getDayOfWeek().getValue();
                    if (weekSet.contains(dayOfWeek)) {
                        InspectionTask inspectionTask = new InspectionTask();
                        inspectionTask.setTitle(inspectionInfo.getTitle());
                        inspectionTask.setProjectId(inspectionInfo.getProjectId());
                        inspectionTask.setStatus(0);
                        inspectionTask.setUserId(inspectionInfo.getUserId());
                        inspectionTask.setContent(inspectionInfo.getContent());
                        inspectionTask.setInspectionId(inspectionInfo.getId());
                        inspectionTask.setDoingTime(tomorrow);
                        inspectionTask.setCreateTime(date);
                        inspectionTask.setUpdateTime(date);
                        inspectionTaskList.add(inspectionTask);
                    }
                } else {
                    Set<Integer> monthSet = new HashSet<>();
                    String evenMonth = inspectionInfo.getEvenMonth();
                    String[] split = evenMonth.split(",");
                    for (String s : split) {
                        monthSet.add(Integer.parseInt(s));
                    }
                    Integer dayOfMonth = tomorrowTime.getDayOfMonth();
                    if (monthSet.contains(dayOfMonth)) {
                        InspectionTask inspectionTask = new InspectionTask();
                        inspectionTask.setTitle(inspectionInfo.getTitle());
                        inspectionTask.setProjectId(inspectionInfo.getProjectId());
                        inspectionTask.setStatus(0);
                        inspectionTask.setUserId(inspectionInfo.getUserId());
                        inspectionTask.setContent(inspectionInfo.getContent());
                        inspectionTask.setInspectionId(inspectionInfo.getId());
                        inspectionTask.setCreateTime(date);
                        inspectionTask.setUpdateTime(date);
                        inspectionTask.setDoingTime(tomorrow);
                        inspectionTaskList.add(inspectionTask);
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(inspectionTaskList)) {
            taskService.saveList(inspectionTaskList);
        }

    }

    /**
     * 定时更新任务状态
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void updateTaskStutas() {
        //当前日期
        Date date = new Date();
        log.info("定时更新任务状态执行了此时的时间"+date.toString());
        //获取初始化的任务
        List<InspectionTask> taskInfo = taskService.getInitTask();
        //根据巡查策略集合遍历获取每一个值
        List<InspectionTask> taskList=new ArrayList<>();
        long timeNow = date.getTime();
        for (InspectionTask taskInfos : taskInfo) {
            //(当前日期 - 任务创建日期 > 7) = 失效日期
            long time = taskInfos.getDoingTime().getTime();
            long betweendays=(timeNow-time)/(1000*3600*24);
            if (betweendays > 7){
                InspectionTask inspectionTask = new InspectionTask();
                inspectionTask.setId(taskInfos.getId());
                inspectionTask.setStatus(3);
                inspectionTask.setUpdateTime(date);
                taskList.add(inspectionTask);
            }
        }
        if(!CollectionUtils.isEmpty(taskList)){
            taskService.updateStatusList(taskList);
        }

    }




    /**
     * 巡检策略具体信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/inspection/info", produces = CommonUtils.MediaTypeJSON)
    public String inspectionList(@RequestBody Map<String,Object> paramMap) {
        Result<InspectionDto> result=new Result<>();
        result.setCode(0);
        Object inspectionIdObj = paramMap.get("inspectionId");
        if(inspectionIdObj==null){
            result=Result.paramError(result);
        }else{
            Long inspectionId=Long.parseLong(inspectionIdObj.toString());
            InspectionDto inspectionDto=inspectionService.getById(inspectionId);
            if(inspectionDto==null){
               result= Result.empty(result);
            }else{
                result.setCode(1);
                result.setData(inspectionDto);
            }
        }
        Type type=new TypeToken<Result<InspectionDto>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    @ApiOperation("修改巡检策略")
    @PostMapping(value = "/update", produces = CommonUtils.MediaTypeJSON)
    public String revampInspection(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object idObj = paramMap.get("id");
            if(idObj==null){
              result=  Result.paramError(result);
            }else{
                Long id=Long.parseLong(idObj.toString());
                Inspection inspection = new Inspection();
                inspection.setId(id);
                Boolean flag=false;
                Object titleObj = paramMap.get("title");
                if(titleObj!=null){
                    flag=true;
                    inspection.setTitle(titleObj.toString());
                }
                Object evenTypeObj = paramMap.get("evenType");
                if(evenTypeObj!=null){
                    flag=true;
                    inspection.setEvenType(Integer.parseInt(evenTypeObj.toString()));
                }
                Object evenWeekObj = paramMap.get("evenWeek");
                if(evenWeekObj!=null){
                    flag=true;
                    inspection.setEvenWeek(evenWeekObj.toString());
                }
                Object evenMonthObj = paramMap.get("evenMonth");
                if(evenMonthObj!=null){
                    flag=true;
                    inspection.setEvenMonth(evenMonthObj.toString());
                }
                Object contentObj = paramMap.get("content");
                if(contentObj!=null){
                    flag=true;
                    inspection.setContent(contentObj.toString());
                }
                Object userIdObj = paramMap.get("userId");
                if(userIdObj!=null){
                    flag=true;
                    inspection.setUserId(Long.parseLong(userIdObj.toString()));
                }
                Object beginTimeObj = paramMap.get("beginTime");
                if(beginTimeObj!=null){
                    flag=true;
                    Date beginDate = DateUtils.parseDate(beginTimeObj.toString(), "yyyy-MM-dd");
                    inspection.setBeginTime(beginDate);
                }
                Object endTimeObj = paramMap.get("endTime");
                if(beginTimeObj!=null){
                    flag=true;
                    Date beginDate = DateUtils.parseDate(endTimeObj.toString(), "yyyy-MM-dd");
                    inspection.setEndTime(beginDate);
                }
                if(flag){
                    inspection.setUpdateTime(new Date());
                    inspectionService.revamp(inspection);
                }
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    @ApiOperation("删除巡检策略")
    @PostMapping(value = "/remove", produces = CommonUtils.MediaTypeJSON)
    public String removeInspection(@RequestBody Map<String,Object> pramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        Object idObj = pramMap.get("id");
        if(idObj==null){
            result=Result.paramError(result);
        }else{
            Long id=Long.parseLong(idObj.toString());
            Inspection inspection=new Inspection();
            inspection.setId(id);
            inspection.setStatus(3);
            inspection.setUpdateTime(new Date());
            inspectionService.revamp(inspection);
            result.setCode(1);
            result.setData(1);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 巡检策略信息列表
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/list",produces =CommonUtils.MediaTypeJSON )
    public String getInspectionList(@RequestBody Map<String, Object> paramMap){
        Result<List<InspectionDto>> result=new Result<>();
        result.setCode(0);
        Object projectIdObj = paramMap.get("projectId");
        if(projectIdObj==null){
            result=Result.paramError(result);
        }else{
            Long projectId=Long.parseLong(projectIdObj.toString());
            List<InspectionDto> inspectionDtoList=inspectionService.getDtoList(projectId);
            if(CollectionUtils.isEmpty(inspectionDtoList)){
               result= Result.empty(result);
            }else{
                result.setCode(1);
                result.setData(inspectionDtoList);
            }
        }
        Type type=new TypeToken<Result<List<InspectionDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }



}
