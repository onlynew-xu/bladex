package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.EnergyEquipmentDao;
import com.steelman.iot.platform.dao.EnergyEquipmentFourthDao;
import com.steelman.iot.platform.dao.EnergyEquipmentSecondDao;
import com.steelman.iot.platform.dao.EnergyEquipmentThirdDao;
import com.steelman.iot.platform.entity.EnergyConsumeType;
import com.steelman.iot.platform.entity.dto.MeasureData;
import com.steelman.iot.platform.entity.vo.EnergyConsumeStatistic;
import com.steelman.iot.platform.entity.vo.EnergyTotalSimpleInfo;
import com.steelman.iot.platform.largescreen.vo.EnergyStatus;
import com.steelman.iot.platform.largescreen.vo.EnergyTotal;
import com.steelman.iot.platform.largescreen.vo.EquipmentStatistic;
import com.steelman.iot.platform.service.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

@Service("energyPingWebService")
public class EnergyPingWebServiceImpl extends BaseService implements EnergyPingWebService {

    @Resource
    private EnergyEquipmentService energyEquipmentService;

    @Resource
    private EnergyEquipmentSecondService energyEquipmentSecondService;

    @Resource
    private EnergyEquipmentThirdService energyEquipmentThirdService;

    @Resource
    private EnergyEquipmentFourthService energyEquipmentFourthService;
    @Resource
    private EnergyEquipmentDao energyEquipmentDao;
    @Resource
    private EnergyEquipmentSecondDao energyEquipmentSecondDao;
    @Resource
    private EnergyEquipmentThirdDao energyEquipmentThirdDao;
    @Resource
    private EnergyEquipmentFourthDao energyEquipmentFourthDao;
    @Resource
    private EnergyConsumeTypeService energyConsumeTypeService;

    @Override
    public EquipmentStatistic getEquipmentStatusStatistic(Long projectId) {
        List<EnergyStatus> finalData=new ArrayList<>();
        List<EnergyStatus> firstData= energyEquipmentService.getEnergyStatistic(projectId);
        List<EnergyStatus> secondData= energyEquipmentSecondService.getEnergyStatistic(projectId);
        List<EnergyStatus> thirdData= energyEquipmentThirdService.getEnergyStatistic(projectId);
        List<EnergyStatus> fourData= energyEquipmentFourthService.getEnergyStatistic(projectId);
        if(!CollectionUtils.isEmpty(firstData)){
            finalData.addAll(firstData);
        }
        if(!CollectionUtils.isEmpty(secondData)){
            finalData.addAll(secondData);
        }
        if(!CollectionUtils.isEmpty(thirdData)){
            finalData.addAll(thirdData);
        }
        if(!CollectionUtils.isEmpty(fourData)){
            finalData.addAll(fourData);
        }
        if(!CollectionUtils.isEmpty(finalData)){
            Integer total=0;
            Integer inline=0;
            Integer outline=0;
            for (EnergyStatus finalDatum : finalData) {
                Integer status = finalDatum.getStatus();
                if(status.equals(2)||status.equals(4)){
                    outline++;
                }else{
                    inline++;
                }
                total++;
            }
            EquipmentStatistic equipmentStatistic=new EquipmentStatistic();
            equipmentStatistic.setInline(inline);
            equipmentStatistic.setOutline(outline);
            equipmentStatistic.setTotal(total);
            if(outline.equals(0)){
                equipmentStatistic.setPercent("100");
            }else if(outline.equals(total)){
                equipmentStatistic.setPercent("0");
            }else{
                inline=inline*100;
                Integer pe=inline/total;
                equipmentStatistic.setPercent(String.valueOf(pe));
            }
            return equipmentStatistic;
        }
        return null;
    }

    @Override
    public EnergyTotal getTotalEnergy(Long projectId) {
        MeasureData totalMeasurement = energyEquipmentService.getTotalMeasurement(projectId);
        if(totalMeasurement!=null){
            Float total = totalMeasurement.getTotal();
            EnergyTotal energyTotal=new EnergyTotal(String.valueOf(total),"0","0");
            return energyTotal;
        }
        return null;
    }

