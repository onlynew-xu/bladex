package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.*;
import com.steelman.iot.platform.entity.dto.EnergyYearMonthConsumeDate;
import com.steelman.iot.platform.entity.dto.EquipmentCenterInfo;
import com.steelman.iot.platform.entity.dto.EquipmentInfoDto;
import com.steelman.iot.platform.entity.dto.MeasureData;
import com.steelman.iot.platform.entity.vo.*;
import com.steelman.iot.platform.service.*;
import com.steelman.iot.platform.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.bytebuddy.description.method.MethodDescription;
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
 * @DATE 2021/3/30 0030 13:52
 * @Description:
 */
@Api(tags = "能源管理")
@RestController
@RequestMapping("/api/energy")
public class EnergyController extends BaseController {
    @Resource
    private EnergyEquipmentService energyEquipmentService;
    @Resource
    private EnergyEquipmentDeviceService equipmentDeviceService;
    @Resource
    private ProjectService projectService;

    @Resource
    private EnergyEquipmentOneDeviceService energyEquipmentOneDeviceService;
    @Resource
    private EnergyEquipmentSecondDeviceService energyEquipmentSecondDeviceService;
    @Resource
    private EnergyEquipmentThirdDeviceService energyEquipmentThirdDeviceService;
    @Resource
    private EnergyEquipmentFourthDeviceService energyEquipmentFourthDeviceService;
    @Resource
    private EnergyEquipmentOneService energyEquipmentOneService;
    @Resource
    private EnergyEquipmentSecondService energyEquipmentSecondService;
    @Resource
    private EnergyEquipmentThirdService energyEquipmentThirdService;

