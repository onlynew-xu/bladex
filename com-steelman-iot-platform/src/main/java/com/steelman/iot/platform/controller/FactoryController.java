package com.steelman.iot.platform.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.*;
import com.steelman.iot.platform.entity.dto.EnergyDayData;
import com.steelman.iot.platform.entity.dto.EnergyMonthData;
import com.steelman.iot.platform.service.*;
import com.steelman.iot.platform.utils.CommonUtils;
import com.steelman.iot.platform.utils.HttpWebUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import com.steelman.iot.platform.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/factory")
public class FactoryController extends BaseController {

    @Resource
    private FactoryInfoService factoryInfoService;
    @Resource
    private EnergyEquipmentService energyEquipmentService;
    @Resource
    private FactoryProcessUnitService factoryProcessUnitService;
    @Resource
    private FactoryUploadConfigureService factoryUploadConfigureService;
    @Resource
    private EnergyUploadService energyUploadService;

    /**
     * 注册信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/register",produces = CommonUtils.MediaTypeJSON)
    public String registerCityFactoryInfo(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
//        Object projectIdObj = paramMap.get("projectId");
//        if(projectIdObj==null){
//            result=Result.paramError(result);
//        }else{
//            Long projectId=Long.parseLong(projectIdObj.toString());
//            Integer res=factoryInfoService.registerCityFactoryInfo(projectId);
//            if(res>0){
//                result.setCode(1);
//            }
//        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 上传配置信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/configure",produces = CommonUtils.MediaTypeJSON)
    public String configureFactoryInfo(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
//        Object projectIdObj = paramMap.get("projectId");
//        if(projectIdObj==null) {
//            result = Result.paramError(result);
//        }else{
//            Long projectId=Long.parseLong(projectIdObj.toString());
//            Integer res=factoryInfoService.configureFactoryInfo(projectId);
//            if(res>0){
//                result.setCode(1);
//            }
//        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 手动上报日电度数据
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/upload/Day",produces = CommonUtils.MediaTypeJSON)
    public String uploadDayData(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
//        Integer success =uploadDayData(2l);
//        EnergyUpload energyUpload=new EnergyUpload();
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime localDate = now.plusDays(-1);
//        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String format = dateTimeFormatter2.format(localDate);
//        energyUpload.setDay(format);
//        energyUpload.setProjectId(2l);
//        energyUpload.setStatus(success);
//        energyUpload.setType(1);
//        energyUpload.setCreateTime(new Date());
//        energyUploadService.save(energyUpload);
//        logger.info("日能耗自动上报"+dateTimeFormatter.format(now));
        result.setCode(1);
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 手动上报月电度数据
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/upload/month",produces = CommonUtils.MediaTypeJSON)
    public String uploadMonthData(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
//        Integer success =uploadMonthData(2l);
//        EnergyUpload energyUpload=new EnergyUpload();
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime localDate = now.plusDays(-1);
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String format = dateTimeFormatter2.format(localDate);
//        energyUpload.setDay(format);
//        energyUpload.setProjectId(2l);
//        energyUpload.setStatus(success);
//        energyUpload.setType(2);
//        energyUpload.setCreateTime(new Date());
//        energyUploadService.save(energyUpload);
//        logger.info("月能耗手动上报成功"+dateTimeFormatter.format(now));
        result.setCode(1);
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 自动上报日电度数据
     */
    @Scheduled(cron = "0 30 9 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void uploadDayData (){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("每天9点半 定时上报日电度数据 此时的时间:"+dateTimeFormatter.format(now));
        Integer success=uploadDayData(2l);
        EnergyUpload energyUpload=new EnergyUpload();
        LocalDateTime localDate = now.plusDays(-1);
        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter2.format(localDate);
        energyUpload.setDay(format);
        energyUpload.setProjectId(2l);
        energyUpload.setStatus(success);
        energyUpload.setType(1);
        energyUpload.setCreateTime(new Date());
        energyUploadService.save(energyUpload);
        logger.info("日能耗自动上报成功"+dateTimeFormatter.format(now));
    }

