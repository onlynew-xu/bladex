package com.steelman.iot.platform.largescreen.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.controller.BaseController;
import com.steelman.iot.platform.entity.Area;
import com.steelman.iot.platform.entity.Building;
import com.steelman.iot.platform.largescreen.vo.*;
import com.steelman.iot.platform.service.*;
import com.steelman.iot.platform.utils.CommonUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import com.steelman.iot.platform.utils.Result;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/safe/daPing")
public  class SafeDaPingController extends BaseController {

    @Resource
    private DeviceService deviceService;
    @Resource
    private AlarmWarnService alarmWarnService;
    @Resource
    private RegionAreaService regionAreaService;
    @Resource
    private RegionBuildingService regionBuildingService;
    @Resource
    private ProjectService projectService;

    @PostMapping(value = "allProject",produces = CommonUtils.MediaTypeJSON)
    public String allProject(@RequestBody Map<String,Object> param){
        Result<List<ProjectSimInfo>>  result=new Result<>();
        result.setCode(0);
        try {
            Object userObj = param.get("userId");
            if(userObj!=null){
                List<ProjectSimInfo> projectSimInfoList=projectService.getProjectSimInfoByUserId(Long.parseLong(userObj.toString()));
                if(CollectionUtils.isEmpty(projectSimInfoList)){
                    result.setMsg("no data");
                }else{
                    result.setCode(1);
                    result.setData(projectSimInfoList);
                }
            }else{
                result.setMsg("param error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setMsg("exception");
        }
        Type type=new TypeToken<Result<List<ProjectSimInfo>>>(){}.getType();
        return  JsonUtils.toJson(result,type);
    }


    @PostMapping(value = "statisticDevice",produces = CommonUtils.MediaTypeJSON)
    public String statisticDevice(@RequestBody Map<String,Object> param){
        Result<Map<String,Integer>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = param.get("projectId");
            if(projectObj!=null){
                long projectId = Long.parseLong(projectObj.toString());
                Map<String,Integer> data= deviceService.staticDeviceDaPin(projectId,1000l);
                if(!CollectionUtils.isEmpty(data)){
                    result.setCode(1);
                    result.setData(data);
                }else{
                    result.setMsg("no data");
                }
            }else{
                result.setMsg("param error");
            }
        } catch (Exception e) {
            result.setMsg("exception");
            logger.error(e.getMessage());
        }
        Type type=new TypeToken<Result<Map<String,Integer>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
    @PostMapping(value = "inHandlerAlarm",produces = CommonUtils.MediaTypeJSON)
    public String inHandlerAlarm(@RequestBody Map<String,Object> param){
        Result<List<InHandlerAlarm>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = param.get("projectId");
            if(projectObj!=null){
                Long projectId=Long.parseLong(projectObj.toString());
                List<InHandlerAlarm> alarmList= alarmWarnService.inHandlerAlarm(projectId,1000l);
                if(!CollectionUtils.isEmpty(alarmList)){
                    result.setCode(1);
                    result.setData(alarmList);
                }else{
                    result.setMsg("empty");
                }
            }else{
                result.setMsg("param error");
            }
        } catch (Exception e) {
            result.setMsg("exception");
            logger.error(e.getMessage());
        }
        Type type =new TypeToken<Result<List<InHandlerAlarm>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
    @PostMapping(value = "areaAll",produces = CommonUtils.MediaTypeJSON)
    public String areaAll(@RequestBody Map<String,Object> param){
        Result<List<Area>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = param.get("projectId");
            if(projectObj!=null){
                Long projectId=Long.parseLong(projectObj.toString());
                List<Area> areaList=regionAreaService.selectByProjectId(projectId);
                if(!CollectionUtils.isEmpty(areaList)){
                    result.setData(areaList);
                    result.setCode(1);
                }else{
                    result.setMsg("empty");
                }
            }else{
                result.setMsg("param error");
            }
        } catch (Exception e) {
            result.setMsg("exception");
            logger.error(e.getMessage());
        }
        Type type=new TypeToken<Result<List<Area>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
    @PostMapping(value = "buildingInfo",produces = CommonUtils.MediaTypeJSON)
    public String buildingInfo(@RequestBody Map<String,Object> paramData){
        Result<List<Building>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramData.get("projectId");
            Object areaObj = paramData.get("areaId");
            if(projectObj==null||areaObj==null){
                result.setMsg("param error");
            }else{
                List<Building> buildingList=new ArrayList<>();
                Long projectId=Long.parseLong(projectObj.toString());
                Long areaId=Long.parseLong(areaObj.toString());
                if(areaId.equals(0l)){
                    buildingList=regionBuildingService.selectByProjectId(projectId);
                }else{
                    buildingList= regionBuildingService.selectByProjectIdAndAreaId(projectId,areaId);
                }
                if(!CollectionUtils.isEmpty(buildingList)){
                   result.setCode(1);
                   result.setData(buildingList);
                }else{
                    result.setMsg("empty");
                }
            }
        } catch (Exception e) {
            result.setMsg("exception");
            logger.error(e.getMessage());
        }
        Type type=new TypeToken<Result<List<Building>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
    @PostMapping(value = "deviceStatus",produces = CommonUtils.MediaTypeJSON)
    public String deviceStatus(@RequestBody Map<String,Object> paramData){
        Result<DeviceStatusStatistic> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramData.get("projectId");
            Object areaObj = paramData.get("areaId");
            Object buildingObj = paramData.get("buildingId");
            if(projectObj==null||areaObj==null||buildingObj==null){
                result.setMsg("param error");
            }else{
                Long projectId=Long.parseLong(projectObj.toString());
                Long areaId=Long.parseLong(areaObj.toString());
                Long buildingId=Long.parseLong(buildingObj.toString());
                DeviceStatusStatistic deviceStatusStatistic=deviceService.deviceStatusAndAlarmPer(projectId,areaId,buildingId);
                result.setCode(1);
                result.setData(deviceStatusStatistic);
            }
        } catch (Exception e) {
            result.setMsg("exception");
            logger.error(e.getMessage());
        }
        Type type=new TypeToken<Result<DeviceStatusStatistic>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
    @PostMapping(value = "alarmParentItem",produces = CommonUtils.MediaTypeJSON)
    public String alarmItem(@RequestBody Map<String,Object> paramData){
        Result<List<AlarmParentItemVo>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramData.get("projectId");
            if(projectObj!=null){
                Long projectId=Long.parseLong(projectObj.toString());
                List<AlarmParentItemVo> AlarmParentItemList = alarmWarnService.getAlarmParentItem(projectId);
                if(!CollectionUtils.isEmpty(AlarmParentItemList)){
                    result.setData(AlarmParentItemList);
                    result.setCode(1);
                }else{
                    result.setMsg("empty");
                }
            }else{
                result.setMsg("param error");
            }
        } catch (Exception e) {
            result.setMsg("exception");
            logger.error(e.getMessage());
        }
        Type type=new TypeToken<Result<List<AlarmParentItemVo>>>(){}.getType();
        return  JsonUtils.toJson(result,type);

    }
    @PostMapping(value = "year",produces = CommonUtils.MediaTypeJSON)
    public String year(@RequestBody Map<String,Object> paramData){
        Result<List<String>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramData.get("projectId");
            if(projectObj!=null){
                Long projectId=Long.parseLong(projectObj.toString());
                List<String> yearList=alarmWarnService.getYear(projectId);
                if(!CollectionUtils.isEmpty(yearList)){
                    result.setCode(1);
                    result.setData(yearList);
                }else{
                    result.setMsg("empty");
                }
            }else{
                result.setMsg("param error");
            }
        } catch (Exception e) {
            result.setMsg("exception");
            logger.error(e.getMessage());
        }

        Type type=new TypeToken<Result<List<String>>>(){}.getType();
        return  JsonUtils.toJson(result,type);
    }
    @PostMapping(value = "month",produces = CommonUtils.MediaTypeJSON)
    public String month(@RequestBody Map<String,Object> paramData){
        Result<List<String>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramData.get("projectId");
            Object yearObj = paramData.get("year");
            if(projectObj!=null&&yearObj!=null){
                Long projectId = Long.parseLong(projectObj.toString());
                String year=yearObj.toString();
                List<String> monthList;
                if(yearObj.equals("0")){
                    monthList= alarmWarnService.getMonth(projectId,null);
                }else{
                    monthList=alarmWarnService.getMonth(projectId,year);
                }
                if(!CollectionUtils.isEmpty(monthList)){
                    result.setCode(1);
                    result.setData(monthList);
                }else{
                    result.setMsg("empty");
                }
            }else{
                result.setMsg("param error");
            }
        } catch (Exception e) {
            result.setMsg("exception");
            logger.error(e.getMessage());
        }
        Type type=new TypeToken<Result<List<String>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
    @PostMapping(value = "alarmWarnInfo",produces = CommonUtils.MediaTypeJSON)
    public String alarmWarnInfo(@RequestBody Map<String,Object> paramMap){
        Result<List<AlarmWarnInfoVo>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramMap.get("projectId");
            Object areaObj = paramMap.get("areaId");
            Object buildingObj = paramMap.get("buildingId");
            Object alarmParentItemObj = paramMap.get("alarmParentItemId");
            Object yearObj = paramMap.get("year");
            Object monthObj = paramMap.get("month");
            if(projectObj==null||areaObj==null||buildingObj==null||alarmParentItemObj==null||yearObj==null||monthObj==null){
                result.setMsg("param error");

            }else{
                Long projectId= Long.parseLong(projectObj.toString());
                Long areaId=Long.parseLong(areaObj.toString());
                Long buildingId=Long.parseLong(buildingObj.toString());
                Long alarmParentItemId=Long.parseLong(alarmParentItemObj.toString());
                String year=yearObj.toString();
                String month=monthObj.toString();
                if(areaId.equals(0L)){
                    areaId=null;
                }
                if(buildingId.equals(0l)){
                    buildingId=null;
                }
                if(alarmParentItemId.equals(0l)){
                    alarmParentItemId=null;
                }
                if(year.equals("0")){
                    year=null;
                }
                if(month.equals("0")){
                    month=null;
                }
                List<AlarmWarnInfoVo> alarmWarnInfoVoList=alarmWarnService.getAlarmInfoVo(projectId,areaId,buildingId,alarmParentItemId,year,month);
                if(!CollectionUtils.isEmpty(alarmWarnInfoVoList)){
                    result.setData(alarmWarnInfoVoList);
                    result.setCode(1);
                }else{
                    result.setMsg("empty");
                }
            }
        } catch (Exception e) {
            result.setMsg("exception");
            logger.error(e.getMessage());
        }
        Type type=new TypeToken<Result<List<AlarmWarnInfoVo>>>(){}.getType();
        return JsonUtils.toJson(result,type);

    }
    @PostMapping(value = "alarmCountInfo",produces = CommonUtils.MediaTypeJSON)
    public String alarmCountInfo(@RequestBody Map<String,Object> paramMap){
        Result<List<AlarmCountInfo>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj=getObjectObj(paramMap);
            Object areaObj = paramMap.get("areaId");
            Object buildingObj=paramMap.get("buildingId");
            Object countObj = paramMap.get("count");
            Object yearObj= paramMap.get("year");
            Object monthObj= paramMap.get("month");
            //根据区域和建筑和月份获取报警次数
            if(areaObj==null||buildingObj==null||countObj==null||yearObj==null||monthObj==null){
                result.setMsg("param error");
            }else{
                Long projectId=Long.parseLong(projectObj.toString());
                Long areaId=Long.parseLong(areaObj.toString());
                if(areaId.equals(0l)){
                    areaId=null;
                }
                Long buildingId=Long.parseLong(buildingObj.toString());
                if(buildingId.equals(0l)){
                    buildingId=null;
                }
                Integer  count=Integer.parseInt(countObj.toString());
                String year=yearObj.toString();
                String month=yearObj.toString();
                List<AlarmCountInfo> alarmCountInfoList=alarmWarnService.getAlarmCountInfo(projectId,areaId,buildingId,count,year,month);
                if(CollectionUtils.isEmpty(alarmCountInfoList)){
                    result.setMsg("empty");
                }else{
                    result.setCode(1);
                    result.setData(alarmCountInfoList);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setMsg("exception");
        }
        Type type=new TypeToken<Result<List<AlarmCountInfo>>>(){}.getType();
        return  JsonUtils.toJson(result,type);
    }
    private Object getObjectObj(Map<String,Object> paramMap){
        Object projectId = paramMap.get("projectId");
        return  projectId;
    }


}