    /**
     * 能耗总计
     *
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/measurement/all", produces = CommonUtils.MediaTypeJSON)
    public String countConsume(@RequestBody Map<String, Object> paramMap) {
        Result<MeasureData> result = new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if (projectIdObj == null) {
                result = Result.paramError(result);
            } else {
                Long projectId = Long.parseLong(projectIdObj.toString());
                MeasureData measureData = energyEquipmentService.getTotalMeasurement(projectId);
                if (measureData != null) {
                    result.setData(measureData);
                    result.setCode(1);
                } else {
                    result = Result.empty(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e, "/api/energy/measurement/all"));
            result = Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<MeasureData>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }

    /**
     * 一级能耗设备总数
     */
    @PostMapping(value = "/energy-equipment-one/count", produces = CommonUtils.MediaTypeJSON)
    public String getEnergyEquipmentOneCount() {
        Result<Map<String, Integer>> result = new Result<>();
        result.setCode(0);

        try {
            Integer id = energyEquipmentOneDeviceService.getEnergyEquipmentOneCount();
            if (id==null){
                result = Result.paramError(result);
            }else {
                Integer energyEquipmentOneCount = energyEquipmentOneDeviceService.getEnergyEquipmentOneCount();
                if (energyEquipmentOneCount>0){
                    result= Result.success("操作成功", energyEquipmentOneCount);
                    Map<String, Integer> data = new HashMap<>();
                    data.put("count",energyEquipmentOneCount);
                    result.setData(data);
                }else {
                    result = Result.empty(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/energy-equipment-one/count"));
            result = Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<Map<String, Integer>>>() {
        }.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 返回一级设备表
     */
    @PostMapping(value = "/energy-equipment-one-device/ids", produces = CommonUtils.MediaTypeJSON)
    public String getIdAndEquipmentOneId() {
        Result<List<Map<String, Object>>> result = new Result<>();
        result.setCode(0);
        try {
            List<EnergyEquipmentOneDevice> records = energyEquipmentOneDeviceService.findAll();

//            for (EnergyEquipmentDevice record : records) {
//                System.out.println(record.getId());
//                System.out.println(record.getEquipmentId());
//
//            }

            if (records != null && !records.isEmpty()) {
                List<Map<String, Object>> data = new ArrayList<>();
                for (EnergyEquipmentOneDevice record : records) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", record.getId());
                    map.put("equipmentOneId", record.getEquipmentOneId());
                    data.add(map);
                }
                result = Result.success("查询成功", data);
            } else {
                result = Result.empty(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e, "/api/energy/energy-equipment-one-device/ids"));
            result = Result.exceptionRe(result);
        }

        Type type = new TypeToken<Result<List<Map<String, Object>>>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }



        /**
         * 一级能耗名字
         */
    @PostMapping(value = "/energy-equipment-one/name", produces = CommonUtils.MediaTypeJSON)
    public String getEnergyEquipmentOneNameById(@RequestParam("id") Long id) {
        Result<Map<String, String>> result = new Result<>();
        result.setCode(0);

        try {
            EnergyEquipmentOne energyEquipmentOne = energyEquipmentOneService.selectByPrimaryKey(id);

            if (energyEquipmentOne == null) {
                result = Result.paramError(result);
            } else {
                String name = energyEquipmentOne.getName();
                Map<String, String> data = new HashMap<>();
                data.put("name", name);
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e, "/api/energy-equipment-one/data"));
            result = Result.exceptionRe(result);
        }

        Type type = new TypeToken<Result<Map<String, String>>>() {}.getType();
        return JsonUtils.toJson(result, type);
    }


    /**
     * 获取一级设备详情信息
     */
    @PostMapping(value = "/energy-equipment-one/data", produces = CommonUtils.MediaTypeJSON)
    public String getEnergyEquipmentOneDataById(@RequestParam("id") Long id) {
        Result<EnergyEquipmentOne> result = new Result<>();
        try {
            EnergyEquipmentOne energyEquipmentOne = energyEquipmentOneService.selectByPrimaryKey(id);
            if (energyEquipmentOne == null) {
                result.setCode(-1);
                result.setMsg("未查询到对应的记录");
            } else {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(energyEquipmentOne);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e, "/api/energy-equipment-one/data"));
            result.setCode(-2);
            result.setMsg("查询异常");
        }

        Type type = new TypeToken<Result<EnergyEquipmentOne>>() {}.getType();
        return JsonUtils.toJson(result, type);
    }



    /**
     * 二级能耗设备总数
     */
    @PostMapping(value = "/energy-equipment-second/count", produces = CommonUtils.MediaTypeJSON)
    public String getEnergyEquipmentSecondCount() {
        Result<Map<String, Integer>> result = new Result<>();
        result.setCode(0);
        try {
            Integer id = energyEquipmentSecondDeviceService.getEnergyEquipmentSecondCount();
            if (id == null) {
                result = Result.paramError(result);
            } else {
                Integer energyEquipmentSecondCount = energyEquipmentSecondDeviceService.getEnergyEquipmentSecondCount();
                 if (energyEquipmentSecondCount>0){
                     result= Result.success("操作成功", energyEquipmentSecondCount);
                     Map<String, Integer> data = new HashMap<>();
                     data.put("count", energyEquipmentSecondCount);
                     result.setData(data);

                } else {
                    result = Result.empty(result);
                }
            }
        } catch (Exception e) {
            //打印异常堆栈信息
            e.printStackTrace();
            //使用logger记录错误日志
            logger.error(ExceptionUtils.getStackMsg(e, "/api/energy/energy-equipment-second/count"));
            //设计结果为异常信息
            result = Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<Map<String, Integer>>>() {
        }.getType();

        return JsonUtils.toJson(result, type);
    }

    /**
     * 返回二级设备表
     */
    @PostMapping(value = "/energy-equipment-second-device/ids", produces = CommonUtils.MediaTypeJSON)
    public String getIdAndEquipmentSecondId() {
        Result<List<Map<String, Object>>> result = new Result<>();
        result.setCode(0);
        try {
            List<EnergyEquipmentSecondDevice> records = energyEquipmentSecondDeviceService.findAll();
//
//            for (EnergyEquipmentSecondDevice record : records) {
//                System.out.println(record.getId());
//                System.out.println(record.getEquipmentSecondId());
//
//            }

            if (records != null && !records.isEmpty()) {
                List<Map<String, Object>> data = new ArrayList<>();
                for (EnergyEquipmentSecondDevice record : records) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", record.getId());
                    map.put("equipmentSecondId", record.getEquipmentSecondId());
                    data.add(map);
                }
                result = Result.success("查询成功", data);
            } else {
                result = Result.empty(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e, "/api/energy/energy-equipment-second-device/ids"));
            result = Result.exceptionRe(result);
        }

        Type type = new TypeToken<Result<List<Map<String, Object>>>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }

    /**
     * 二级能耗名字
     */
    @PostMapping(value = "/energy-equipment-second/name", produces = CommonUtils.MediaTypeJSON)
    public String getEnergyEquipmentSecondById(@RequestParam("id") Long id) {
        Result<Map<String, String>> result = new Result<>();
        result.setCode(0);

        try {
            EnergyEquipmentSecond energyEquipmentSecond = energyEquipmentSecondService.selectByPrimaryKey(id);

            if (energyEquipmentSecond == null) {
                result = Result.paramError(result);
            } else {
                String name = energyEquipmentSecond.getName();
                Map<String, String> data = new HashMap<>();
                data.put("name", name);
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e, "/api/energy-equipment-second/data"));
            result = Result.exceptionRe(result);
        }

        Type type = new TypeToken<Result<Map<String, String>>>() {}.getType();
        return JsonUtils.toJson(result, type);
    }


    /**
     * 获取二级设备详情信息
     */
    @PostMapping(value = "/energy-equipment-second/data", produces = CommonUtils.MediaTypeJSON)
    public String getEnergyEquipmentSecondDataById(@RequestParam("id") Long id) {
        Result<EnergyEquipmentSecond> result = new Result<>();
        try {
            EnergyEquipmentSecond energyEquipmentSecond = energyEquipmentSecondService.selectByPrimaryKey(id);
            if (energyEquipmentSecond == null) {
                result.setCode(-1);
                result.setMsg("未查询到对应的记录");
            } else {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(energyEquipmentSecond);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e, "/api/energy-equipment-second/data"));
            result.setCode(-2);
            result.setMsg("查询异常");
        }

        Type type = new TypeToken<Result<EnergyEquipmentSecond>>() {}.getType();
        return JsonUtils.toJson(result, type);
    }


