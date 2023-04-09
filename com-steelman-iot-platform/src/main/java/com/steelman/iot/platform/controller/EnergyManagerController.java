package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.*;
import com.steelman.iot.platform.entity.dto.EntityDto;
import com.steelman.iot.platform.entity.dto.EquipmentInfo;
import com.steelman.iot.platform.entity.dto.EquipmentVariable;
import com.steelman.iot.platform.log.Log;
import com.steelman.iot.platform.service.*;
import com.steelman.iot.platform.utils.CommonUtils;
import com.steelman.iot.platform.utils.ExceptionUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import com.steelman.iot.platform.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/energy/manager")
public class EnergyManagerController extends BaseController {
    @Resource
    private EnergyEquipmentService energyEquipmentService;
    @Resource
    private EnergyTypeService energyTypeService;
    @Resource
    private EnergyConsumeTypeService energyConsumeTypeService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private EnergyEquipmentDeviceService energyEquipmentDeviceService;

    /**
     * 能耗设备类型
     *
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/energy/type/list", produces = CommonUtils.MediaTypeJSON)
    public String getEnergyTypeList(@RequestBody Map<String, Object> paramMap) {
        Result<List<EntityDto>> result = new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramMap.get("projectId");
            if (projectObj == null) {
                result = Result.paramError(result);
            } else {
                Long projectId = Long.parseLong(projectObj.toString());
                List<EnergyType> energyTypeList = energyTypeService.getListByProjectSystem(projectId, 3000L);
                if (CollectionUtils.isEmpty(energyTypeList)) {
                    result = Result.empty(result);
                } else {
                    List<EntityDto> entityDtoList = new ArrayList<>();
                    for (EnergyType energyType : energyTypeList) {
                        entityDtoList.add(new EntityDto(energyType.getId(), energyType.getName(), energyType.getProjectId()));
                    }
                    result.setCode(1);
                    result.setData(entityDtoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/manager/energy/type/list"));
            result=Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<List<EntityDto>>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }

    /**
     * 添加能耗设备类型
     *
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/energy/type/save", produces = CommonUtils.MediaTypeJSON)
    public String saveEnergyType(@RequestBody Map<String, Object> paramMap) {
        Result<Integer> result = new Result<>();
        result.setCode(0);
        try {
            Object energyTypeNameObj = paramMap.get("energyTypeName");
            Object projectObj = paramMap.get("projectId");
            if (energyTypeNameObj == null || projectObj == null) {
                result = Result.paramError(result);
            } else {
                String energyTypeName = energyTypeNameObj.toString();
                Long projectId = Long.parseLong(projectObj.toString());
                EnergyType energyTypeSer = energyTypeService.selectByName(energyTypeName, projectId);
                if (energyTypeSer != null) {
                    result.setMsg("名称已存在，请修改后重试");
                } else {
                    EnergyType energyType = new EnergyType();
                    energyType.setProjectId(projectId);
                    energyType.setSystemId(3000L);
                    energyType.setName(energyTypeName);
                    energyType.setCreateTime(new Date());
                    energyType.setUpdateTime(new Date());
                    energyTypeService.save(energyType);
                    result.setData(1);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/manager/energy/type/save"));
            result=Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<Integer>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }

    /**
     * 修改能耗设备类型
     *
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/energy/type/update", produces = CommonUtils.MediaTypeJSON)
    public String updateEnergyType(@RequestBody Map<String, Object> paramMap) {
        Result<Integer> result = new Result<>();
        result.setCode(0);
        try {
            Object energyTypeNameObj = paramMap.get("energyTypeName");
            Object energyTypeIdObj = paramMap.get("energyTypeId");
            Object projectIdObj = paramMap.get("projectId");
            if (energyTypeNameObj == null || projectIdObj == null || energyTypeIdObj == null) {
                result = Result.paramError(result);
            } else {
                String energyTypeName = energyTypeNameObj.toString();
                Long projectId = Long.parseLong(projectIdObj.toString());
                Long energyTypeId = Long.parseLong(energyTypeIdObj.toString());
                EnergyType energyType = energyTypeService.getInfo(energyTypeId);
                if (energyTypeName.equals(energyType.getName())) {
                    result.setMsg("名称信息未改变，请修改后重试");
                } else {
                    EnergyType energyTypeSer = energyTypeService.selectByName(energyTypeName, projectId);
                    if (energyTypeSer != null) {
                        result.setMsg("名称已存在，请修改后重试");
                    } else {
                        EnergyType energyTypeUpdate = new EnergyType();
                        energyTypeUpdate.setId(energyTypeId);
                        energyType.setName(energyTypeName);
                        energyType.setUpdateTime(new Date());
                        energyTypeService.updateEnergyType(energyType);
                        result.setData(1);
                        result.setCode(1);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/manager/energy/type/update"));
            result=Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<Integer>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }

    /**
     * 删除能耗设备类型
     *
     * @param paramMap
     * @return
     */
    @Log("删除能耗设备类型")
    @PostMapping(value = "/energy/type/delete", produces = CommonUtils.MediaTypeJSON)
    public String deleteEnergyType(@RequestBody Map<String, Object> paramMap) {
        Result<Integer> result = new Result<>();
        result.setCode(0);
        try {
            Object energyTypeIdObj = paramMap.get("energyTypeId");
            Object projectIdObj = paramMap.get("projectId");
            if (projectIdObj == null || energyTypeIdObj == null) {
                result = Result.paramError(result);
            } else {
                Long projectId = Long.parseLong(projectIdObj.toString());
                Long energyTypeId = Long.parseLong(energyTypeIdObj.toString());
                List<EnergyEquipment> energyEquipmentList = energyEquipmentService.selectByEnergyTypeId(energyTypeId, projectId);
                if(CollectionUtils.isEmpty(energyEquipmentList)){
                    Boolean flag = energyTypeService.deleteById(energyTypeId, projectId);
                    if (flag) {
                        result.setData(1);
                        result.setCode(1);
                    }
                }else{
                    result.setMsg("删除失败,该类型下还存在有设备");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/manager/energy/type/delete"));
            result=Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<Integer>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }


    /**
     * 能耗类型列表
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/consume/type/list", produces = CommonUtils.MediaTypeJSON)
    public String consumeTypeList(@RequestBody Map<String,Object> paramMap) {
        Result< List<EntityDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramMap.get("projectId");
            if (projectObj==null) {
                result = Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectObj.toString());
                List<EnergyConsumeType> energyConsumeTypeServiceList = energyConsumeTypeService.getList(projectId);
                if(CollectionUtils.isEmpty(energyConsumeTypeServiceList)){
                    result= Result.empty(result);
                }else{
                    List<EntityDto> entityDtoList = new ArrayList<>();
                    for (EnergyConsumeType energyConsumeType : energyConsumeTypeServiceList) {
                        entityDtoList.add(new EntityDto(energyConsumeType.getId(),energyConsumeType.getName(),energyConsumeType.getProjectId()));
                    }
                    result.setData(entityDtoList);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/manager/consume/type/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result< List<EntityDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 能耗类型列表
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/consume/type/save", produces = CommonUtils.MediaTypeJSON)
    public String consumeTypeSave(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramMap.get("projectId");
            Object energyConsumeNameObj = paramMap.get("consumeTypeName");
            if (projectObj==null || energyConsumeNameObj==null) {
                result=Result.paramError(result);
            }else{
                Long projectId =Long.parseLong(projectObj.toString());
                String energyConsumeName=energyConsumeNameObj.toString();
                EnergyConsumeType energyConsumeTypeOld =energyConsumeTypeService.findByName(projectId,energyConsumeName);
                if(energyConsumeTypeOld!=null){
                    result.setMsg("名称已存在，请修改后重试");
                }else{
                    EnergyConsumeType energyConsumeType = new EnergyConsumeType();
                    energyConsumeType.setName(energyConsumeName);
                    energyConsumeType.setProjectId(projectId);
                    energyConsumeType.setSystemId(3000L);
                    Date date = new Date();
                    energyConsumeType.setUpdateTime(date);
                    energyConsumeType.setCreateTime(date);
                    energyConsumeTypeService.save(energyConsumeType);
                    result.setCode(1);
                    result.setMsg("success");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/manager/consume/type/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 修改能耗类型名称
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/consume/type/update", produces = CommonUtils.MediaTypeJSON)
    public String updateConsumeType(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object energyConsumeObj = paramMap.get("consumeTypeId");
            Object energyConsumeNameObj = paramMap.get("consumeTypeName");
            Object projectIdObj = paramMap.get("projectId");
            if (energyConsumeObj==null || energyConsumeNameObj==null||projectIdObj==null) {
                result=Result.paramError(result);
            }else{
                Long energyConsumeId=Long.parseLong(energyConsumeObj.toString());
                Long projectId=Long.parseLong(projectIdObj.toString());
                String  energyConsumeName=energyConsumeNameObj.toString();
                EnergyConsumeType energyConsumeTypeOld = energyConsumeTypeService.findById(energyConsumeId);
                if(energyConsumeName.equals(energyConsumeTypeOld.getName())){
                    result.setMsg("名称未改变，请修改后重试");
                }else{
                    EnergyConsumeType byName = energyConsumeTypeService.findByName(projectId, energyConsumeName);
                    if(byName!=null){
                        result.setMsg("名称已存在，请修改后重试");
                    }else{
                        EnergyConsumeType energyConsumeType = new EnergyConsumeType();
                        energyConsumeType.setId(energyConsumeId);
                        energyConsumeType.setName(energyConsumeName);
                        energyConsumeType.setUpdateTime(new Date());
                        energyConsumeTypeService.update(energyConsumeType);
                        result.setCode(1);
                        result.setMsg("success");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/manager/consume/type/update"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 删除能耗类型
     * @param paramMap
     * @return
     */
    @Log("删除能耗类型")
    @PostMapping(value = "/consume/type/delete", produces = CommonUtils.MediaTypeJSON)
    public String removeConsumeType(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object energyConsumeObj = paramMap.get("consumeTypeId");
            Object projectIdObj = paramMap.get("projectId");
            if(energyConsumeObj==null||projectIdObj==null){
                result= Result.paramError(result);
            }else{
                Long consumeTypeId=Long.parseLong(energyConsumeObj.toString());
                Long projectId=Long.parseLong(projectIdObj.toString());
                List<EnergyEquipment> byEnergyConsumeTypeId = energyEquipmentService.getByEnergyConsumeTypeId(projectId, consumeTypeId);
                if(CollectionUtils.isEmpty(byEnergyConsumeTypeId)){
                    energyConsumeTypeService.delete(consumeTypeId);
                    result.setCode(1);
                    result.setData(1);
                }else{
                    result.setMsg("删除失败，该类型还有设备未被移除");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/manager/consume/type/delete"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
    /**
     * 获取能源设备信息 根据能源设备类型
     * @param paramData
     * @return
     */
    @PostMapping(value = "/equipment/energyType/list", produces = CommonUtils.MediaTypeJSON)
    public String getEquipmentList(@RequestBody Map<String,Object> paramData) {
        Result<List<EntityDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramData.get("projectId");
            Object energyTypeIdObj = paramData.get("energyTypeId");
            if(projectIdObj==null||energyTypeIdObj==null){
                result= Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                Long energyTypeId=Long.parseLong(energyTypeIdObj.toString());
                List<EnergyEquipment> list=energyEquipmentService.getByEnergyTypeId(projectId,energyTypeId);
                if(CollectionUtils.isEmpty(list)){
                    result =Result.empty(result);
                }else{
                    List<EntityDto> dataList=new ArrayList<>();
                    for (EnergyEquipment energyEquipment : list) {
                        dataList.add(new EntityDto(energyEquipment.getId(),energyEquipment.getName(),energyEquipment.getProjectId()));
                    }
                    result.setCode(1);
                    result.setData(dataList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/manager/equipment/energyType/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<EntityDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 添加能源设备
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/equipment/save", produces = CommonUtils.MediaTypeJSON)
    public String saveEquipment(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result = new Result<>();
        result.setCode(0);
        Boolean flag = false;
        Boolean bindingDeviceFlag = false;
        try {
            EnergyEquipment equipment = new EnergyEquipment();
            String paramFlag = checkEquipment(paramMap, equipment);
            if (!StringUtils.isBlank(paramFlag)) {
                result.setCode(-1);
                result.setMsg(paramFlag);
            } else {
                Object deviceIdObj = paramMap.get("deviceId");
                Object deviceTypeIdObj = paramMap.get("deviceTypeId");
                if (deviceIdObj != null && deviceTypeIdObj != null && !deviceIdObj.toString().equals("0") && !deviceTypeIdObj.toString().equals("0")) {
                    bindingDeviceFlag = true;
                    Device device = deviceService.findById(Long.parseLong(deviceIdObj.toString()));
                    if (!device.getBindingStatus().equals(0)) {
                        flag = true;
                        result.setCode(-1);
                        result.setMsg(paramFlag);
                    }
                }
                String name = paramMap.get("name").toString();
                Long  projectId = Long.parseLong(paramMap.get("projectId").toString());
                EnergyEquipment energyEquipment=energyEquipmentService.findByName(name,projectId);
                if(energyEquipment!=null){
                    result.setMsg("名称信息已存在");
                    Type type=new TypeToken<Result<Integer>>(){}.getType();
                    return JsonUtils.toJson(result,type);
                }
                if (!flag) {
                    Boolean msg = energyEquipmentService.saveEnergyDevice(equipment, paramMap, bindingDeviceFlag);
                    if (msg) {
                        result.setCode(1);
                        result.setMsg("success");
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/manager/equipment/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @PostMapping(value ="/equipment/info",produces = CommonUtils.MediaTypeJSON)
    public String equipmentInfo(@RequestBody Map<String,Object> paramMap){
       Result<EquipmentInfo> result=new Result<>();
       result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object equipmentIdObj = paramMap.get("equipmentId");
            if (projectIdObj==null||equipmentIdObj==null){
               result= Result.paramError(result);
            }else{
                Long projectId= Long.parseLong(projectIdObj.toString());
                Long equipmentId= Long.parseLong(equipmentIdObj.toString());
                EquipmentInfo equipmentInfo=energyEquipmentService.getEquipmentManagerInfo(projectId,equipmentId);
                if(equipmentInfo!=null){
                    result.setCode(1);
                    result.setData(equipmentInfo);
                }else{
                    result=Result.empty(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/manager/equipment/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<EquipmentInfo>>(){}.getType();
       return JsonUtils.toJson(result,type);
    }

    @PostMapping(value ="/equipment/variableParam",produces = CommonUtils.MediaTypeJSON)
    public String equipmentParam(@RequestBody Map<String,Object> paramMap){
        Result<EquipmentVariable> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object equipmentIdObj = paramMap.get("equipmentId");
            if (projectIdObj==null||equipmentIdObj==null){
                result= Result.paramError(result);
            }else{
                Long projectId= Long.parseLong(projectIdObj.toString());
                Long equipmentId= Long.parseLong(equipmentIdObj.toString());
                EquipmentVariable equipmentVariable=energyEquipmentService.getVariableParam(projectId,equipmentId);
                if(equipmentVariable!=null){
                    result.setCode(1);
                    result.setData(equipmentVariable);
                }else{
                    result=Result.empty(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/manager/equipment/variableParam"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<EquipmentVariable>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    @PostMapping(value ="/equipment/update",produces = CommonUtils.MediaTypeJSON)
    public String equipmentUpdate(@RequestBody EquipmentVariable equipmentVariable){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Long equipmentId = equipmentVariable.getId();
            Long projectId = equipmentVariable.getProjectId();
            if (equipmentId==null || projectId==null ){
                result= Result.paramError(result);
            }else{
                String msg=energyEquipmentService.updateParam(equipmentVariable);
                if(StringUtils.isBlank(msg)){
                    result.setCode(1);
                    result.setData(1);
                }else{
                    result.setMsg(msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/manager/equipment/update"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @PostMapping(value ="/equipment/remove",produces = CommonUtils.MediaTypeJSON)
    public String equipmentRemove(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object equipmentIdObj = paramMap.get("equipmentId");
            if (equipmentIdObj==null ){
                result= Result.paramError(result);
            }else{
                Long equipmentId =  Long.parseLong(equipmentIdObj.toString());
                EnergyEquipmentDevice energyEquipmentDevice = energyEquipmentDeviceService.selectByEquipmentId(equipmentId);
                if(energyEquipmentDevice!=null){
                    Long deviceId = energyEquipmentDevice.getDeviceId();
                    Device device=new Device();
                    device.setId(deviceId);
                    device.setUpdateTime(new Date());
                    device.setBindingStatus(0);
                    device.setBindingType(-1);
                    deviceService.update(device);
                }
                energyEquipmentService.delete(equipmentId);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/energy/manager/equipment/remove"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }



    /**
     * 添加能源设备检验参数
     * @param paramMap
     * @return
     */
    private String checkEquipment(Map<String, Object> paramMap,EnergyEquipment energyEquipment){
        List<String> errorInfoList=new ArrayList<>();
        Object brandObj = paramMap.get("brand");
        Object nameObj = paramMap.get("name");
        Object productionDateObj = paramMap.get("productionDate");
        Object ratedPowerObj = paramMap.get("ratedPower");
        Object energyTypeIdObj = paramMap.get("energyTypeId");
        Object consumeTypeIdObj = paramMap.get("consumeTypeId");
        Object projectIdObj = paramMap.get("projectId");
        Object companyIdObj = paramMap.get("companyId");
        energyEquipment.setSystemId(3000l);
        if(brandObj==null|| StringUtils.isBlank(brandObj.toString())){
            errorInfoList.add("品牌参数信息错误");
        }else{
            energyEquipment.setBrand(brandObj.toString());
        }

        if(nameObj==null|| StringUtils.isBlank(nameObj.toString())){
            errorInfoList.add("名称参数信息错误");
        }else{
            energyEquipment.setName(nameObj.toString());
        }
        if(productionDateObj==null|| StringUtils.isBlank(productionDateObj.toString())){
            errorInfoList.add("生产日期参数信息错误");
        }else{
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            try {
                energyEquipment.setProductionDate(simpleDateFormat.parse(productionDateObj.toString()));
            } catch (Exception e) {
                errorInfoList.add("生产日期参数格式信息错误 正确的格式:2020-09-31");
            }
        }
        if(ratedPowerObj==null||StringUtils.isBlank(ratedPowerObj.toString())){
            errorInfoList.add("额定功率参数信息错误");
        }else{
            energyEquipment.setRatedPower(ratedPowerObj.toString());
        }
        if(energyTypeIdObj==null||StringUtils.isBlank(energyTypeIdObj.toString())){
            errorInfoList.add("能源设备类型参数信息错误");
        }else{
            energyEquipment.setEnergyTypeId(Long.parseLong(energyTypeIdObj.toString()));
        }
        if(consumeTypeIdObj==null||StringUtils.isBlank(consumeTypeIdObj.toString())){
            errorInfoList.add("能耗设备类型参数信息错误");
        }else{
            energyEquipment.setConsumeTypeId(Long.parseLong(consumeTypeIdObj.toString()));
        }
        if(projectIdObj==null||StringUtils.isBlank(projectIdObj.toString())){
            errorInfoList.add("项目Id参数信息错误");
        }else{
            energyEquipment.setProjectId(Long.parseLong(projectIdObj.toString()));
        }
        if(companyIdObj==null||StringUtils.isBlank(companyIdObj.toString())){
            errorInfoList.add("使用公司参数信息错误");
        }else{
            if(companyIdObj.toString().equals("0")){
                energyEquipment.setCompanyId(null);
            }else{
                energyEquipment.setCompanyId(Long.parseLong(companyIdObj.toString()));
            }
        }
        Object locationFlagObj = paramMap.get("locationFlag");
        if(locationFlagObj==null||StringUtils.isBlank(locationFlagObj.toString())){
            errorInfoList.add("自定义位置标志参数信息错误");
        }else{
            String locationFlag = locationFlagObj.toString();
            if(locationFlag.equals("0")){
                Object areaIdObj = paramMap.get("areaId");
                Object buildingIdObj = paramMap.get("buildingId");
                Object storeyIdObj = paramMap.get("storeyId");
                Object roomIdObj = paramMap.get("roomId");
                if(areaIdObj==null||StringUtils.isBlank(areaIdObj.toString())){
                    errorInfoList.add("区域参数信息错误");
                }else{
                    if(!areaIdObj.toString().equals("0")){
                        energyEquipment.setAreaId(Long.parseLong(areaIdObj.toString()));
                    }else{
                        energyEquipment.setAreaId(null);
                    }
                }
                if(buildingIdObj==null||StringUtils.isBlank(buildingIdObj.toString())){
                    errorInfoList.add("楼参数信息错误");
                }else{
                    energyEquipment.setBuildingId(Long.parseLong(buildingIdObj.toString()));
                }
                if(storeyIdObj==null||StringUtils.isBlank(storeyIdObj.toString())){
                    errorInfoList.add("层参数信息错误");
                }else{
                    energyEquipment.setStoreyId(Long.parseLong(storeyIdObj.toString()));
                }
                if(roomIdObj==null||StringUtils.isBlank(roomIdObj.toString())){
                    errorInfoList.add("房间参数信息错误");
                }else{
                    energyEquipment.setRoomId(Long.parseLong(roomIdObj.toString()));
                }
            }else{
                Object locationObj = paramMap.get("location");
                if(locationObj==null||StringUtils.isBlank(locationObj.toString())){
                    errorInfoList.add("自定义区域信息参数信息错误");
                }else{
                    energyEquipment.setLocation(locationObj.toString());
                    energyEquipment.setAreaId(null);
                    energyEquipment.setBuildingId(null);
                    energyEquipment.setStoreyId(null);
                    energyEquipment.setRoomId(null);
                }
            }
        }
        if(!CollectionUtils.isEmpty(errorInfoList)){
            String join = StringUtils.join(errorInfoList, ",");
            return join;
        }else{
            return  null;
        }
    }

}
