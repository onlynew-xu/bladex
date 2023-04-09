package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.vo.*;
import com.steelman.iot.platform.service.AlarmWarnService;
import com.steelman.iot.platform.utils.CommonUtils;
import com.steelman.iot.platform.utils.ExceptionUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import com.steelman.iot.platform.utils.Result;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author tang
 * date 2020-12-02
 */
@RestController
@RequestMapping("/api/alarmWarn")
public class AlarmWarnController extends BaseController {
    @Resource
    private AlarmWarnService alarmWarnService;

    /**
     * 安全预警  实时数据预警中心数据
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/safe/handle/status",produces = CommonUtils.MediaTypeJSON)
    public String alarmInfo(@RequestBody Map<String,Object> paramMap ){
        Result<AlarmHandleStatus> result=new Result<>();
        try {
            Object projectObj = paramMap.get("projectId");
            if(projectObj==null){
                result = Result.paramError(result);
            }else{
                AlarmHandleStatus handleStatus = alarmWarnService.getHandleStatus( Long.parseLong(projectObj.toString()), 1000L);
                if(handleStatus==null){
                    Result.empty(result);
                }else{
                    result.setData(handleStatus);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackMsg(e,"/api/alarmWarn/safe/handle/status"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<AlarmHandleStatus>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 安全预警  报警次数综合统计
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/safe/recent/count",produces = CommonUtils.MediaTypeJSON)
    public String getAlarmCountByDate(@RequestBody Map<String,Object> paramMap){
        Result<List<AlarmCountStatistics>> result=new Result<>();
        try {
            Object projectObj = paramMap.get("projectId");
            if(projectObj==null){
                result = Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectObj.toString());
                List<AlarmCountStatistics> dataList=alarmWarnService.getAlarmCountByDate(projectId,1000L);
                if(CollectionUtils.isEmpty(dataList)){
                    result = Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(dataList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/alarmWarn/safe/recent/count"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<AlarmCountStatistics>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 安全预警  预警数据类型数据综合统计
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/safe/alarmType/statistic" ,produces = CommonUtils.MediaTypeJSON)
    public String statisticAlarmType(@RequestBody Map<String,Object> paramMap){
        Result<Map<String,List<AlarmTypeStatistic>>> result=new Result<>();
        try {
            Object projectObj = paramMap.get("projectId");
            result.setCode(0);
            if(projectObj==null){
                result.setMsg("param error");
            }else{
                Long projectId=Long.parseLong(projectObj.toString());
                Map<String,List<AlarmTypeStatistic>> dataMap=alarmWarnService.statisticAlarmType(projectId,1000L);
                if(CollectionUtils.isEmpty(dataMap)){
                    result.setCode(-1);
                    result.setMsg("empty");
                }else{
                    result.setCode(1);
                    result.setData(dataMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/alarmWarn/safe/alarmType/statistic"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Map<String,List<AlarmTypeStatistic>>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 安全预警  设备类型预警数据综合统计
     * @param paramMap
     * @return
     */
    @PostMapping(value ="/safe/deviceType/statistic" ,produces = CommonUtils.MediaTypeJSON)
    public String deviceTypeStatistic(@RequestBody Map<String,Object> paramMap){
        Result<Map<String,List<DeviceTypeAlarmStatistic>>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramMap.get("projectId");
            if(projectObj==null){
                 result = Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectObj.toString());
                Map<String,List<DeviceTypeAlarmStatistic>> dataMap=alarmWarnService.deviceTypeStatistic(projectId,1000l);
                if(CollectionUtils.isEmpty(dataMap)){
                    result = Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(dataMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/alarmWarn/safe/deviceType/statistic"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Map<String,List<DeviceTypeAlarmStatistic>>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 安全预警  获取部分离线数据(10条)
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/safe/offline/part",produces = CommonUtils.MediaTypeJSON)
    public String partOfflineDevice(@RequestBody Map<String,Object> paramMap){
        Result<List<DeviceOfflineInfo>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramMap.get("projectId");
            if(projectObj==null){
                result= Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectObj.toString());
                List<DeviceOfflineInfo> deviceOfflineInfoList=alarmWarnService.getOfflineInfo(projectId,1000L,"part");
                if(CollectionUtils.isEmpty(deviceOfflineInfoList)){
                    result = Result.empty(result);
                }else{
                    result.setData(deviceOfflineInfoList);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/alarmWarn/safe/offline/part"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<DeviceOfflineInfo>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     *安全预警  获取所有离线数据
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/safe/offline/all",produces = CommonUtils.MediaTypeJSON)
    public String AllOfflineDevice(@RequestBody Map<String,Object> paramMap){
        Result<List<DeviceOfflineInfo>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramMap.get("projectId");
            if(projectObj==null){
                result= Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectObj.toString());
                List<DeviceOfflineInfo> deviceOfflineInfoList=alarmWarnService.getOfflineInfo(projectId,1000L,"all");
                if(CollectionUtils.isEmpty(deviceOfflineInfoList)){
                    result = Result.empty(result);
                }else{
                    result.setData(deviceOfflineInfoList);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/alarmWarn/safe/offline/all"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<DeviceOfflineInfo>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     *安全预警 获取风险的设备数据(部分)
     * @param paramData
     * @return
     */
    @PostMapping(value = "/safe/potential/part/risk",produces = CommonUtils.MediaTypeJSON)
    public String potentialRisk(@RequestBody Map<String,Object> paramData){
        Result<List<DeviceRiskInfo>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramData.get("projectId");
            if(projectObj==null){
               result= Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectObj.toString());
                List<DeviceRiskInfo> dataList=alarmWarnService.getRiskDevice(projectId,1000L,"part");
                if(CollectionUtils.isEmpty(dataList)){
                   result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(dataList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/alarmWarn/safe/potential/part/risk"));
            result=Result.exceptionRe(result);
        }
        Type  type=new TypeToken<Result<List<DeviceRiskInfo>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     *安全预警 获取风险的设备数据(全部)
     * @param paramData
     * @return
     */
    @PostMapping(value = "/safe/all/risk",produces = CommonUtils.MediaTypeJSON)
    public String allRisk(@RequestBody Map<String,Object> paramData){
        Result<List<DeviceRiskInfo>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramData.get("projectId");
            if(projectObj==null){
                result= Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectObj.toString());
                List<DeviceRiskInfo> dataList=alarmWarnService.getRiskDevice(projectId,1000L,"");
                if(CollectionUtils.isEmpty(dataList)){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(dataList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/alarmWarn/safe/all/risk"));
            result=Result.exceptionRe(result);
        }
        Type  type=new TypeToken<Result<List<DeviceRiskInfo>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 安全预警  设备预警类型统计
     * @param paramData
     * @return
     */
    @PostMapping(value = "/safe/deviceAlarmType/statistic",produces = CommonUtils.MediaTypeJSON)
    public  String getSafeAlarm(@RequestBody Map<String,Object> paramData){
        Result<List<DeviceTypeAlarmStatistic>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramData.get("projectId");
            Object deviceObj = paramData.get("deviceId");
            if(projectObj==null||deviceObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId = Long.parseLong(projectObj.toString());
                Long deviceId=Long.parseLong(deviceObj.toString());
                List<DeviceTypeAlarmStatistic>  deviceTypeAlarmStatistics=alarmWarnService.getSafeAlarmTypeStatistic(projectId,deviceId,1000L);
                if(CollectionUtils.isEmpty(deviceTypeAlarmStatistics)){
                    result = Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(deviceTypeAlarmStatistics);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/alarmWarn/safe/deviceAlarmType/statistic"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<DeviceTypeAlarmStatistic>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     *安全预警  设备预警历史信息
     * @param paramData
     * @return
     */
    @PostMapping(value = "/safe/deviceAlarm/history",produces = CommonUtils.MediaTypeJSON)
    public String getSafeAlarmHistory(@RequestBody Map<String,Object> paramData){
        Result<List<AlarmSimpleInfo>> result=new Result();
        result.setCode(0);
        try {
            Object projectObj = paramData.get("projectId");
            Object deviceObj = paramData.get("deviceId");
            if(projectObj==null||deviceObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId = Long.parseLong(projectObj.toString());
                Long deviceId=Long.parseLong(deviceObj.toString());
                List<AlarmSimpleInfo> alarmSimpleInfoList=alarmWarnService.getDeviceAlarmHistory(projectId,deviceId,1000L);
                if(CollectionUtils.isEmpty(alarmSimpleInfoList)){
                    result=Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(alarmSimpleInfoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/alarmWarn/safe/deviceAlarm/history"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<AlarmSimpleInfo>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

}