    /**
     * 三级能耗设备总数
     */
    @PostMapping(value = "/energy-equipment-third/count", produces = CommonUtils.MediaTypeJSON)
    public String getEnergyEquipmentThirdCount() {
        Result<Map<String, Integer>> result = new Result<>();
        //设置初始code为0
        result.setCode(0);

        try {

            Integer id = energyEquipmentThirdDeviceService.getEnergyEquipmentThirdCount();
            if (id==null){
                result = Result.paramError(result);
            }else {
                //得到表id总数

                Integer energyEquipmentThirdCount = energyEquipmentThirdDeviceService.getEnergyEquipmentThirdCount();
                if (energyEquipmentThirdCount!=null){
                    result.setCode(1);
                    result.setMsg("查询成功");
                    Map<String, Integer> data = new HashMap<>();
                    data.put("count", energyEquipmentThirdCount);
                    result.setData(data);
                }else {
                    result = Result.empty(result);
                }
            }
        } catch (Exception e) {
            //打印异常堆栈信息
            e.printStackTrace();
            //使用logger记录错误日志
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/energy-equipment-third/count"));
            //设计结果为异常信息
            result = Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<Integer>>() {
        }.getType();

        return JsonUtils.toJson(result,type);

    }

    /**
     * 返回三级设备表
     */
    @PostMapping(value = "/energy-equipment-third-device/ids", produces = CommonUtils.MediaTypeJSON)
    public String getIdAndEquipmentThirdId() {
        Result<List<Map<String, Object>>> result = new Result<>();
        result.setCode(0);
        try {
            List<EnergyEquipmentThirdDevice> records = energyEquipmentThirdDeviceService.findAll();
//
//            for (EnergyEquipmentSecondDevice record : records) {
//                System.out.println(record.getId());
//                System.out.println(record.getEquipmentSecondId());
//
//            }

            if (records != null && !records.isEmpty()) {
                List<Map<String, Object>> data = new ArrayList<>();
                for (EnergyEquipmentThirdDevice record : records) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", record.getId());
                    map.put("equipmentThirdId", record.getEquipmentThirdId());
                    data.add(map);
                }
                result = Result.success("查询成功", data);
            } else {
                result = Result.empty(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e, "/api/energy/energy-equipment-third-device/ids"));
            result = Result.exceptionRe(result);
        }

        Type type = new TypeToken<Result<List<Map<String, Object>>>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }
    /**
     * 三级能耗名字
     */
    @PostMapping(value = "/energy-equipment-third/name", produces = CommonUtils.MediaTypeJSON)
    public String getEnergyEquipmentThirdById(@RequestParam("id") Long id) {
        Result<Map<String, String>> result = new Result<>();
        result.setCode(0);

        try {
            EnergyEquipmentThird energyEquipmentThird = energyEquipmentThirdService.selectByPrimaryKey(id);

            if (energyEquipmentThird == null) {
                result = Result.paramError(result);
            } else {
                String name = energyEquipmentThird.getName();
                Map<String, String> data = new HashMap<>();
                data.put("name", name);
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e, "/api/energy-equipment-third/data"));
            result = Result.exceptionRe(result);
        }

