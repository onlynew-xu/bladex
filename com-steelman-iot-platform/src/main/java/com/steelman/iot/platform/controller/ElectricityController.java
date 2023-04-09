package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.device.MT300DeviceRemoteService;
import com.steelman.iot.platform.device.MT300SDeviceRemoteService;
import com.steelman.iot.platform.device.SafeDeviceRemoteService;
import com.steelman.iot.platform.entity.*;
import com.steelman.iot.platform.entity.dto.*;
import com.steelman.iot.platform.entity.vo.*;
import com.steelman.iot.platform.service.*;
import com.steelman.iot.platform.utils.*;
import com.steelman.iot.platform.weather.config.WeatherUtils;
import com.steelman.iot.platform.weather.config.entity.CityWeather;
import io.swagger.annotations.Api;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Author lsj
 * @DATE 2021/4/12 0012 16:01
 * @Description:
 */
@RestController
@RequestMapping("/api/power")
@Api(tags = "智慧用电")
public class ElectricityController extends BaseController {

    @Resource
    private AlarmWarnService alarmWarnService;

    @Resource
    private PowerService powerService;
    @Resource
    private ProjectService projectService;
    @Resource
    private WeatherUtils weatherUtils;
    @Resource
    private PowerIncomingService powerIncomingService;
    @Resource
    private PowerTransformerService powerTransformerService;
    @Resource
    private PowerCompensateService powerCompensateService;
    @Resource
    private PowerWaveService powerWaveService;
    @Resource
    private PowerFeederService powerFeederService;
    @Resource
    private PowerGeneratorService powerGeneratorService;
    @Resource
    private PowerBoxService powerBoxService;
    @Resource
    private PowerMatriculationService powerMatriculationService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private SafeDeviceRemoteService safeDeviceRemoteService;
    @Resource
    private MT300DeviceRemoteService mt300DeviceRemoteService;
    @Resource
    private MT300SDeviceRemoteService mt300SDeviceRemoteService;
    @Resource
    private PowerBoxLoopService powerBoxLoopService;
    @Resource
    private PowerFeederLoopService powerFeederLoopService;
    @Resource
    private DeviceTaskService deviceTaskService;

