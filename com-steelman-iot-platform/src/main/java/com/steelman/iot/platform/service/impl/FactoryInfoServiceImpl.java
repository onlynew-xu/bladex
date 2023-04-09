package com.steelman.iot.platform.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.steelman.iot.platform.dao.FactoryInfoDao;
import com.steelman.iot.platform.entity.FactoryInfo;
import com.steelman.iot.platform.entity.FactoryProcessUnit;
import com.steelman.iot.platform.entity.FactoryUploadConfigure;
import com.steelman.iot.platform.service.FactoryInfoService;
import com.steelman.iot.platform.service.FactoryProcessUnitService;
import com.steelman.iot.platform.service.FactoryUploadConfigureService;
import com.steelman.iot.platform.utils.HttpWebUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service("factoryInfoService")
public class FactoryInfoServiceImpl extends BaseService implements FactoryInfoService {

    @Resource
    private FactoryInfoDao factoryInfoDao;
    @Resource
    private FactoryUploadConfigureService factoryUploadConfigureService;
    @Resource
    private FactoryProcessUnitService factoryProcessUnitService;


    @Override
    public Integer registerCityFactoryInfo(Long projectId) {
        FactoryInfo factoryInfo=  factoryInfoDao.findByProjectId(projectId);
        Integer result=0;
        Long factoryInfoId = factoryInfo.getId();
        FactoryUploadConfigure  factoryUploadConfigure= factoryUploadConfigureService.getByFactoryId(factoryInfoId);
        String registerUrl = factoryUploadConfigure.getRegisterUrl();
        String enterpriseCode = factoryUploadConfigure.getEnterpriseCode();
        String region = factoryUploadConfigure.getRegion();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("enterpriseCode",enterpriseCode);
        jsonObject.put("region",region);
        try {
            String post = HttpWebUtils.post(registerUrl, jsonObject.toJSONString());
            JSONObject jsonObjectResult = JSONObject.parseObject(post);
            if(jsonObjectResult!=null){
                String responseMessage = jsonObjectResult.getString("responseMessage");
                if("RECEIVE SUCCESS".equals(responseMessage)){
                    FactoryUploadConfigure factoryUploadUpdate=new FactoryUploadConfigure();
                    String deviceId = jsonObjectResult.getString("deviceId");
                    String centerInfoURL = jsonObjectResult.getString("centerInfoURL");
                    String centerDataURL = jsonObjectResult.getString("centerDataURL");
                    String centerInfoDownloadURL = jsonObjectResult.getString("centerInfoDownloadURL");
                    String centerDataDownloadURL = jsonObjectResult.getString("centerDataDownloadURL");
                    factoryUploadUpdate.setDeviceId(deviceId);
                    factoryUploadUpdate.setId(factoryUploadConfigure.getId());
                    factoryUploadUpdate.setCenterInfoUrl(centerInfoURL);
                    factoryUploadUpdate.setCenterDataUrl(centerDataURL);
                    factoryUploadUpdate.setCenterInfoDownloadUrl(centerInfoDownloadURL);
                    factoryUploadUpdate.setCenterDataDownloadUrl(centerDataDownloadURL);
                    factoryUploadUpdate.setUpdateTime(new Date());
                    factoryUploadConfigureService.updateFactoryUpload(factoryUploadUpdate);
                    result=1;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            result=-1;
        }
        return result;
    }

    @Override
    public Integer configureFactoryInfo(Long projectId) {
        FactoryInfo factoryInfo=  factoryInfoDao.findByProjectId(projectId);
        Integer result=0;
        Long factoryInfoId = factoryInfo.getId();
        FactoryUploadConfigure  factoryUploadConfigure= factoryUploadConfigureService.getByFactoryId(factoryInfoId);
        FactoryProcessUnit factoryProcessUnit=factoryProcessUnitService.getByFactoryId(factoryInfoId);
        String enterpriseCode = factoryUploadConfigure.getEnterpriseCode();
        String centerInfoUrl = factoryUploadConfigure.getCenterInfoUrl();
        String deviceId = factoryUploadConfigure.getDeviceId();
        JSONObject dataParam=new JSONObject();
        dataParam.put("deviceId",deviceId);
        dataParam.put("enterpriseCode",enterpriseCode);
        JSONArray collectItemConfig=new JSONArray();

        JSONObject collectItemConfigItemHour=new JSONObject();
        collectItemConfigItemHour.put("name",factoryProcessUnit.getName());
        collectItemConfigItemHour.put("processCode",factoryProcessUnit.getProcessCode());
        collectItemConfigItemHour.put("processUnitCode",factoryProcessUnit.getProcessUnitCode());
        collectItemConfigItemHour.put("equipmentCode",factoryProcessUnit.getEquipmentCode());
        collectItemConfigItemHour.put("equipmentUnitCode",factoryProcessUnit.getEquipmentUnitCode());
        collectItemConfigItemHour.put("energyClassCode",factoryProcessUnit.getEnergyClassCode());
        collectItemConfigItemHour.put("energyTypeCode",factoryProcessUnit.getEnergyTypeCode());
        collectItemConfigItemHour.put("dataUsageCode",factoryProcessUnit.getDataUsageCode());
        collectItemConfigItemHour.put("inputType",factoryProcessUnit.getInputType());
        collectItemConfigItemHour.put("dataValueMax",factoryProcessUnit.getDataValueMax());
        collectItemConfigItemHour.put("dataValueMin",factoryProcessUnit.getDataValueMin());
        collectItemConfigItemHour.put("scope",factoryProcessUnit.getScope());
        collectItemConfigItemHour.put("statType","0");

        JSONObject collectItemConfigItemDay=new JSONObject();
        collectItemConfigItemDay.put("name",factoryProcessUnit.getName());
        collectItemConfigItemDay.put("processCode",factoryProcessUnit.getProcessCode());
        collectItemConfigItemDay.put("processUnitCode",factoryProcessUnit.getProcessUnitCode());
        collectItemConfigItemDay.put("equipmentCode",factoryProcessUnit.getEquipmentCode());
        collectItemConfigItemDay.put("equipmentUnitCode",factoryProcessUnit.getEquipmentUnitCode());
        collectItemConfigItemDay.put("energyClassCode",factoryProcessUnit.getEnergyClassCode());
        collectItemConfigItemDay.put("energyTypeCode",factoryProcessUnit.getEnergyTypeCode());
        collectItemConfigItemDay.put("dataUsageCode",factoryProcessUnit.getDataUsageCode());
        collectItemConfigItemDay.put("inputType",factoryProcessUnit.getInputType());
        collectItemConfigItemDay.put("dataValueMax",factoryProcessUnit.getDataValueMax());
        collectItemConfigItemDay.put("dataValueMin",factoryProcessUnit.getDataValueMin());
        collectItemConfigItemDay.put("scope",factoryProcessUnit.getScope());
        collectItemConfigItemDay.put("statType","1");

        JSONObject collectItemConfigItemMonth=new JSONObject();
        collectItemConfigItemMonth.put("name",factoryProcessUnit.getName());
        collectItemConfigItemMonth.put("processCode",factoryProcessUnit.getProcessCode());
        collectItemConfigItemMonth.put("processUnitCode",factoryProcessUnit.getProcessUnitCode());
        collectItemConfigItemMonth.put("equipmentCode",factoryProcessUnit.getEquipmentCode());
        collectItemConfigItemMonth.put("equipmentUnitCode",factoryProcessUnit.getEquipmentUnitCode());
        collectItemConfigItemMonth.put("energyClassCode",factoryProcessUnit.getEnergyClassCode());
        collectItemConfigItemMonth.put("energyTypeCode",factoryProcessUnit.getEnergyTypeCode());
        collectItemConfigItemMonth.put("dataUsageCode",factoryProcessUnit.getDataUsageCode());
        collectItemConfigItemMonth.put("inputType",factoryProcessUnit.getInputType());
        collectItemConfigItemMonth.put("dataValueMax",factoryProcessUnit.getDataValueMax());
        collectItemConfigItemMonth.put("dataValueMin",factoryProcessUnit.getDataValueMin());
        collectItemConfigItemMonth.put("scope",factoryProcessUnit.getScope());
        collectItemConfigItemMonth.put("statType","2");

        JSONObject collectItemConfigItemYear=new JSONObject();
        collectItemConfigItemYear.put("name",factoryProcessUnit.getName());
        collectItemConfigItemYear.put("processCode",factoryProcessUnit.getProcessCode());
        collectItemConfigItemYear.put("processUnitCode",factoryProcessUnit.getProcessUnitCode());
        collectItemConfigItemYear.put("equipmentCode",factoryProcessUnit.getEquipmentCode());
        collectItemConfigItemYear.put("equipmentUnitCode",factoryProcessUnit.getEquipmentUnitCode());
        collectItemConfigItemYear.put("energyClassCode",factoryProcessUnit.getEnergyClassCode());
        collectItemConfigItemYear.put("energyTypeCode",factoryProcessUnit.getEnergyTypeCode());
        collectItemConfigItemYear.put("dataUsageCode",factoryProcessUnit.getDataUsageCode());
        collectItemConfigItemYear.put("inputType",factoryProcessUnit.getInputType());
        collectItemConfigItemYear.put("dataValueMax",factoryProcessUnit.getDataValueMax());
        collectItemConfigItemYear.put("dataValueMin",factoryProcessUnit.getDataValueMin());
        collectItemConfigItemYear.put("scope",factoryProcessUnit.getScope());
        collectItemConfigItemYear.put("statType","3");

        collectItemConfig.add(collectItemConfigItemHour);
        collectItemConfig.add(collectItemConfigItemDay);
        collectItemConfig.add(collectItemConfigItemMonth);
        collectItemConfig.add(collectItemConfigItemYear);
        dataParam.put("collectItemConfig",collectItemConfig);
        JSONObject enterprise=new JSONObject();
        enterprise.put("code",factoryInfo.getCode());
        enterprise.put("name",factoryInfo.getName());
        enterprise.put("typeCode",factoryInfo.getTypeCode());
        enterprise.put("typeName",factoryInfo.getTypeName());
        enterprise.put("industryCode",factoryInfo.getIndustryCode());
        enterprise.put("regionCode",factoryInfo.getRegionCode());
        enterprise.put("regionName",factoryInfo.getRegionName());
        enterprise.put("center",factoryInfo.getCenter());
        enterprise.put("corporationCode",factoryInfo.getCorporationCode());
        enterprise.put("jgzh",factoryInfo.getJgzh());
        enterprise.put("energyConsumeLevel",factoryInfo.getEnergyConsumeLevel());
        enterprise.put("latitude",factoryInfo.getLatitude());
        enterprise.put("longitude",factoryInfo.getLongitude());
        enterprise.put("phone",factoryInfo.getPhone());
        dataParam.put("enterprise",enterprise);
        try {
            String post = HttpWebUtils.post(centerInfoUrl, dataParam.toJSONString());
            JSONObject jsonObject=JSONObject.parseObject(post);
            String responseMessage = jsonObject.getString("responseMessage");
            if(StringUtils.isNotBlank(responseMessage)&&"RECEIVE SUCCESS".equals(responseMessage)){
                result=1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result=-1;
        }
        return result;
    }


    @Override
    public FactoryInfo findByProjectId(Long projectId) {
        return  factoryInfoDao.findByProjectId(projectId);
    }
}