        Type type = new TypeToken<Result<Map<String, String>>>() {}.getType();
        return JsonUtils.toJson(result, type);
    }


    /**
     * 获取三级设备详情信息
     */
    @PostMapping(value = "/energy-equipment-third/data", produces = CommonUtils.MediaTypeJSON)
    public String getEnergyEquipmentThirdDataById(@RequestParam("id") Long id) {
        Result<EnergyEquipmentThird> result = new Result<>();
        try {
            EnergyEquipmentThird energyEquipmentThird = energyEquipmentThirdService.selectByPrimaryKey(id);
            if (energyEquipmentThird == null) {
                result.setCode(-1);
                result.setMsg("未查询到对应的记录");
            } else {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(energyEquipmentThird);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e, "/api/energy-equipment-third/data"));
            result.setCode(-2);
            result.setMsg("查询异常");
        }

        Type type = new TypeToken<Result<EnergyEquipmentThird>>() {}.getType();
        return JsonUtils.toJson(result, type);
    }

    /**
     * 四级能耗设备总数
     */
    @PostMapping(value = "/energy-equipment-fourth/count", produces = CommonUtils.MediaTypeJSON)
    public String getEnergyEquipmentFourCount() {
        Result<Map<String, Integer>> result = new Result<>();
        //设置初始code为0
        result.setCode(0);

        try {
            Integer id = energyEquipmentFourthDeviceService.getEnergyEquipmentFourCount();

            if (id==null){
                result = Result.paramError(result);
            }else {
                //得到表id总数
                Integer energyEquipmentFourCount = energyEquipmentFourthDeviceService.getEnergyEquipmentFourCount();
                if (energyEquipmentFourCount!=null){
                    result.setCode(1);
                    result.setMsg("查询成功");
                    Map<String, Integer> data = new HashMap<>();
                    data.put("count", energyEquipmentFourCount);
                    result.setData(data);
                }else {
                    result = Result.empty(result);
                }
            }
        } catch (Exception e) {
            //打印异常堆栈信息
            e.printStackTrace();
            //使用logger记录错误日志
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/energy-equipment-fourth/count"));
            //设计结果为异常信息
            result = Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<Integer>>() {
        }.getType();

        return JsonUtils.toJson(result,type);

    }


    /**
     * 能源设备类型占比
     *
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/type/energy/statistic", produces = CommonUtils.MediaTypeJSON)
    public String countEnergyDeviceType(@RequestBody Map<String, Object> paramMap) {
        Result<List<EnergyEquipmentStatistic>> result = new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if (projectIdObj == null) {
                result = Result.paramError(result);
            } else {
                Long projectId = Long.parseLong(projectIdObj.toString());
                List<EnergyEquipmentStatistic> dataList = energyEquipmentService.statisticEnergyType(projectId);
                if (!CollectionUtils.isEmpty(dataList)) {
                    result.setData(dataList);
                    result.setCode(1);
                } else {
                    result = Result.empty(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e, "/api/energy/type/energy/statistic"));
            result = Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<List<EnergyEquipmentStatistic>>>() {
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
            logger.error(ExceptionUtils.getStackMsg(e, "/api/energy/type/consume/statistic"));
            result = Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<List<EnergyConsumeStatistic>>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }


    @ApiOperation("能耗每日趋势图")
    @PostMapping(value = "/week/energy/statistic", produces = CommonUtils.MediaTypeJSON)
    public String weekEnergyStatistic(@RequestBody Map<String, Object> paramMap) {
        Result<Map<String, List<WeekEnergyConsumeStatistic>>> result = new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramMap.get("projectId");
            if (projectObj == null) {
                result = Result.paramError(result);
            } else {
                Long projectId = Long.parseLong(projectObj.toString());
                Map<String, List<WeekEnergyConsumeStatistic>> dataMap = energyEquipmentService.getWeekEnergyStatistic(projectId);
                if (CollectionUtils.isEmpty(dataMap)) {
                    result = Result.empty(result);
                } else {
                    result.setData(dataMap);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e, "/api/energy/week/energy/statistic"));
            result = Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<Map<String, List<WeekEnergyConsumeStatistic>>>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }


    /**
     * 能耗排行榜数据
     *
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/rank", produces = CommonUtils.MediaTypeJSON)
    public String deviceEnergyConsumePartRank(@RequestBody Map<String, Object> paramMap) {
        Result<List<EnergyDeviceInfo>> result = new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object typeIdObj = paramMap.get("type");
            Object part = paramMap.get("part");
            if (projectIdObj == null || typeIdObj == null) {
                result = Result.paramError(result);
            } else {
                Long projectId = Long.parseLong(projectIdObj.toString());
                Integer typeId = Integer.parseInt(typeIdObj.toString());
                List<EnergyDeviceInfo> dataList = equipmentDeviceService.deviceEnergyConsumePartRank(projectId, typeId, part);
                if (CollectionUtils.isEmpty(dataList)) {
                    result = Result.empty(result);
                } else {
                    result.setData(dataList);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e, "/api/energy/rank"));
            result = Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<List<EnergyDeviceInfo>>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }

    /**
     * 导出数据接口数据
     *
     * @param projectId
     * @param response
     * @param request
     */
    @GetMapping(value = "/rank/export", produces = CommonUtils.MediaTypeJSON)
    public void rankExport(@RequestParam Long projectId, HttpServletResponse response, HttpServletRequest request) {
        Map<String, String[][]> dataMap = equipmentDeviceService.getExportEnergyConsumeRank(projectId);
        List<String> title = new ArrayList<>();
        title.add("设备名称");
        title.add("设备类别");
        title.add("能耗类别");
        title.add("设备位置");
        title.add("能耗总计");
        try {
            Project project = projectService.findById(projectId);
            String fileName = project.getName() + "能耗设备排行榜" + ".xlsx";
            String realName = FileHelper.getEncodeName(fileName, request);
            XSSFWorkbook workbook = ExcelUtils.exportDate(title, dataMap);
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + FileHelper.getDownloadName(realName, request));//默认Excel名称
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e, "/api/energy/rank/export"));
        }
    }

    /**
     * 能耗月趋势图
     *
     * @param paramMap
     */
    @PostMapping(value = "/month/statistic", produces = CommonUtils.MediaTypeJSON)
    public String rankExport(@RequestBody Map<String, Object> paramMap) {
        Result<Map<String, List<EnergyYearMonthConsumeDate>>> result = new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if (projectIdObj == null) {
                result = Result.paramError(result);
            } else {
                Long projectId = Long.parseLong(projectIdObj.toString());
                Map<String, List<EnergyYearMonthConsumeDate>> dataMap = energyEquipmentService.getYearMonthConsumeData(projectId);
                if (CollectionUtils.isEmpty(dataMap)) {
                    result = Result.empty(result);
                } else {
                    result.setCode(1);
                    result.setData(dataMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e, "/api/energy/month/statistic"));
            result = Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<Map<String, List<EnergyYearMonthConsumeDate>>>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }


    /**
     * 能耗统计中心
     *
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/energy/statistic", produces = CommonUtils.MediaTypeJSON)
    public String energyStatistic(@RequestBody Map<String, Object> paramMap) {
        Result<Map<String, List<EnergyDeviceInfo>>> result = new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object consumeTypeIdObj = paramMap.get("consumeTypeId");
            Object energyTypeIdObj = paramMap.get("energyTypeId");
            Object beginTimeObj = paramMap.get("beginTime");
            Object endTimeObj = paramMap.get("endTime");
            Object orderObj = paramMap.get("order");
            Object partObj = paramMap.get("part");
            if (projectIdObj == null || consumeTypeIdObj == null || energyTypeIdObj == null || beginTimeObj == null || endTimeObj == null || orderObj == null || partObj == null) {
                result = Result.paramError(result);
            } else {
                Long projectId = Long.parseLong(projectIdObj.toString());
                Long consumeTypeId = Long.parseLong(consumeTypeIdObj.toString());
                Long energyTypeId = Long.parseLong(energyTypeIdObj.toString());
                String beginTime = beginTimeObj.toString();
                String endTime = endTimeObj.toString();
                String order = orderObj.toString();
                String part = partObj.toString();
                Map<String, List<EnergyDeviceInfo>> dataList = energyEquipmentService.getEnergyDateStatistic(projectId, consumeTypeId, energyTypeId, beginTime, endTime, order, part);
                if (CollectionUtils.isEmpty(dataList)) {
                    result = Result.empty(result);
                } else {
                    result.setData(dataList);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e, "/api/energy/energy/statistic"));
            result = Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<Map<String, List<EnergyDeviceInfo>>>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }

    @GetMapping(value = "/energy/statistic/export", produces = CommonUtils.MediaTypeJSON)
    public void energyStatisticExport(@RequestParam Map<String, Object> paramMap, HttpServletResponse response, HttpServletRequest request) {
        Object projectIdObj = paramMap.get("projectId");
        Object consumeTypeIdObj = paramMap.get("consumeTypeId");
        Object energyTypeIdObj = paramMap.get("energyTypeId");
        Object beginTimeObj = paramMap.get("beginTime");
        Object endTimeObj = paramMap.get("endTime");
        Object orderObj = paramMap.get("order");
        Long projectId = Long.parseLong(projectIdObj.toString());
        Long consumeTypeId = Long.parseLong(consumeTypeIdObj.toString());
        Long energyTypeId = Long.parseLong(energyTypeIdObj.toString());
        String beginTime = beginTimeObj.toString();
        String endTime = endTimeObj.toString();
        String order = orderObj.toString();
        Map<String, List<EnergyDeviceInfo>> dataList = energyEquipmentService.getEnergyDateStatistic(projectId, consumeTypeId, energyTypeId, beginTime, endTime, order, "all");
        Map<String, String[][]> dataMap = getDownloadDate(dataList);
        List<String> title = new ArrayList<>();
        title.add("设备名称");
        title.add("能耗类别");
        title.add("设备类别");
        title.add("设备位置");
        title.add("能耗总计(kW·h)");
        try {
            Project project = projectService.findById(projectId);
            String fileName = project.getName() + beginTime + "~" + endTime + "耗能统计" + ".xlsx";
            String realName = FileHelper.getEncodeName(fileName, request);
            XSSFWorkbook workbook = ExcelUtils.exportDate(title, dataMap);
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + FileHelper.getDownloadName(realName, request));//默认Excel名称
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e, "/api/energy/energy/statistic/export"));
        }


    }

    private Map<String, String[][]> getDownloadDate(Map<String, List<EnergyDeviceInfo>> dataList) {
        Map<String, String[][]> dataMap = new LinkedHashMap<>();
        List<EnergyDeviceInfo> energyDeviceInfoList = dataList.get("total");
        String[][] totalArr = new String[energyDeviceInfoList.size()][5];
        for (int i = 0; i < energyDeviceInfoList.size(); i++) {
            EnergyDeviceInfo energyDeviceInfo = energyDeviceInfoList.get(i);
            totalArr[i][0] = energyDeviceInfo.getName();
            totalArr[i][1] = energyDeviceInfo.getConsumeTypeName();
            totalArr[i][2] = energyDeviceInfo.getEnergyTypeName();
            totalArr[i][3] = energyDeviceInfo.getLocation();
            if (energyDeviceInfo.getTotalData().equals(0f)) {
                totalArr[i][4] = "0";
            } else {
                totalArr[i][4] = energyDeviceInfo.getTotalData().toString();
            }
        }
        dataMap.put("总计", totalArr);
        List<EnergyDeviceInfo> energyDeviceInfoListSpike = dataList.get("spike");
        String[][] spikeArr = new String[energyDeviceInfoListSpike.size()][5];
        for (int i = 0; i < energyDeviceInfoListSpike.size(); i++) {
            EnergyDeviceInfo energyDeviceInfo = energyDeviceInfoListSpike.get(i);
            spikeArr[i][0] = energyDeviceInfo.getName();
            spikeArr[i][1] = energyDeviceInfo.getConsumeTypeName();
            spikeArr[i][2] = energyDeviceInfo.getEnergyTypeName();
            spikeArr[i][3] = energyDeviceInfo.getLocation();
            if (energyDeviceInfo.getTotalData().equals(0f)) {
                spikeArr[i][4] = "0";
            } else {
                spikeArr[i][4] = energyDeviceInfo.getTotalData().toString();
            }

        }
        dataMap.put("尖时", spikeArr);
        List<EnergyDeviceInfo> energyDeviceInfoListPeak = dataList.get("peak");
        String[][] peakArr = new String[energyDeviceInfoListPeak.size()][5];
        for (int i = 0; i < energyDeviceInfoListPeak.size(); i++) {
            EnergyDeviceInfo energyDeviceInfo = energyDeviceInfoListPeak.get(i);
            peakArr[i][0] = energyDeviceInfo.getName();
            peakArr[i][1] = energyDeviceInfo.getConsumeTypeName();
            peakArr[i][2] = energyDeviceInfo.getEnergyTypeName();
            peakArr[i][3] = energyDeviceInfo.getLocation();
            if (energyDeviceInfo.getTotalData().equals(0f)) {
                peakArr[i][4] = "0";
            } else {
                peakArr[i][4] = energyDeviceInfo.getTotalData().toString();
            }

        }
        dataMap.put("峰时", peakArr);
        List<EnergyDeviceInfo> energyDeviceInfoListNormal = dataList.get("normal");
        String[][] normalArr = new String[energyDeviceInfoListNormal.size()][5];
        for (int i = 0; i < energyDeviceInfoListNormal.size(); i++) {
            EnergyDeviceInfo energyDeviceInfo = energyDeviceInfoListNormal.get(i);
            normalArr[i][0] = energyDeviceInfo.getName();
            normalArr[i][1] = energyDeviceInfo.getConsumeTypeName();
            normalArr[i][2] = energyDeviceInfo.getEnergyTypeName();
            normalArr[i][3] = energyDeviceInfo.getLocation();
            if (energyDeviceInfo.getTotalData().equals(0f)) {
                normalArr[i][4] = "0";
            } else {
                normalArr[i][4] = energyDeviceInfo.getTotalData().toString();
            }

        }
        dataMap.put("平时", normalArr);

        List<EnergyDeviceInfo> energyDeviceInfoListValley = dataList.get("valley");
        String[][] valleyArr = new String[energyDeviceInfoListValley.size()][5];
        for (int i = 0; i < energyDeviceInfoListValley.size(); i++) {
            EnergyDeviceInfo energyDeviceInfo = energyDeviceInfoListValley.get(i);
            valleyArr[i][0] = energyDeviceInfo.getName();
            valleyArr[i][1] = energyDeviceInfo.getConsumeTypeName();
            valleyArr[i][2] = energyDeviceInfo.getEnergyTypeName();
            valleyArr[i][3] = energyDeviceInfo.getLocation();
            if (energyDeviceInfo.getTotalData().equals(0f)) {
                valleyArr[i][4] = "0";
            } else {
                valleyArr[i][4] = energyDeviceInfo.getTotalData().toString();
            }

        }
        dataMap.put("谷时", valleyArr);
        return dataMap;
    }

    /**
     * 自动抄表数据
     *
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/energy/reading", produces = CommonUtils.MediaTypeJSON)
    public String energyReading(@RequestBody Map<String, Object> paramMap) {
        Result<Map<String, List<EnergyDeviceInfo>>> result = new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object consumeTypeIdObj = paramMap.get("consumeTypeId");
            Object energyTypeIdObj = paramMap.get("energyTypeId");
            Object orderObj = paramMap.get("order");
            Object partObj = paramMap.get("part");
            if (projectIdObj == null || consumeTypeIdObj == null || energyTypeIdObj == null || orderObj == null || partObj == null) {
                result = Result.paramError(result);
            } else {
                Long projectId = Long.parseLong(projectIdObj.toString());
                Long consumeTypeId = Long.parseLong(consumeTypeIdObj.toString());
                Long energyTypeId = Long.parseLong(energyTypeIdObj.toString());
                String order = orderObj.toString();
                String part = partObj.toString();
                Map<String, List<EnergyDeviceInfo>> dataList = energyEquipmentService.getEnergyReading(projectId, consumeTypeId, energyTypeId, order, part);
                if (CollectionUtils.isEmpty(dataList)) {
                    result = Result.empty(result);
                } else {
                    result.setData(dataList);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e, "/api/energy/energy/reading"));
            result = Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<Map<String, List<EnergyDeviceInfo>>>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }

    /**
     * 自动抄表数据导出
     *
     * @param paramMap
     * @return
     */
    @GetMapping(value = "/energy/reading/export", produces = CommonUtils.MediaTypeJSON)
    public void energyReadingExport(@RequestParam Map<String, Object> paramMap, HttpServletResponse response, HttpServletRequest request) {
        Object projectIdObj = paramMap.get("projectId");
        Object consumeTypeIdObj = paramMap.get("consumeTypeId");
        Object energyTypeIdObj = paramMap.get("energyTypeId");
        Object orderObj = paramMap.get("order");
        Long projectId = Long.parseLong(projectIdObj.toString());
        Long consumeTypeId = Long.parseLong(consumeTypeIdObj.toString());
        Long energyTypeId = Long.parseLong(energyTypeIdObj.toString());
        String order = orderObj.toString();
        Map<String, List<EnergyDeviceInfo>> dataList = energyEquipmentService.getEnergyReading(projectId, consumeTypeId, energyTypeId, order, "all");
        Map<String, String[][]> downloadDate = getDownloadDate(dataList);
        List<String> title = new ArrayList<>();
        title.add("设备名称");
        title.add("能耗类别");
        title.add("设备类别");
        title.add("设备位置");
        title.add("当月能耗(kW·h)");
        try {
            Project project = projectService.findById(projectId);
            LocalDate localDate = LocalDate.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fileName = project.getName() + dateTimeFormatter.format(localDate) + "耗能统计" + ".xlsx";
            String realName = FileHelper.getEncodeName(fileName, request);
            XSSFWorkbook workbook = ExcelUtils.exportDate(title, downloadDate);
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + FileHelper.getDownloadName(realName, request));//默认Excel名称
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e, "/api/energy/energy/reading/export"));
        }
    }


    /**
     * 设备中心
     *
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/equipment/center")
    public String equipmentCenterInfo(@RequestBody Map<String, Object> paramMap) {
        Result<EquipmentCenterInfo> result = new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if (projectIdObj == null) {
                result = Result.empty(result);
            } else {
                Long projectId = Long.parseLong(projectIdObj.toString());
                EquipmentCenterInfo equipmentCenterInfo = energyEquipmentService.getCenterInfoData(projectId);
                if (equipmentCenterInfo.getConsume() == null && equipmentCenterInfo.getEnergy() == null) {
                    result = Result.empty(result);
                } else {
                    result.setData(equipmentCenterInfo);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e, "/api/energy/equipment/center"));
            result = Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<EquipmentCenterInfo>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }

    /**
     * 能耗设备详情页
     *
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/equipment/info")
    public String equipmentInfo(@RequestBody Map<String, Object> paramMap) {
        Result<EquipmentInfoDto> result = new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object equipmentIdObj = paramMap.get("equipmentId");
            if (projectIdObj == null || equipmentIdObj == null) {
                result = Result.paramError(result);
            } else {
                Long projectId = Long.parseLong(projectIdObj.toString());
                Long equipmentId = Long.parseLong(equipmentIdObj.toString());
                EquipmentInfoDto equipmentInfoDto = energyEquipmentService.getEquipmentInfo(equipmentId, projectId);
                if (equipmentInfoDto == null) {
                    result = Result.empty(result);
                } else {
                    result.setData(equipmentInfoDto);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e, "/api/energy/equipment/info"));
            result = Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<EquipmentInfoDto>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }

    /**
     * 能耗每日趋势图
     *
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/equipment/measure/week")
    public String equipmentMeasureWeekInfo(@RequestBody Map<String, Object> paramMap) {
        Result<Map<String, List<WeekEnergyConsumeStatistic>>> result = new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object equipmentIdObj = paramMap.get("equipmentId");
            if (projectIdObj == null || equipmentIdObj == null) {
                result = Result.paramError(result);
            } else {
                Long projectId = Long.parseLong(projectIdObj.toString());
                Long equipmentId = Long.parseLong(equipmentIdObj.toString());
                Map<String, List<WeekEnergyConsumeStatistic>> dataMap = energyEquipmentService.getEquipmentWeekInfo(equipmentId, projectId);
                if (CollectionUtils.isEmpty(dataMap)) {
                    result = Result.empty(result);
                } else {
                    result.setData(dataMap);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e, "/api/energy/equipment/measure/week"));
            result = Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<Map<String, List<WeekEnergyConsumeStatistic>>>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }

    /**
     * 能耗类比数据图
     *
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/equipment/measure/contrast")
    public String measureContrast(@RequestBody Map<String, Object> paramMap) {
        Result<Map<String, List<EnergyConsumeStatisticDetail>>> result = new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramMap.get("projectId");
            Object equipmentObj = paramMap.get("equipmentId");
            if (projectObj == null || equipmentObj == null) {
                result = Result.paramError(result);
            } else {
                Long projectId = Long.parseLong(projectObj.toString());
                Long equipmentId = Long.parseLong(equipmentObj.toString());
                Map<String, List<EnergyConsumeStatisticDetail>> dataMap = energyEquipmentService.getEquipmentContrastData(projectId, equipmentId);
                if (CollectionUtils.isEmpty(dataMap)) {
                    result = Result.empty(result);
                } else {
                    result.setCode(1);
                    result.setData(dataMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e, "/api/energy/equipment/measure/contrast"));
            result = Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<Map<String, List<EnergyConsumeStatisticDetail>>>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }
}