    @Override
    public List<EnergyConsumeStatistic> getEquipmentConsumeType(Long projectId) {
        List<Map<String, Object>> firstDataList = energyEquipmentDao.consumeStatistic(projectId);
        List<Map<String, Object>> secondDataList = energyEquipmentSecondDao.consumeStatistic(projectId);
        List<Map<String, Object>> thirdDataList = energyEquipmentThirdDao.consumeStatistic(projectId);
        List<Map<String, Object>> fourthDataList = energyEquipmentFourthDao.consumeStatistic(projectId);
        Map<Long,Integer> dataMap=new LinkedHashMap<>();
        List<EnergyConsumeType> energyConsumeTypes=energyConsumeTypeService.getList(projectId);
        Map<Long,String> consumeTypeMap=new HashMap<>();
        if(!CollectionUtils.isEmpty(energyConsumeTypes)){
            for (EnergyConsumeType energyConsumeType : energyConsumeTypes) {
                consumeTypeMap.put(energyConsumeType.getId(),energyConsumeType.getName());
            }
        }
        if(!CollectionUtils.isEmpty(firstDataList)){
            for (Map<String, Object> stringObjectMap : firstDataList) {
                Long typeId = Long.parseLong(stringObjectMap.get("typeId").toString());
                Integer num = Integer.parseInt(stringObjectMap.get("num").toString());
                dataMap.put(typeId,num);
            }
        }
        if(!CollectionUtils.isEmpty(secondDataList)){
            for (Map<String, Object> stringObjectMap : secondDataList) {
                Long typeId=Long.parseLong(stringObjectMap.get("typeId").toString());
                Integer num = Integer.parseInt(stringObjectMap.get("num").toString());
                if(dataMap.containsKey(typeId)){
                    Integer integer = dataMap.get(typeId);
                    integer=integer+num;
                    dataMap.put(typeId,integer);
                }else{
                    dataMap.put(typeId,num);
                }
            }
        }
        if(!CollectionUtils.isEmpty(thirdDataList)){
            for (Map<String, Object> stringObjectMap : thirdDataList) {
                Long typeId=Long.parseLong(stringObjectMap.get("typeId").toString());
                Integer num = Integer.parseInt(stringObjectMap.get("num").toString());
                if(dataMap.containsKey(typeId)){
                    Integer integer = dataMap.get(typeId);
                    integer=integer+num;
                    dataMap.put(typeId,integer);
                }else{
                    dataMap.put(typeId,num);
                }
            }
        }
        if(!CollectionUtils.isEmpty(fourthDataList)){
            for (Map<String, Object> stringObjectMap : fourthDataList) {
                Long typeId=Long.parseLong(stringObjectMap.get("typeId").toString());
                Integer num = Integer.parseInt(stringObjectMap.get("num").toString());
                if(dataMap.containsKey(typeId)){
                    Integer integer = dataMap.get(typeId);
                    integer=integer+num;
                    dataMap.put(typeId,integer);
                }else{
                    dataMap.put(typeId,num);
                }
            }
        }
        if(!CollectionUtils.isEmpty(dataMap)){
            List<EnergyConsumeStatistic> energyConsumeStatistics=new ArrayList<>();
            for (Map.Entry<Long, Integer> longIntegerEntry : dataMap.entrySet()) {
                Long key = longIntegerEntry.getKey();
                Integer value = longIntegerEntry.getValue();
                energyConsumeStatistics.add(new EnergyConsumeStatistic(consumeTypeMap.get(key),value));
            }
            return energyConsumeStatistics;
        }
        return null;
    }