    /**
     * 自动上报月电度数据
     */
    @Scheduled(cron = "0 30 9 2 * ?")
    @Transactional(rollbackFor = Exception.class)
    public void uploadMonthData (){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("每月2日9点半执行了定时任务 此时的时间"+dateTimeFormatter.format(now));
        Integer success=uploadMonthData(2l);
        EnergyUpload energyUpload=new EnergyUpload();
        LocalDateTime localDate = now.plusDays(-1);
        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter2.format(localDate);
        energyUpload.setDay(format);
        energyUpload.setProjectId(2l);
        energyUpload.setStatus(success);
        energyUpload.setType(2);
        energyUpload.setCreateTime(new Date());
        energyUploadService.save(energyUpload);
        logger.info("月能耗自动上报成功"+dateTimeFormatter.format(now));
    }


    public Integer uploadDayData(Long projectId){
        LocalDate localDate = LocalDate.now();
        LocalDate localDate2 = localDate.plusDays(-1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter.format(localDate2);
        List<EnergyHourDifferData> energyHourDifferDataList=energyEquipmentService.getHourDiffData(projectId,format);
        EnergyDayData energyDayData=energyEquipmentService.getDayTotal(projectId,format);
        FactoryInfo factoryInfo=  factoryInfoService.findByProjectId(projectId);
        //做数据判断
        if(energyHourDifferDataList==null||energyDayData==null||factoryInfo==null){
            return 0;
        }
        FactoryProcessUnit factoryProcessUnit = factoryProcessUnitService.getByFactoryId(factoryInfo.getId());
        FactoryUploadConfigure factoryUploadConfigure = factoryUploadConfigureService.getByFactoryId(factoryInfo.getId());
        if(factoryProcessUnit==null||factoryUploadConfigure==null){
            return 0;
        }
        String deviceId = factoryUploadConfigure.getDeviceId();
        String enterpriseCode = factoryUploadConfigure.getEnterpriseCode();
        String processCode = factoryProcessUnit.getProcessCode();
        String processUnitCode = factoryProcessUnit.getProcessUnitCode();
        String equipmentCode = factoryProcessUnit.getEquipmentCode();
        String equipmentUnitCode = factoryProcessUnit.getEquipmentUnitCode();
        String energyClassCode = factoryProcessUnit.getEnergyClassCode();
        String energyTypeCode = factoryProcessUnit.getEnergyTypeCode();
        String dataUsageCode = factoryProcessUnit.getDataUsageCode();
        String dataCode=processCode+"-"+processUnitCode+"-"+equipmentCode+equipmentUnitCode+"-"+energyClassCode+energyTypeCode+"-"+dataUsageCode;
        JSONObject paramObject=new JSONObject();
        paramObject.put("deviceId",deviceId);
        paramObject.put("enterpriseCode",enterpriseCode);
        JSONArray dataArray=new JSONArray();
        LocalDateTime date=LocalDateTime.now();
        DateTimeFormatter  dateTimeFormatter1=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String format1 = dateTimeFormatter1.format(date);
        for (EnergyHourDifferData energyHourDifferData : energyHourDifferDataList) {
            JSONObject dataObject=new JSONObject();
            dataObject.put("dataCode",dataCode);
            Long hourTotal = energyHourDifferData.getHourTotal();
            Long total= hourTotal/1000;
            dataObject.put("dataValue",total);
            dataObject.put("inputType",1);
            dataObject.put("statType",0);
            dataObject.put("statDate",energyHourDifferData.getDateTimeBegin());
            dataObject.put("uploadDate",format1);
            dataObject.put("scope",1);
            dataObject.put("valid",true);
            dataArray.add(dataObject);
        }
        Long dayTotal = energyDayData.getDayTotal();
        String yearMonthDay = energyDayData.getYearMonthDay();
        yearMonthDay=yearMonthDay+" 00:00:00";
        JSONObject dataObject=new JSONObject();
        dataObject.put("dataCode",dataCode);
        Long total= dayTotal/1000;
        dataObject.put("dataValue",total);
        dataObject.put("inputType",1);
        dataObject.put("statType",1);
        dataObject.put("statDate",yearMonthDay);
        dataObject.put("uploadDate",format1);
        dataObject.put("scope",1);
        dataObject.put("valid",true);
        dataArray.add(dataObject);
        paramObject.put("data",dataArray);
        try {
            String response= HttpWebUtils.post(factoryUploadConfigure.getCenterDataUrl(),paramObject.toJSONString());
            logger.info("上报日电度返回数据");
            JSONObject parse1 = JSONObject.parseObject(response);
            logger.info("上报日电度相关数据返回的数据"+parse1.toJSONString());
            String success = parse1.getString("responseMessage");
            if("RECEIVE SUCCESS".equals(success)){
                return 1;
            }

        } catch (Exception e) {
            logger.error("上报日电度相关数据失败"+e.getMessage());
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    public Integer uploadMonthData(Long projectId){
        EnergyMonthData energyMonthData=energyEquipmentService.getMonthLastMeasure(projectId);
        if(energyMonthData!=null){
            DateTimeFormatter  dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
            FactoryInfo factoryInfo=  factoryInfoService.findByProjectId(projectId);
            FactoryProcessUnit factoryProcessUnit = factoryProcessUnitService.getByFactoryId(factoryInfo.getId());
            FactoryUploadConfigure factoryUploadConfigure = factoryUploadConfigureService.getByFactoryId(factoryInfo.getId());
            String deviceId = factoryUploadConfigure.getDeviceId();
            String enterpriseCode = factoryUploadConfigure.getEnterpriseCode();
            String processCode = factoryProcessUnit.getProcessCode();
            String processUnitCode = factoryProcessUnit.getProcessUnitCode();
            String equipmentCode = factoryProcessUnit.getEquipmentCode();
            String equipmentUnitCode = factoryProcessUnit.getEquipmentUnitCode();
            String energyClassCode = factoryProcessUnit.getEnergyClassCode();
            String energyTypeCode = factoryProcessUnit.getEnergyTypeCode();
            String dataUsageCode = factoryProcessUnit.getDataUsageCode();
            String dataCode=processCode+"-"+processUnitCode+"-"+equipmentCode+equipmentUnitCode+"-"+energyClassCode+energyTypeCode+"-"+dataUsageCode;
            JSONObject paramObject=new JSONObject();
            paramObject.put("deviceId",deviceId);
            paramObject.put("enterpriseCode",enterpriseCode);
            JSONArray dataArray=new JSONArray();

            LocalDateTime date=LocalDateTime.now();
            DateTimeFormatter  dateTimeFormatter1=DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
            String format1 = dateTimeFormatter1.format(date);
            LocalDateTime localDate = date.plusMonths(-1);
            LocalDateTime with = localDate.with(TemporalAdjusters.firstDayOfMonth());
            String format = dateTimeFormatter.format(with);
            format=format+" 00:00:00";

            JSONObject dataObject=new JSONObject();
            dataObject.put("dataCode",dataCode);
            Long monthTotal = energyMonthData.getMonthTotal();
            Long total= monthTotal/1000;
            dataObject.put("dataValue",total);
            dataObject.put("inputType",1);
            dataObject.put("statType",2);
            dataObject.put("statDate",format);
            dataObject.put("uploadDate",format1);
            dataObject.put("scope",1);
            dataObject.put("valid",true);
            dataArray.add(dataObject);
            paramObject.put("data",dataArray);
            try {
                String post = HttpWebUtils.post(factoryUploadConfigure.getCenterDataUrl(),paramObject.toJSONString());
                logger.info("上报月电度返回数据");
                if(StringUtils.isNotBlank(post)){
                    logger.info("上报月电度相关数据返回的数据"+post);
                    JSONObject jsonObject = JSONObject.parseObject(post);
                    String responseMessage = jsonObject.getString("responseMessage");
                    if("RECEIVE SUCCESS".equals(responseMessage)){
                        return 1;
                    }
                }
            } catch (Exception e) {
                logger.error("上报月电度数据失败"+e.getMessage());
                e.printStackTrace();
                return -1;
            }

        }
        return 0;
    }

}
