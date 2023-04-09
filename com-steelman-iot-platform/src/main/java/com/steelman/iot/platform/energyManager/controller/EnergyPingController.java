package com.steelman.iot.platform.energyManager.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.controller.BaseController;
import com.steelman.iot.platform.energyManager.dto.*;
import com.steelman.iot.platform.energyManager.entity.MonthMeasureStatistic;
import com.steelman.iot.platform.energyManager.service.EnergyPingService;
import com.steelman.iot.platform.entity.Project;
import com.steelman.iot.platform.entity.User;
import com.steelman.iot.platform.entity.dto.EnergyReportDataDetail;
import com.steelman.iot.platform.entity.dto.EntityDto;
import com.steelman.iot.platform.entity.vo.EnergyConsumeStatistic;
import com.steelman.iot.platform.entity.vo.EnergyConsumeStatisticDetail;
import com.steelman.iot.platform.entity.vo.EnergyStatisticYearDetail;
import com.steelman.iot.platform.entity.vo.EquipmentRankReport;
import com.steelman.iot.platform.service.EnergyEquipmentService;
import com.steelman.iot.platform.service.ProjectService;
import com.steelman.iot.platform.service.UserService;
import com.steelman.iot.platform.utils.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/energy/ping")
public class EnergyPingController extends BaseController {
    @Resource
    private EnergyPingService energyPingService;

    @Resource
    private EnergyEquipmentService energyEquipmentService;
    @Resource
    private ProjectService projectService;
    @Resource
    private UserService userService;