    @Override
    public List<EnergyTotalSimpleInfo> getMeasureRank(Long projectId) {
        List<Map<String,Object>>dataFirstMap=energyEquipmentService.getEquipmentTotal(projectId);
        List<Map<String,Object>>dataSecondMap =energyEquipmentSecondService.getEquipmentTotal(projectId);
        List<Map<String,Object>>dataThirdMap=energyEquipmentThirdService.getEquipmentTotal(projectId);
        List<Map<String,Object>>dataFourMap=energyEquipmentFourthService.getEquipmentTotal(projectId);
        List<EnergyTotalSimpleInfo> energyTotalSimpleInfos=new ArrayList<>();
        //处理一级能耗的数据
        if(!CollectionUtils.isEmpty(dataFirstMap)){
            for (Map<String, Object> stringObjectMap : dataFirstMap) {
                Long equipmentId = Long.parseLong(stringObjectMap.get("equipmentId").toString());
                String equipmentName = stringObjectMap.get("equipmentName").toString();
                String totalTotal = stringObjectMap.get("totalTotal").toString();
                EnergyTotalSimpleInfo energyTotalSimpleInfo=new EnergyTotalSimpleInfo();
                energyTotalSimpleInfo.setEquipmentId(equipmentId);
                energyTotalSimpleInfo.setEquipmentName(equipmentName);
                energyTotalSimpleInfo.setLevel(1);
                energyTotalSimpleInfo.setOrderValue(Long.parseLong(totalTotal));
                energyTotalSimpleInfo.setTotal(totalTotal);
                energyTotalSimpleInfos.add(energyTotalSimpleInfo);
            }
        }
        //处理二级能耗的数据
        if(!CollectionUtils.isEmpty(dataSecondMap)){
            for (Map<String, Object> stringObjectMap : dataSecondMap) {
                Long equipmentId = Long.parseLong(stringObjectMap.get("equipmentId").toString());
                String equipmentName = stringObjectMap.get("equipmentName").toString();
                String totalTotal = stringObjectMap.get("totalTotal").toString();
                EnergyTotalSimpleInfo energyTotalSimpleInfo=new EnergyTotalSimpleInfo();
                energyTotalSimpleInfo.setEquipmentId(equipmentId);
                energyTotalSimpleInfo.setEquipmentName(equipmentName);
                energyTotalSimpleInfo.setLevel(2);
                energyTotalSimpleInfo.setOrderValue(Long.parseLong(totalTotal));
                energyTotalSimpleInfo.setTotal(totalTotal);
                energyTotalSimpleInfos.add(energyTotalSimpleInfo);
            }
        }
        //处理三级能耗的数据
        if(!CollectionUtils.isEmpty(dataThirdMap)){
            for (Map<String, Object> stringObjectMap : dataThirdMap) {
                Long equipmentId = Long.parseLong(stringObjectMap.get("equipmentId").toString());
                String equipmentName = stringObjectMap.get("equipmentName").toString();
                String totalTotal = stringObjectMap.get("totalTotal").toString();
                EnergyTotalSimpleInfo energyTotalSimpleInfo=new EnergyTotalSimpleInfo();
                energyTotalSimpleInfo.setEquipmentId(equipmentId);
                energyTotalSimpleInfo.setEquipmentName(equipmentName);
                energyTotalSimpleInfo.setLevel(3);
                energyTotalSimpleInfo.setOrderValue(Long.parseLong(totalTotal));
                energyTotalSimpleInfo.setTotal(totalTotal);
                energyTotalSimpleInfos.add(energyTotalSimpleInfo);
            }
        }
        //处理四级能耗的数据
        if(!CollectionUtils.isEmpty(dataFourMap)){
            for (Map<String, Object> stringObjectMap : dataFourMap) {
                Long equipmentId = Long.parseLong(stringObjectMap.get("equipmentId").toString());
                String equipmentName = stringObjectMap.get("equipmentName").toString();
                String totalTotal = stringObjectMap.get("totalTotal").toString();
                EnergyTotalSimpleInfo energyTotalSimpleInfo=new EnergyTotalSimpleInfo();
                energyTotalSimpleInfo.setEquipmentId(equipmentId);
                energyTotalSimpleInfo.setEquipmentName(equipmentName);
                energyTotalSimpleInfo.setLevel(4);
                energyTotalSimpleInfo.setOrderValue(Long.parseLong(totalTotal));
                energyTotalSimpleInfo.setTotal(totalTotal);
                energyTotalSimpleInfos.add(energyTotalSimpleInfo);
            }
        }
        if(!CollectionUtils.isEmpty(energyTotalSimpleInfos)){
            energyTotalSimpleInfos.sort(new Comparator<EnergyTotalSimpleInfo>() {
                @Override
                public int compare(EnergyTotalSimpleInfo o1, EnergyTotalSimpleInfo o2) {
                    Long cha=o1.getOrderValue()- o2.getOrderValue();
                    if(cha>0){
                        return 1;
                    }else{
                        return -1;
                    }
                }
            });
        }
        return energyTotalSimpleInfos;
    }
}