    /**
     * 滚动未处理的预警信息详情
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/alarm/inHandler",produces = CommonUtils.MediaTypeJSON)
    public String alarmInHandler(@RequestBody Map<String,Object> paramMap){
        Result<List<PowerAlarmWarnVo>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                result = Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                List<PowerAlarmWarnVo> vos = alarmWarnService.getPowerAlarmWarnVo(projectId, 2000L);
                if(CollectionUtils.isEmpty(vos)){
                   result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(vos);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/alarm/inHandler"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerAlarmWarnVo>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 未处理的告警数量
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/alarm/inHandler/count",produces = CommonUtils.MediaTypeJSON)
    public String alarmInHandlerCount(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                result = Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                Integer vos = alarmWarnService.getPowerAlarmWarnCount(projectId, 2000L);
                result.setCode(1);
                result.setData(vos);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/alarm/inHandler/count"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 告警类型占比
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/alarm/count/statistic",produces = CommonUtils.MediaTypeJSON)
    public String electricityAlarmCount(@RequestBody Map<String,Object> paramMap) {
        Result<List<DeviceTypeAlarmStatistic>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj!=null){
                Long projectId= Long.parseLong(projectIdObj.toString());
                List<Map<String, Object>> dataList = alarmWarnService.alarmCount(projectId, 2000L);
                if(!CollectionUtils.isEmpty(dataList)){
                    List<DeviceTypeAlarmStatistic> deviceTypeAlarmStatisticList=new ArrayList<>();
                    for (Map<String, Object> stringObjectMap : dataList) {
                        Object num = stringObjectMap.get("num");
                        Object itemName = stringObjectMap.get("itemName");
                        DeviceTypeAlarmStatistic deviceTypeAlarmStatistic=new DeviceTypeAlarmStatistic();
                        deviceTypeAlarmStatistic.setName(itemName.toString());
                        deviceTypeAlarmStatistic.setValue(Integer.parseInt(num.toString()));
                        deviceTypeAlarmStatisticList.add(deviceTypeAlarmStatistic);
                    }
                    result.setCode(1);
                    result.setData(deviceTypeAlarmStatisticList);
                }else{
                   result= Result.empty(result);
                }
            }else{
               result= Result.paramError(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/alarm/count/statistic"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<DeviceTypeAlarmStatistic>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 获取项目所在地的天气信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/weather",produces = CommonUtils.MediaTypeJSON)
    public String weatherInfo(@RequestBody Map<String,Object> paramMap) {
        Result<CityWeather> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj!=null){
                Long projectId=  Long.parseLong(projectIdObj.toString());
                Project project = projectService.findById(projectId);
                CityWeather weather = weatherUtils.getWeatherByCityName(project.getCity());
                if(weather==null){
                   result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(weather);
                }
            }else{
                result= Result.paramError(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/weather"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<CityWeather>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 获取电房概略信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/simple/info",produces = CommonUtils.MediaTypeJSON)
    public String powerSimple(@RequestBody Map<String,Object> paramMap){
        Result<List<PowerDataSimple>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                Result.paramError(result);
            }else{
               Long projectId=  Long.parseLong(projectIdObj.toString());
               List<PowerDataSimple> powerDataSimpleList=  powerService.getPowerSimple(projectId);
               if(!CollectionUtils.isEmpty(powerDataSimpleList)){
                   result.setCode(1);
                   PowerDataSimple powerDataSimple = powerDataSimpleList.get(0);
                   powerDataSimple.setDefaultFlag(1);
                   result.setData(powerDataSimpleList);
               }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/simple/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerDataSimple>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 电房历史报警信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/alarm/history",produces = CommonUtils.MediaTypeJSON)
    public String getAlarmList(@RequestBody Map<String,Object> paramMap) {
        Result<List<AlarmWarnItemVo>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object partObj = paramMap.get("part");
            if(projectIdObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                String part=null;
                if(partObj!=null){
                    part=projectIdObj.toString();
                }
                List<AlarmWarnItemVo> alarmWarnInfoVos = alarmWarnService.getAlarmList(projectId, 2000L,part);
                if(!CollectionUtils.isEmpty(alarmWarnInfoVos)){
                   result.setCode(1);
                   result.setData(alarmWarnInfoVos);
                }else{
                   result= Result.empty(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/alarm/history"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<AlarmWarnItemVo>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    @GetMapping(value = "/alarm/history/export",produces = CommonUtils.MediaTypeJSON)
    public void exportAlarmList(@RequestParam Map<String,Object>paramMap , HttpServletResponse response, HttpServletRequest request){
        Object projectIdObj = paramMap.get("projectId");
        Long projectId=Long.parseLong(projectIdObj.toString());
        List<String> title = new ArrayList<>();
        title.add("电房名称");
        title.add("电房设备名称");
        title.add("电房设备回路");
        title.add("告警类型");
        title.add("告警时间");
        title.add("告警处理状态");
        title.add("当前设备状态");
        Map<String, String[][]> downloadDate = powerService.getAlarmDownLoadData(projectId);
        try {
            Project project = projectService.findById(projectId);
            LocalDate localDate=LocalDate.now();
            DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fileName =  project.getName()+dateTimeFormatter.format(localDate)+"电房告警历史信息"+ ".xlsx";
            String realName= FileHelper.getEncodeName(fileName,request);
            XSSFWorkbook workbook = ExcelUtils.exportDate(title, downloadDate);
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + FileHelper.getDownloadName(realName,request));//默认Excel名称
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/alarm/history/export"));
            e.printStackTrace();
        }
    }

    /**
     * 电房详情页数据
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/power/detail",produces = CommonUtils.MediaTypeJSON)
    public String powerCenter(@RequestBody Map<String,Object> paramMap){
        Result<PowerInfoVo> result=new Result();
        result.setCode(0);
        try {
            Object powerIdObj = paramMap.get("powerId");
            if(powerIdObj==null){
               result= Result.paramError(result);
            }else{
                Long powerId  = Long.parseLong(powerIdObj.toString());
                PowerInfoVo powerInfoVo=powerService.getPowerDetail(powerId);
                if(powerInfoVo==null){
                   result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(powerInfoVo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/power/detail"));
            result=Result.exceptionRe(result);
        }

        Type type=new TypeToken<Result<PowerInfoVo>>(){}.getType();
        return JsonUtils.toJson(result,type);

    }

    /**
     * 设备中心
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/center",produces = CommonUtils.MediaTypeJSON)
    public String powerDeviceCenter(@RequestBody Map<String,Object> paramMap) {
        Result<PowerCenterInfo> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                result=Result.paramError(result);
            }else{
               Long projectId= Long.parseLong(projectIdObj.toString());
               PowerCenterInfo powerCenterInfo=powerService.getPowerCenter(projectId);
               if(powerCenterInfo!=null){
                   result.setCode(1);
                   result.setData(powerCenterInfo);
               }else{
                   result=Result.empty(result);
               }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/center"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<PowerCenterInfo>>(){}.getType();
        return JsonUtils.toJson(result,type);

    }

    /**
     * 设备中心(电房切换)
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/center/change/info",produces = CommonUtils.MediaTypeJSON)
    public String powerDeviceCenterInfo(@RequestBody Map<String,Object> paramMap) {
        Result<List<PowerFacilityCenterInfo>> result=new Result<>();
        result.setCode(0);
        try {
            Object powerIdObj = paramMap.get("powerId");
            if(powerIdObj==null){
                result=Result.paramError(result);
            }else{
                Long powerId= Long.parseLong(powerIdObj.toString());
                List<PowerFacilityCenterInfo> dataMap=powerService.getPowerCenterDevice(powerId);
                if(!CollectionUtils.isEmpty(dataMap)){
                   result.setCode(1);
                   result.setData(dataMap);
                }else{
                    Result.empty(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/center/change/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerFacilityCenterInfo>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
    /**
     * 变压器信息详情页
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/transformer/info",produces = CommonUtils.MediaTypeJSON)
    public String transformerInfo(@RequestBody Map<String,Object> paramMap) {
        Result<TransformerInfoDto> result=new Result<>();
        result.setCode(0);
        try {
            Object transformerIdObj = paramMap.get("transformerId");
            if(transformerIdObj==null){
               result= Result.paramError(result);
            }else{
                Long transformerId=Long.parseLong(transformerIdObj.toString());
                TransformerInfoDto transformerInfo = powerTransformerService.getTransformerInfoDto(transformerId);
                if(transformerInfo==null){
                    result=Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(transformerInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/transformer/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<TransformerInfoDto>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
    /**
     * 发电机详情页
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/generator/info",produces = CommonUtils.MediaTypeJSON)
    public String generatorInfo(@RequestBody Map<String,Object> paramMap){
        Result<GeneratorDto> result=new Result<>();
        result.setCode(0);
        try {
            Object generatorIdObj = paramMap.get("generatorId");
            if(generatorIdObj==null){
               result= Result.paramError(result);
            }else{
                Long generatorId=Long.parseLong(generatorIdObj.toString());
                GeneratorDto generatorDto=powerGeneratorService.getGeneratorDto(generatorId);
                if(generatorDto==null){
                    result=Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(generatorDto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/generator/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<GeneratorDto>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
    /**
     * 进线柜详情页信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/incoming/info",produces = CommonUtils.MediaTypeJSON)
    public String incomingInfo(@RequestBody Map<String,Object> paramMap){
        Result<IncomingInfo> result=new Result<>();
        result.setCode(0);
        try {
            Object incomingIdObj = paramMap.get("incomingId");
            if(incomingIdObj==null){
                result=Result.paramError(result);
            }else{
                Long incomingId=Long.parseLong(incomingIdObj.toString());
                IncomingInfo incomingInfo=powerIncomingService.getIncomingInfoDetail(incomingId);
                if(incomingInfo==null){
                   result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(incomingInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/incoming/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<IncomingInfo>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 进线柜常规数据
     * @param paramMap
     * @return
     */
    @RequestMapping(value = "/incoming/data",produces = CommonUtils.MediaTypeJSON)
    public String incomingData(@RequestBody Map<String,Object> paramMap){
        Result<IncomingData> result=new Result<>();
        result.setCode(0);
        try {
            Object incomingIdObj = paramMap.get("incomingId");
            if(incomingIdObj==null){
                result=Result.paramError(result);
            }else{
                Long incomingId=Long.parseLong(incomingIdObj.toString());
                IncomingData incomingData=powerIncomingService.getIncomingData(incomingId);
                if(incomingData==null){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(incomingData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/incoming/data"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<IncomingData>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 进线柜电度数据
     * @param paramMap
     * @return
     */
    @RequestMapping(value = "/incoming/measure",produces = CommonUtils.MediaTypeJSON)
    public String incomingMeasure(@RequestBody Map<String,Object> paramMap){
        Result<Map<String,List<WeekEnergyConsumeStatistic>>> result=new Result<>();
        result.setCode(0);
        try {
            Object incomingIdObj = paramMap.get("incomingId");
            if(incomingIdObj==null){
                result=Result.paramError(result);
            }else{
                Long incomingId=Long.parseLong(incomingIdObj.toString());
                Map<String,List<WeekEnergyConsumeStatistic>> measureData=powerIncomingService.getMeasureData(incomingId);
                if(measureData==null){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(measureData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/incoming/measure"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Map<String,List<WeekEnergyConsumeStatistic>>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 补偿柜详情页信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/compensate/info",produces = CommonUtils.MediaTypeJSON)
    public String compensateInfo(@RequestBody Map<String,Object> paramMap){
        Result<CompensateDto> result=new Result<>();
        result.setCode(0);
        try {
            Object compensateObj = paramMap.get("compensateId");
            if(compensateObj==null){
                result=Result.paramError(result);
            }else{
                Long compensateId=Long.parseLong(compensateObj.toString());
                CompensateDto compensateDto=powerCompensateService.getCompensateInfoDetail(compensateId);
                if(compensateDto==null){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(compensateDto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/compensate/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<CompensateDto>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 补偿元件状态信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/compensate/components/info",produces = CommonUtils.MediaTypeJSON)
    public String compensateComponentsInfo(@RequestBody Map<String,Object> paramMap){
        Result<List<PowerEntityDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object compensateObj = paramMap.get("compensateId");
            if(compensateObj==null){
                result=Result.paramError(result);
            }else{
                Long compensateId=Long.parseLong(compensateObj.toString());
                List<PowerEntityDto> powerEntityDto=powerCompensateService.getCompensateComponentsInfo(compensateId);
                if(powerEntityDto==null){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(powerEntityDto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/compensate/components/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerEntityDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 滤波柜信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/wave/info",produces = CommonUtils.MediaTypeJSON)
    public String getPowerWaveInfo(@RequestBody Map<String,Object> paramMap) {
        Result<WaveDto> result=new Result<>();
        result.setCode(0);
        try {
            Object waveIdObj = paramMap.get("waveId");
            if(waveIdObj==null){
                 result = Result.paramError(result);
            }else{
                Long waveId=Long.parseLong(waveIdObj.toString());
                WaveDto waveDto = powerWaveService.getWaveInfoDto(waveId);
                if(waveDto==null){
                    result = Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(waveDto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/wave/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<WaveDto>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 单回路馈线柜
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/feeder/simple/info",produces = CommonUtils.MediaTypeJSON)
    public String getPowerFeederSimpleInfo(@RequestBody Map<String,Object> paramMap) {
        Result<PowerFeederInfoDto> result=new Result<>();
        result.setCode(0);
        try {
            Object feederIdObj = paramMap.get("feederId");
            if(feederIdObj==null){
                result = Result.paramError(result);
            }else{
                Long feederId=Long.parseLong(feederIdObj.toString());
                PowerFeederInfoDto powerFeederInfoDto = powerFeederService.getSimpleFeederInfo(feederId);
                if(powerFeederInfoDto==null){
                    result = Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(powerFeederInfoDto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/feeder/simple/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<PowerFeederInfoDto>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 多回路馈线柜回路信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/feeder/multiply/loop",produces = CommonUtils.MediaTypeJSON)
    public String getFeederMultiplyLoop(@RequestBody Map<String,Object> paramMap){
        Result<List<PowerFeederLoop>> result=new Result<>();
        result.setCode(0);
        try {
            Object feederIdObj = paramMap.get("feederId");
            if(feederIdObj==null){
               result= Result.paramError(result);
            }else{
                Long feederId=Long.parseLong(feederIdObj.toString());
                List<PowerFeederLoop> powerFeederLoopList=powerFeederLoopService.getLoopList(feederId);
                if(CollectionUtils.isEmpty(powerFeederLoopList)){
                    result=Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(powerFeederLoopList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/feeder/multiply/loop"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerFeederLoop>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 多回路馈线柜信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/feeder/multiply/info",produces = CommonUtils.MediaTypeJSON)
    public String getFeederMultiplyInfo(@RequestBody Map<String,Object> paramMap){
        Result<PowerFeederInfoDto> result=new Result<>();
        result.setCode(0);
        try {
            Object feederIdObj = paramMap.get("feederId");
            Object loopIdObj = paramMap.get("loopId");
            if(feederIdObj==null||loopIdObj==null){
                result = Result.paramError(result);
            }else{
                Long feederId=Long.parseLong(feederIdObj.toString());
                Long loopId= Long.parseLong(loopIdObj.toString());
                logger.info("此时的馈线柜Id:"+feederId);
                logger.info("此时的馈线柜回路Id:"+loopId);
                PowerFeederInfoDto powerFeederInfoDto=  powerFeederService.getMultiplyFeederInfo(feederId,loopId);
                if(powerFeederInfoDto!=null){
                    result.setData(powerFeederInfoDto);
                    result.setCode(1);
                }else{
                    Result.empty(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/feeder/multiply/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<PowerFeederInfoDto>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 配电箱回路信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/box/multiply/loop",produces = CommonUtils.MediaTypeJSON)
    public String getBoxLoopInfo(@RequestBody Map<String,Object> paramMap){
        Result<List<PowerBoxLoop>> result=new Result<>();
        result.setCode(0);
        try {
            Object boxIdObj = paramMap.get("boxId");
            if(boxIdObj==null){
                result=Result.paramError(result);
            }else{
                Long boxId=Long.parseLong(boxIdObj.toString());
                List<PowerBoxLoop> powerBoxLoopList= powerBoxLoopService.getBoxLoopList(boxId);
                if(CollectionUtils.isEmpty(powerBoxLoopList)){
                   result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(powerBoxLoopList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/box/multiply/loop"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerBoxLoop>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 配电箱信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/box/multiply/info",produces = CommonUtils.MediaTypeJSON)
    public String getBoxInfo(@RequestBody Map<String,Object> paramMap){
        Result<PowerBoxInfoDto> result=new Result<>();
        result.setCode(0);
        try {
            Object boxIdObj = paramMap.get("boxId");
            Object loopIdObj = paramMap.get("loopId");
            if(boxIdObj==null||loopIdObj==null){
               result=  Result.paramError(result);
            }else{
                Long boxId=Long.parseLong(boxIdObj.toString());
                Long loopId=Long.parseLong(loopIdObj.toString());
                logger.info("此时的配电箱Id:"+boxId);
                logger.info("此时的配电箱回路Id:"+boxId);
                PowerBoxInfoDto powerBoxInfoDto=powerBoxService.getBoxInfoDto(boxId,loopId);
                if(powerBoxInfoDto!=null){
                    result.setCode(1);
                    result.setData(powerBoxInfoDto);
                }else{
                    result=Result.empty(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/box/multiply/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<PowerBoxInfoDto>>(){}.getType();
        return  JsonUtils.toJson(result,type);
    }

    /**
     * 母联柜信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/matriculation/info",produces = CommonUtils.MediaTypeJSON)
    public String getMatriculationInfo(@RequestBody Map<String,Object> paramMap){
        Result<PowerMatriculationDto> result=new Result<>();
        result.setCode(0);
        try {
            Object matriculationIdObj = paramMap.get("matriculationId");
            if(matriculationIdObj==null){
                result=Result.paramError(result);
            }else {
                Long matriculationId=Long.parseLong(matriculationIdObj.toString());
                PowerMatriculationDto powerMatriculationDto=powerMatriculationService.getPowerMatriculationInfo(matriculationId);
                if(powerMatriculationDto==null){
                   result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(powerMatriculationDto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/matriculation/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<PowerMatriculationDto>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 设备自检
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/device/selfInspection",produces = CommonUtils.MediaTypeJSON)
    public String deviceSelfInspection(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        Object deviceIdObj = paramMap.get("deviceId");
        Object deviceTypeIdObj = paramMap.get("deviceTypeId");
        try {
            if(deviceIdObj==null||deviceTypeIdObj==null){
               result= Result.paramError(result);
            }else{
                Long deviceId=Long.parseLong(deviceIdObj.toString());
                Integer deviceTypeId=Integer.parseInt(deviceTypeIdObj.toString());
                Device device = deviceService.findById(deviceId);
                Integer status = device.getStatus();
                if(status.equals(2)||status.equals(4)){
                    result.setMsg("设备已离线,请检查设备");
                }else{
                    if(deviceTypeId.equals(1)||deviceTypeId.equals(2)||deviceTypeId.equals(3)||deviceTypeId.equals(4)){
                        Boolean selfInspection = safeDeviceRemoteService.selfInspection(device.getSerialNum());
                        if(selfInspection){
                            result.setCode(1);
                            result.setData(1);
                        }
                    }else if(deviceTypeId.equals(6)){
                        Boolean mt300Self = mt300DeviceRemoteService.selfInspection(device.getSerialNum());
                        if(mt300Self){
                            result.setCode(1);
                            result.setData(1);
                        }
                    }else if(deviceTypeId.equals(13)){
                        Boolean selfInspection = mt300SDeviceRemoteService.selfInspection(device.getSerialNum());
                        if(selfInspection){
                            result.setCode(1);
                            result.setData(1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/device/selfInspection"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 复位

     * @return
     */
    @PostMapping(value = "/device/restoration",produces = CommonUtils.MediaTypeJSON)
    public String deviceRestoration(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        Object deviceIdObj = paramMap.get("deviceId");
        Object deviceTypeIdObj = paramMap.get("deviceTypeId");
        try {
            if(deviceIdObj==null||deviceTypeIdObj==null){
                result= Result.paramError(result);
            }else{
                Long deviceId=Long.parseLong(deviceIdObj.toString());
                Integer deviceTypeId=Integer.parseInt(deviceTypeIdObj.toString());
                Device device = deviceService.findById(deviceId);
                Integer status = device.getStatus();
                if(status.equals(2)||status.equals(4)){
                    result.setMsg("设备已离线,请检查设备");
                }else{
                    if(deviceTypeId.equals(1)||deviceTypeId.equals(2)||deviceTypeId.equals(3)||deviceTypeId.equals(4)){
                        Boolean selfInspection = safeDeviceRemoteService.restoration(device.getSerialNum());
                        if(selfInspection){
                            result.setCode(1);
                            result.setData(1);
                        }
                    }else if(deviceTypeId.equals(6)){
                        Boolean mt300Self = mt300DeviceRemoteService.setRestoration(device.getSerialNum());
                        if(mt300Self){
                            result.setCode(1);
                            result.setData(1);
                        }
                    }else if(deviceTypeId.equals(13)){
                        Boolean mt300Self = mt300SDeviceRemoteService.setRestoration(device.getSerialNum());
                        if(mt300Self){
                            result.setCode(1);
                            result.setData(1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/device/restoration"));
            result=Result.exceptionRe(result);
        }

        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 消音
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/device/erasure",produces = CommonUtils.MediaTypeJSON)
    public String deviceSmartErasure(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        Object deviceIdObj = paramMap.get("deviceId");
        Object deviceTypeIdObj = paramMap.get("deviceTypeId");
        try {
            if(deviceIdObj==null||deviceTypeIdObj==null){
                result= Result.paramError(result);
            }else{
                Long deviceId=Long.parseLong(deviceIdObj.toString());
                Integer deviceTypeId=Integer.parseInt(deviceTypeIdObj.toString());
                Device device = deviceService.findById(deviceId);
                Integer status = device.getStatus();
                if(status.equals(2)||status.equals(4)){
                    result.setMsg("设备已离线,请检查设备");
                }else{
                    if(deviceTypeId.equals(1)||deviceTypeId.equals(2)||deviceTypeId.equals(3)||deviceTypeId.equals(4)){
                        Boolean selfInspection = safeDeviceRemoteService.erasure(device.getSerialNum());
                        if(selfInspection){
                            result.setCode(1);
                            result.setData(1);
                        }
                    }else if(deviceTypeId.equals(6)){
                        Boolean mt300Self = mt300DeviceRemoteService.setErasure(device.getSerialNum());
                        if(mt300Self){
                            result.setCode(1);
                            result.setData(1);
                        }
                    }else if(deviceTypeId.equals(13)){
                        Boolean mt300Self = mt300SDeviceRemoteService.setErasure(device.getSerialNum());
                        if(mt300Self){
                            result.setCode(1);
                            result.setData(1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/device/erasure"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 电房设备告警统计
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/alarm/statistic",produces = CommonUtils.MediaTypeJSON)
    public String alarmStatistic(@RequestBody Map<String,Object> paramMap){
        Result<List<DeviceTypeAlarmStatistic>> result=new Result<>();
        result.setCode(0);
        try {
            Object typeObj = paramMap.get("type");
            Object idObj = paramMap.get("id");
            Object loopIdObj = paramMap.get("loopId");
            if(typeObj==null||idObj==null){
                result= Result.paramError(result);
            }else{
                Integer type=Integer.parseInt(typeObj.toString());
                Long id=Long.parseLong(idObj.toString());
                List<DeviceTypeAlarmStatistic> deviceTypeAlarmStatistics=powerService.alarmStatistic(type,id,loopIdObj);
                if(deviceTypeAlarmStatistics==null){
                    result=Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(deviceTypeAlarmStatistics);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/alarm/statistic"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<DeviceTypeAlarmStatistic>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 电房设备告警历史统计
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/alarm/device/history",produces = CommonUtils.MediaTypeJSON)
    public String alarmHistory(@RequestBody Map<String,Object> paramMap){
        Result<List<AlarmWarnItemVo>> result=new Result<>();
        result.setCode(0);
        try {
            Object typeObj = paramMap.get("type");
            Object idObj = paramMap.get("id");
            Object loopIdObj = paramMap.get("loopId");
            if(typeObj==null||idObj==null){
                result= Result.paramError(result);
            }else{
                Integer type=Integer.parseInt(typeObj.toString());
                Long id=Long.parseLong(idObj.toString());
                List<AlarmWarnItemVo> alarmWarnItemVoList=powerService.alarmHistory(type,id,loopIdObj);
                if(alarmWarnItemVoList==null){
                    result=Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(alarmWarnItemVoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/alarm/device/history"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<AlarmWarnItemVo>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @PostMapping(value = "/device/maintenance",produces = CommonUtils.MediaTypeJSON)
    public String deviceMaintenance(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result = new Result<>();
        result.setCode(0);
        try {
            Object deviceIdObj = paramMap.get("deviceId");
            Object userIdObj = paramMap.get("userId");
            Object detailObj = paramMap.get("detail");
            Object projectIdObj = paramMap.get("projectId");
            if (Objects.isNull(deviceIdObj) || Objects.isNull(userIdObj) || Objects.isNull(detailObj) || Objects.isNull(projectIdObj)) {
                result = Result.paramError(result);
            } else {
                Long deviceId=Long.parseLong(deviceIdObj.toString());
                Long userId=Long.parseLong(userIdObj.toString());
                Long projectId=Long.parseLong(projectIdObj.toString());
                String  detail=detailObj.toString();
                Device device = deviceService.findById(deviceId);
                if (device == null) {
                    result.setMsg("设备不存在");
                } else {
                    Date date = new Date();
                    DeviceTask deviceTask = new DeviceTask();
                    deviceTask.setSystemId(2000L);
                    deviceTask.setSerialNum(device.getSerialNum());
                    deviceTask.setProjectId(projectId);
                    deviceTask.setCreateTime(date);
                    deviceTask.setDetail(detail);
                    deviceTask.setDeviceStatus(device.getStatus());
                    deviceTask.setStatus(0);
                    deviceTask.setDeviceId(device.getId());
                    deviceTask.setUpdateTime(date);
                    deviceTask.setWorkerId(null);
                    deviceTask.setUserId(userId);
                    deviceTaskService.insert(deviceTask);
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/device/maintenance"));
            result=Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<Integer>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }
}