    /**
     * 认证能源管理账号
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/authentication/user",produces = CommonUtils.MediaTypeJSON)
    public String authenticationUser(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object userIdObj = paramMap.get("userId");
            if(userIdObj==null){
                result=Result.paramError(result);
            }else{
                Long userId=Long.parseLong(userIdObj.toString());
                User  user=userService.findById(userId);
                Integer admin = user.getAdmin();
                if(admin.equals(3)){
                    result.setCode(1);
                    result.setData(1);
                }else{
                    result.setCode(-1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/ping/authentication/user"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 获取用户项目
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/project/list", produces = CommonUtils.MediaTypeJSON)
    public String getProjectList(@RequestBody Map<String, Object> paramMap) {
        Result<List<EntityDto>> result = new Result<>();
        result.setCode(0);
        try {
            Object userIdObj = paramMap.get("userId");
            if (userIdObj == null) {
                result = Result.paramError(result);
            } else {
                Long userId = Long.parseLong(userIdObj.toString());
                List<EntityDto> entityDtoList = energyPingService.getProjectList(userId);
                result.setCode(1);
                result.setData(entityDtoList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/ping/project/list"));
            result=Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<List<EntityDto>>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }




    /**
     * 统计设备的在线和离线数量
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/count/statistic", produces = CommonUtils.MediaTypeJSON)
    public String countStatistic(@RequestBody Map<String, Object> paramMap) {
        Result<CountStatistic> result = new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if (projectIdObj == null) {
                result = Result.paramError(result);
            } else {
                Long projectId = Long.parseLong(projectIdObj.toString());
                CountStatistic countStatistic = energyPingService.countStatistic(projectId);
                result.setCode(1);
                result.setData(countStatistic);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/ping/count/statistic"));
            result=Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<CountStatistic>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }


    /**
     * 获取日，月 昨天的能耗统计
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/day/statistic", produces = CommonUtils.MediaTypeJSON)
    public String DayStatistic(@RequestBody Map<String, Object> paramMap) {
        Result<MeasureDayMonthStatistic> result = new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if (projectIdObj == null) {
                result = Result.paramError(result);
            } else {
                Long projectId = Long.parseLong(projectIdObj.toString());
                MeasureDayMonthStatistic measureDayMonthStatistic = energyPingService.dayStatistic(projectId);
                result.setCode(1);
                result.setData(measureDayMonthStatistic);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/ping/day/statistic"));
            result=Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<MeasureDayMonthStatistic>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }


    /**
     * 获取当天的每个小时的能耗统计
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/day/hour/statistic", produces = CommonUtils.MediaTypeJSON)
    public String hourMeasureStatistic(@RequestBody Map<String, Object> paramMap) {
        Result<Map<String, Object>> result = new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if (projectIdObj == null) {
                result = Result.paramError(result);
            } else {
                Long projectId = Long.parseLong(projectIdObj.toString());
                Map<String, Object> dataMap = energyPingService.getHourMeasureStatistic(projectId);
                if (CollectionUtils.isEmpty(dataMap)) {
                    result = Result.empty(result);
                } else {
                    result.setCode(1);
                    result.setData(dataMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/ping/day/hour/statistic"));
            result=Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<Map<String, Object>>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }

    /**
     * 获取月能耗环比数据
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/month/compare/statistic", produces = CommonUtils.MediaTypeJSON)
    public String monthCompare(@RequestBody Map<String, Object> paramMap) {
        Result<List<EnergyConsumeStatisticDetail>> result = new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if (projectIdObj == null) {
                result = Result.paramError(result);
            } else {
                Long projectId = Long.parseLong(projectIdObj.toString());
                List<EnergyConsumeStatisticDetail> details = energyPingService.monthCompareStatistic(projectId);
                if (CollectionUtils.isEmpty(details)) {
                    result = Result.empty(result);
                } else {
                    result.setCode(1);
                    result.setData(details);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/ping/month/compare/statistic"));
            result=Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<List<EnergyConsumeStatisticDetail>>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }

    /**
     * 能耗设备类型占比
     *
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/type/consume/statistic", produces = CommonUtils.MediaTypeJSON)
    public String countConsumeDevice(@RequestBody Map<String, Object> paramMap) {
        Result<List<EnergyConsumeStatistic>> result = new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if (projectIdObj == null) {
                result = Result.paramError(result);
            } else {
                Long projectId = Long.parseLong(projectIdObj.toString());
                List<EnergyConsumeStatistic> energyConsumeStatisticList = energyEquipmentService.consumeStatistic(projectId);
                if (CollectionUtils.isEmpty(energyConsumeStatisticList)) {
                    result = Result.empty(result);
                } else {
                    result.setData(energyConsumeStatisticList);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/ping/type/consume/statistic"));
            result=Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<List<EnergyConsumeStatistic>>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }

    /**
     * 获取设备电度统计(流向图)
     *
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/measure/statistic", produces = CommonUtils.MediaTypeJSON)
    public String measureStatistic(@RequestBody Map<String, Object> paramMap) {
        Result<TotalMeasure> result = new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object typeObj = paramMap.get("type");
            if (projectIdObj == null || typeObj == null) {
                result = Result.paramError(result);
            } else {
                Long projectId = Long.parseLong(projectIdObj.toString());
                Integer type = Integer.parseInt(typeObj.toString());
                TotalMeasure totalMeasure = energyPingService.totalStatistic(projectId, type);
                if (totalMeasure == null) {
                    result = Result.empty(result);
                } else {
                    result.setCode(1);
                    result.setData(totalMeasure);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/ping/measure/statistic"));
            result=Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<TotalMeasure>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }

    /**
     * 获取所有能源类型列表
     *
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/consume/type/list", produces = CommonUtils.MediaTypeJSON)
    public String consumeList(@RequestBody Map<String, Object> paramMap) {
        Result<List<EntityDto>> result = new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if (projectIdObj == null) {
                result = Result.empty(result);
            } else {
                Long projectId = Long.parseLong(projectIdObj.toString());
                List<EntityDto> entityDtoList = energyPingService.getConsumeList(projectId);
                if (CollectionUtils.isEmpty(entityDtoList)) {
                    result = Result.empty(result);
                } else {
                    result.setCode(1);
                    result.setData(entityDtoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/ping/consume/type/list"));
            result=Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<List<EntityDto>>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }

    /**
     * 获取设备当天的能源质量数据
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/hour/device/data", produces = CommonUtils.MediaTypeJSON)
    public String hourDeviceData(@RequestBody Map<String, Object> paramMap) {
        Result<Map<String, Object>> result = new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object consumeTypeIdObj = paramMap.get("consumeTypeId");
            Object dataTypeObj = paramMap.get("dataType");
            if (projectIdObj == null || consumeTypeIdObj == null || dataTypeObj == null) {
                result = Result.empty(result);
            } else {
                Long projectId = Long.parseLong(projectIdObj.toString());
                Long consumeTypeId = Long.parseLong(consumeTypeIdObj.toString());
                String dataType = dataTypeObj.toString();
                Map<String, Object> dataMap = energyPingService.getHourDeviceData(projectId, consumeTypeId, dataType);
                if (CollectionUtils.isEmpty(dataMap)) {
                    result = Result.empty(result);
                } else {
                    result.setCode(1);
                    result.setData(dataMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/ping/hour/device/data"));
            result=Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<Map<String, Object>>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }


    /**
     * 获取指定时间间隔的电度数据
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/dateTime/device/measure", produces = CommonUtils.MediaTypeJSON)
    public String measureDateTime(@RequestBody Map<String, Object> paramMap) {
        Result<List<EnergyMeasureDateTime>> result = new Result<>();
        result.setCode(0);
        try {
            Object projectIdOBj = paramMap.get("projectId");
            Object begTimeOBj = paramMap.get("begTime");
            Object endTimeObj = paramMap.get("endTime");
            Object consumeTypeIdObj = paramMap.get("consumeTypeId");
            Object orderObj = paramMap.get("order");
            if (projectIdOBj == null || begTimeOBj == null || endTimeObj == null || consumeTypeIdObj == null || orderObj == null) {
                result = Result.paramError(result);
            } else {
                Long projectId = Long.parseLong(projectIdOBj.toString());
                String begTime = begTimeOBj.toString();
                String endTime = endTimeObj.toString();
                Long consumeTypeId = Long.parseLong(consumeTypeIdObj.toString());
                String order = orderObj.toString();
                List<EnergyMeasureDateTime> measureDateTimeList = energyPingService.measureDateTime(projectId, consumeTypeId, begTime, endTime, order);
                if (!CollectionUtils.isEmpty(measureDateTimeList)) {
                    result.setCode(1);
                    result.setData(measureDateTimeList);
                } else {
                    result = Result.empty(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/ping/dateTime/device/measure"));
            result=Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<List<EnergyMeasureDateTime>>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }

    /**
     * 导出指定时间间隔的电度数据
     * @param paramMap
     * @param response
     * @param request
     */
    @GetMapping(value = "/dateTime/device/measure/export", produces = CommonUtils.MediaTypeJSON)
    public void measureDateTimeExport(@RequestParam Map<String, Object> paramMap, HttpServletResponse response, HttpServletRequest request) {
        Result<Integer> result = new Result<>();
        result.setCode(0);
        Object projectIdOBj = paramMap.get("projectId");
        Object begTimeOBj = paramMap.get("begTime");
        Object endTimeObj = paramMap.get("endTime");
        Object consumeTypeIdObj = paramMap.get("consumeTypeId");
        Object orderObj = paramMap.get("order");
        if (projectIdOBj == null || begTimeOBj == null || endTimeObj == null || consumeTypeIdObj == null || orderObj == null) {
            result = Result.paramError(result);
        } else {
            Long projectId = Long.parseLong(projectIdOBj.toString());
            String begTime = begTimeOBj.toString();
            String endTime = endTimeObj.toString();
            Long consumeTypeId = Long.parseLong(consumeTypeIdObj.toString());
            String order = orderObj.toString();
            List<EnergyMeasureDateTime> measureDateTimeList = energyPingService.measureDateTime(projectId, consumeTypeId, begTime, endTime, order);
            Map<String, String[][]> dateTimeMeasureExport = getDateTimeMeasureExport(measureDateTimeList, begTime, endTime);
            List<String> title = new ArrayList<>();
            title.add("设备名称");
            title.add("能耗类别");
            title.add("设备类别");
            title.add("尖时能耗(kW·h)");
            title.add("峰时能耗(kW·h)");
            title.add("尖时能耗(kW·h)");
            title.add("谷时能耗(kW·h)");
            title.add("能耗总计(kW·h)");
            title.add("统计开始日期");
            title.add("统计截止日期");

            try {
                Project project = projectService.findById(projectId);
                String fileName = project.getName() + begTime + "~" + endTime + "耗能统计" + ".xlsx";
                String realName = FileHelper.getEncodeName(fileName, request);
                XSSFWorkbook workbook = ExcelUtils.exportDate(title, dateTimeMeasureExport);
                response.setContentType("application/octet-stream");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Disposition", "attachment;filename=" + FileHelper.getDownloadName(realName, request));//默认Excel名称
                response.flushBuffer();
                workbook.write(response.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/ping/dateTime/device/measure"));
                result=Result.exceptionRe(result);
            }

        }
    }

    /**
     * 今年 月消耗数据
     * @param pramMap
     * @return
     */
    @PostMapping(value = "/month/measure/statistic", produces = CommonUtils.MediaTypeJSON)
    public String monthMeasureStatistic(@RequestBody Map<String,Object> pramMap){
        Result<List<MonthMeasureStatistic>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = pramMap.get("projectId");
            if(projectIdObj==null){
               result= Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                LocalDate now = LocalDate.now();
                int yearInt = now.getYear();
                String year=String.valueOf(yearInt);
                List<MonthMeasureStatistic> measureStatistics=energyPingService.getMonthMeasureStatistic(projectId,year);
                if(CollectionUtils.isEmpty(measureStatistics)){
                   result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(measureStatistics);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/ping/month/measure/statistic"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<MonthMeasureStatistic>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 今年月消耗数据 导出
     * @param pramMap
     * @param request
     * @param response
     */
    @GetMapping(value = "/month/measure/statistic/export", produces = CommonUtils.MediaTypeJSON)
    public void monthMeasureStatisticExport(@RequestParam Map<String,Object> pramMap,HttpServletRequest request,HttpServletResponse response){
        Result<List<MonthMeasureStatistic>> result=new Result<>();
        result.setCode(0);
        Object projectIdObj = pramMap.get("projectId");
            Long projectId=Long.parseLong(projectIdObj.toString());
            LocalDate now = LocalDate.now();
            int yearInt = now.getYear();
            String year=String.valueOf(yearInt);
            List<MonthMeasureStatistic> measureStatistics=energyPingService.getMonthMeasureStatistic(projectId,year);
            Map<String, String[][]> monthTotalStatistic = getMonthTotalStatistic(measureStatistics);
            List<String> title = new ArrayList<>();
            title.add("年");
            title.add("月");
            title.add("设备总数");
            title.add("总能耗(kW·h)");
            title.add("同比能耗(kW·h)");
            title.add("结算时间");
            try {
                Project project = projectService.findById(projectId);
                String fileName = project.getName() + " 月耗能统计" + ".xlsx";
                String realName = FileHelper.getEncodeName(fileName, request);
                XSSFWorkbook workbook = ExcelUtils.exportDate(title, monthTotalStatistic);
                response.setContentType("application/octet-stream");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Disposition", "attachment;filename=" + FileHelper.getDownloadName(realName, request));//默认Excel名称
                response.flushBuffer();
                workbook.write(response.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/ping/month/measure/statistic/export"));
            }

    }

    /**
     * 获取某个月 每台设备的月能耗数据
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/month/measure/detail/statistic", produces = CommonUtils.MediaTypeJSON)
    public String getMonthDetail(@RequestBody Map<String,Object> paramMap){
        Result<List<MonthDetailStatistic>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object yearObj = paramMap.get("year");
            Object monthObj = paramMap.get("month");
            if(projectIdObj==null||yearObj==null||monthObj==null){
               result= Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                String year=yearObj.toString();
                String month=monthObj.toString();
                List<MonthDetailStatistic> monthDetailStatisticList=energyPingService.getMonthDetail(projectId,year,month);
                if(!CollectionUtils.isEmpty(monthDetailStatisticList)){
                    result.setCode(1);
                    result.setData(monthDetailStatisticList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/ping/month/measure/detail/statistic"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<MonthDetailStatistic>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 某个月 每台设备的月能耗数据 导出
     * @param paramMap
     * @param request
     * @param response
     */
    @GetMapping(value = "/month/measure/detail/statistic/export", produces = CommonUtils.MediaTypeJSON)
    public void getMonthDetailExport(@RequestParam Map<String,Object> paramMap,HttpServletRequest request,HttpServletResponse response){
        Object projectIdObj = paramMap.get("projectId");
        Object yearObj = paramMap.get("year");
        Object monthObj = paramMap.get("month");
        Long projectId=Long.parseLong(projectIdObj.toString());
        String year=yearObj.toString();
        String month=monthObj.toString();
        List<MonthDetailStatistic> monthDetailStatisticList=energyPingService.getMonthDetail(projectId,year,month);
        Map<String, String[][]> monthTotalStatistic = getMonthDetailStatistic(monthDetailStatisticList);
        List<String> title = new ArrayList<>();
        title.add("设备名称");
        title.add("能耗类型");
        title.add("年");
        title.add("月");
        title.add("总能耗(kW·h)");
        title.add("同比数据(kW·h)");
        title.add("数据保存日期");
        try {
            Project project = projectService.findById(projectId);
            String fileName = project.getName()+" " +year+"-"+month+ " 设备耗能统计" + ".xlsx";
            String realName = FileHelper.getEncodeName(fileName, request);
            XSSFWorkbook workbook = ExcelUtils.exportDate(title, monthTotalStatistic);
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + FileHelper.getDownloadName(realName, request));//默认Excel名称
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/ping/month/measure/detail/statistic/export"));
        }

    }


    /**
     * 查看设备的实时数据
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/equipment/now/detail/statistic", produces = CommonUtils.MediaTypeJSON)
    public String getEquipmentDetail(@RequestBody Map<String,Object> paramMap){
        Result<List<EquipmentDetailNow>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                result= Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                List<EquipmentDetailNow> monthDetailStatisticList=energyPingService.getEquipmentDetail(projectId);
                if(!CollectionUtils.isEmpty(monthDetailStatisticList)){
                    result.setCode(1);
                    result.setData(monthDetailStatisticList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/ping/equipment/now/detail/statistic"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<EquipmentDetailNow>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 获取设备报表数据 1天  2月  3季度  4年
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/equipment/report/detail",produces = CommonUtils.MediaTypeJSON)
    public String equipmentReport(@RequestBody Map<String,Object> paramMap){
        Result<List<EquipmentReportDetail>> result=new Result<List<EquipmentReportDetail>>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object typeObj = paramMap.get("type");
            if(projectIdObj==null||typeObj==null){
               result= Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                Integer type=Integer.parseInt(typeObj.toString());
                List<EquipmentReportDetail> equipmentReportDetails=energyPingService.getEquipmentReport(projectId,type);
                if(CollectionUtils.isEmpty(equipmentReportDetails)){
                  result=  Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(equipmentReportDetails);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/ping/equipment/report/detail"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<EquipmentReportDetail>>>(){}.getType();
        return  JsonUtils.toJson(result,type);
    }


    /**
     *  获取最近半年的环比数据
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/month/ringRatio/detail",produces = CommonUtils.MediaTypeJSON)
    public  String getMonthRingRatioDetail(@RequestBody Map<String,Object> paramMap){
        Result<Map<String,Object>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                Map<String,Object> dataMap=energyPingService.getRingRatioMeasure(projectId);
                if(CollectionUtils.isEmpty(dataMap)){
                    result = Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(dataMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/ping/month/ringRatio/detail"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Map<String,Object>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 获取类比数据
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/month/analogy/detail",produces = CommonUtils.MediaTypeJSON)
    public  String getMonthAnalogyDetail(@RequestBody Map<String,Object> paramMap){
        Result<Map<String,Object>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                Map<String,Object> dataMap=energyPingService.getMonthAnalogyDetail(projectId);
                if(CollectionUtils.isEmpty(dataMap)){
                    result = Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(dataMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/ping/month/analogy/detail"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Map<String,Object>>>(){}.getType();
        return JsonUtils.toJson(result,type);


    }




    private Map<String, String[][]> getDateTimeMeasureExport(List<EnergyMeasureDateTime> energyMeasureDateTimes, String begTime, String endTime) {
        if (!CollectionUtils.isEmpty(energyMeasureDateTimes)) {
           String[][] totalArr = new String[energyMeasureDateTimes.size()][10];
            Map<String, String[][]> dataMap = new LinkedHashMap<>();
            for (int i = 0; i < energyMeasureDateTimes.size(); i++) {
                EnergyMeasureDateTime energyMeasureDateTime = energyMeasureDateTimes.get(i);
                totalArr[i][0] = energyMeasureDateTime.getEquipmentName();
                totalArr[i][1] = energyMeasureDateTime.getConsumeTypeName();
                totalArr[i][2] = energyMeasureDateTime.getEnergyTypeName();
                String spikeMeasure = energyMeasureDateTime.getSpikeMeasure();
                if (spikeMeasure.equals("0")) {
                    totalArr[i][3] = "0";
                } else {
                    totalArr[i][3] = spikeMeasure;
                }
                String peakMeasure = energyMeasureDateTime.getPeakMeasure();
                if (peakMeasure.equals("0")) {
                    totalArr[i][4] = "0";
                } else {
                    totalArr[i][4] = peakMeasure;
                }
                String normalMeasure = energyMeasureDateTime.getNormalMeasure();
                if (normalMeasure.equals("0")) {
                    totalArr[i][5] = "0";
                } else {
                    totalArr[i][5] = normalMeasure;
                }
                String valleyMeasure = energyMeasureDateTime.getValleyMeasure();
                if (valleyMeasure.equals("0")) {
                    totalArr[i][6] = "0";
                } else {
                    totalArr[i][6] = valleyMeasure;

                }
                String totalMeasure = energyMeasureDateTime.getTotalMeasure();
                if (totalMeasure.equals("0")) {
                    totalArr[i][7] = "0";
                } else {
                    totalArr[i][7] = totalMeasure;

                }
                totalArr[i][8] = begTime;
                totalArr[i][9] = endTime;

            }
            dataMap.put("电度数据", totalArr);
            return dataMap;

        }
        return null;
    }

    private Map<String, String[][]> getMonthTotalStatistic(List<MonthMeasureStatistic> measureStatistics){
        if(!CollectionUtils.isEmpty(measureStatistics)){
            String[][] totalArr = new String[measureStatistics.size()][6];
            Map<String, String[][]> dataMap=new LinkedHashMap<>();
            for (int i = 0; i < measureStatistics.size(); i++) {
                MonthMeasureStatistic monthMeasureStatistic = measureStatistics.get(i);
                totalArr[i][0]=monthMeasureStatistic.getYear();
                totalArr[i][1]=monthMeasureStatistic.getMonth();
                totalArr[i][2]=String.valueOf(monthMeasureStatistic.getEnergyCount());
                totalArr[i][3]=monthMeasureStatistic.getTotalConsume();
                totalArr[i][4]=monthMeasureStatistic.getYearOverYearConsume();
                totalArr[i][5]=monthMeasureStatistic.getYearMonthDay();

            }
            dataMap.put("月电度数据统计",totalArr);
            return dataMap;
        }
        return null;
    }

    private Map<String, String[][]> getMonthDetailStatistic(List<MonthDetailStatistic> monthDetailStatisticList) {
        if(!CollectionUtils.isEmpty(monthDetailStatisticList)){
            String[][] totalArr = new String[monthDetailStatisticList.size()][7];
            Map<String, String[][]> dataMap=new LinkedHashMap<>();
            for (int i = 0; i < monthDetailStatisticList.size(); i++) {
                MonthDetailStatistic monthMeasureStatistic = monthDetailStatisticList.get(i);
                totalArr[i][0]=monthMeasureStatistic.getEquipmentName();
                totalArr[i][1]=monthMeasureStatistic.getConsumeTypeName();
                totalArr[i][2]=String.valueOf(monthMeasureStatistic.getYear());
                totalArr[i][3]=monthMeasureStatistic.getMonth();
                totalArr[i][4]=monthMeasureStatistic.getMonthTotal();
                totalArr[i][5]=monthMeasureStatistic.getYearOverYearMonthTotal();
                totalArr[i][6]=monthMeasureStatistic.getYearMonthDay();

            }
            dataMap.put("设备月能耗统计",totalArr);
            return dataMap;
        }
        return null;
    }

    /**
     * 获取报表数据 (流向图 占比 和变压器负荷等)
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/report/base/data",produces = CommonUtils.MediaTypeJSON)
    public String getEnergyReportData(@RequestBody Map<String,Object> paramMap){
        Result<EnergyReportDataDetail> result=new Result();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object typeIdObj = paramMap.get("typeId");
            if(projectIdObj==null||typeIdObj==null){
                 result = Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                Integer typeId=Integer.parseInt(typeIdObj.toString());
                EnergyReportDataDetail energyReportDataDetail= energyPingService.getReportData(projectId,typeId);
                if(energyReportDataDetail==null){
                   result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(energyReportDataDetail);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/ping/report/base/data"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<EnergyReportDataDetail>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 获取月环比消耗数据
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/report/ring/data",produces = CommonUtils.MediaTypeJSON)
    public String getReportRingData(@RequestBody Map<String,Object> paramMap){
        Result<List<EnergyConsumeStatisticDetail>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId = Long.parseLong(projectIdObj.toString());
                List<EnergyConsumeStatisticDetail> details = energyPingService.monthCompareStatistic(projectId);
                if(CollectionUtils.isEmpty(details)){
                    result=Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(details);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/ping/report/ring/data"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<EnergyConsumeStatisticDetail>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    /**
     * 获取年类比 月能耗
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/report/analogy/data",produces = CommonUtils.MediaTypeJSON)
    public String getReportYearData(@RequestBody Map<String,Object> paramMap){
        Result<List<EnergyStatisticYearDetail>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
               result= Result.paramError(result);
            }else{
                Long projectId = Long.parseLong(projectIdObj.toString());
                List<EnergyStatisticYearDetail> energyStatisticYearDetails=energyPingService.getReportYearData(projectId);
                if(!CollectionUtils.isEmpty(energyStatisticYearDetails)){
                    result.setData(energyStatisticYearDetails);
                    result.setCode(1);
                }else{
                    result=Result.empty(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/ping/report/analogy/data"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<EnergyStatisticYearDetail>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 获取月/年排名数据
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/report/rank/data",produces = CommonUtils.MediaTypeJSON)
    public String getReportRankData(@RequestBody Map<String,Object> paramMap){
        Result<List<EquipmentRankReport>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object typeIdObj = paramMap.get("typeId");
            if(projectIdObj==null||typeIdObj==null){
               result= Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                Integer typeId=Integer.parseInt(typeIdObj.toString());
                List<EquipmentRankReport> equipmentRankReports=energyPingService.getReportRankData(projectId,typeId);
                if(CollectionUtils.isEmpty(equipmentRankReports)){
                   result = Result.empty(result);
                }else{
                    result.setData(equipmentRankReports);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/ping/report/analogy/data"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<EquipmentRankReport>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
}
