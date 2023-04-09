package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.EnergyEquipmentDeviceDao;
import com.steelman.iot.platform.entity.*;
import com.steelman.iot.platform.entity.vo.EnergyDeviceInfo;
import com.steelman.iot.platform.service.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Author lsj
 * @DATE 2021/3/31 0031 15:57
 * @Description:
 */
@Service("energyEquipmentDeviceService")
public class EnergyEquipmentDeviceServiceImpl extends BaseService implements EnergyEquipmentDeviceService {

    @Resource
    private EnergyEquipmentDeviceDao equipmentDeviceDao;
    @Resource
    private DeviceService deviceService;
    @Resource
    private RegionAreaService regionAreaService;
    @Resource
    private RegionBuildingService  regionBuildingService;
    @Resource
    private RegionStoreyService  regionStoreyService;
    @Resource
    private RegionRoomService  regionRoomService;

    @Override
    public void save(EnergyEquipmentDevice equipmentDevice) {
        equipmentDeviceDao.insert(equipmentDevice);
    }

    @Override
    public EnergyEquipmentDevice getInfo(Long equipmentId) {
        return equipmentDeviceDao.selectByEquipmentId(equipmentId);
    }

    @Override
    public void update(EnergyEquipmentDevice equipmentDevice) {
        equipmentDeviceDao.updateByPrimaryKeySelective(equipmentDevice);
    }

    @Override
    public List<EnergyDeviceInfo> deviceEnergyConsumeRank(Long projectId, Integer type) {
        LocalDate localDate = LocalDate.now();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        //当月或当年
        if (type == 1 || type == 3) {
            return equipmentDeviceDao.selectMonthYearConsumeRank(projectId, month, year, type);
        }
        int startMonth = localDate.getMonth().firstMonthOfQuarter().getValue();
        int endMonth = startMonth + 2;
        return equipmentDeviceDao.selectQuarterConsumeRank(projectId, year, startMonth, endMonth);
    }

    @Override
    public List<EnergyDeviceInfo> getCountCenter(Long projectId, Long consumeTypeId, Long energyTypeId, String beginTime, String endTime, Integer order, Integer type) {
        List<EnergyDeviceInfo> deviceInfoList = equipmentDeviceDao.selectCountCenter(projectId, consumeTypeId, energyTypeId, beginTime, endTime, order, type);
        return deviceInfoList;
    }

    @Override
    public List<EnergyDeviceInfo> countCenterReading(Long projectId, Long consumeTypeId, Long energyTypeId, Integer type, Integer order) {
        List<EnergyDeviceInfo> deviceInfos = equipmentDeviceDao.selectCountCenterReading(projectId, consumeTypeId, energyTypeId, type, order);
        return deviceInfos;
    }

    @Override
    public List<Map<String, Object>> selectDeviceCountMeasurement(Long equipmentId) {
        return equipmentDeviceDao.selectDeviceCountMeasurement(equipmentId);
    }

    @Override
    public List<Map<String, Object>> selectEnergyTrend(Long id, String[] dateStrArr) {
        List<Map<String, Object>> list = new ArrayList<>();

        List<String> totalList = new ArrayList<>();
        List<String> needleList = new ArrayList<>();
        List<String> peekList = new ArrayList<>();
        List<String> normalList = new ArrayList<>();
        List<String> valleyList = new ArrayList<>();


        for (String dataStr : dateStrArr) {
            String totalData = equipmentDeviceDao.selectDeviceMeasurementDay(id, dataStr, 1);
            totalList.add(totalData);
            String needleData = equipmentDeviceDao.selectDeviceMeasurementDay(id, dataStr, 2);
            needleList.add(needleData);
            String totalPeak = equipmentDeviceDao.selectDeviceMeasurementDay(id, dataStr, 3);
            peekList.add(totalPeak);
            String totalNormal = equipmentDeviceDao.selectDeviceMeasurementDay(id, dataStr, 4);
            normalList.add(totalNormal);
            String totalValley = equipmentDeviceDao.selectDeviceMeasurementDay(id, dataStr, 5);
            valleyList.add(totalValley);
        }
        Map<String, Object> total = new HashMap<>();
        total.put("type", 1);
        total.put("data", totalList);
        Map<String, Object> needle = new HashMap<>();
        needle.put("type", 2);
        needle.put("data", needleList);
        Map<String, Object> peek = new HashMap<>();
        peek.put("type", 3);
        peek.put("data", peekList);
        Map<String, Object> normal = new HashMap<>();
        normal.put("type", 4);
        normal.put("data", normalList);
        Map<String, Object> valley = new HashMap<>();
        valley.put("type", 5);
        valley.put("data", valleyList);
        list.add(total);
        list.add(needle);
        list.add(peek);
        list.add(normal);
        list.add(valley);
        return list;
    }

    @Override
    public EnergyEquipmentPower selectByDeviceId(Long deviceId) {
        return equipmentDeviceDao.selectByDeviceId(deviceId);
    }


    @Override
    public void deleteByDeviceId(Long deviceId) {
        Device device = new Device();
        device.setId(deviceId);
        device.setBindingType(-1);
        device.setBindingStatus(0);
        device.setUpdateTime(new Date());
        deviceService.update(device);
        equipmentDeviceDao.deleteByDeviceId(deviceId);
    }

    @Override
    public void removeDevice(Long deviceId) {
        equipmentDeviceDao.deleteByDeviceId(deviceId);
    }

    @Override
    public void removeByEquipmentId(Long equipmentId) {
        equipmentDeviceDao.deleteByEquipmentId(equipmentId);
    }

    @Override
    public EnergyEquipment findEnergyEquipment(Long id) {

        return equipmentDeviceDao.selectEnergyEquipmentByDeviceId(id);
    }

    @Override
    public EnergyEquipmentDevice selectByEquipmentId(Long id) {
        return equipmentDeviceDao.selectByEquipmentId(id);
    }

    @Override
    public List<EnergyDeviceInfo> deviceEnergyConsumePartRank(Long projectId, Integer typeId ,Object part) {
        if(typeId.equals(1)){
            LocalDate localDate= LocalDate.now();
            DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String format = dateTimeFormatter.format(localDate);
            List<Map<String,Object>> dataList=  equipmentDeviceDao.getMonthMeasure(projectId,format);
            List<EnergyDeviceInfo> energyDeviceInfoList=new ArrayList();
            if(!CollectionUtils.isEmpty(dataList)){
                BigDecimal bigDecimalThou=new BigDecimal(1000);
                for (Map<String, Object> stringObjectMap : dataList) {
                    Long id = Long.parseLong(stringObjectMap.get("id").toString());
                    String name = stringObjectMap.get("name").toString();
                    String energyName = stringObjectMap.get("energyName").toString();
                    String consumeName = stringObjectMap.get("consumeName").toString();
                    Integer monthConsume = Integer.parseInt(stringObjectMap.get("monthConsume").toString());
                    BigDecimal monthConsumeBig=new BigDecimal(monthConsume);
                    EnergyDeviceInfo energyDeviceInfo = new EnergyDeviceInfo(id, name, energyName, consumeName, monthConsumeBig.divide(bigDecimalThou, 3, BigDecimal.ROUND_HALF_UP).floatValue(), "");
                    energyDeviceInfoList.add(energyDeviceInfo);
                }
            }
            if(!CollectionUtils.isEmpty(energyDeviceInfoList)){
                energyDeviceInfoList.sort(Comparator.comparing(EnergyDeviceInfo::getTotalData).reversed());
                if(part!=null && "part".equals(part.toString())){
                    List<EnergyDeviceInfo> finalDate=new ArrayList<>();
                    if(energyDeviceInfoList.size()<10){
                        return energyDeviceInfoList;
                    }else{
                        for (int i = 0; i <10 ; i++) {
                            finalDate.add(energyDeviceInfoList.get(i));
                        }
                        return finalDate;
                    }
                }else{
                    return energyDeviceInfoList;
                }
            }
            return null;
        }else{
            List<EnergyDeviceInfo> energyDeviceInfoList=new ArrayList();
            LocalDate localDate=LocalDate.now();
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String format = formatters.format(localDate);
            int monthValue = localDate.getMonthValue();
            int year = localDate.getYear();
            int du= monthValue / 3 + 1;
            String dateYear="";
            if(typeId.equals(2)){
                if(du==1){
                    dateYear=(year-1)+"-12-31";
                }else if(du==2){
                    dateYear=year+"-03-31";
                }else if(du==3){
                    dateYear=year+"-06-30";
                }else if(du==4){
                    dateYear=year+"-09-30";
                }
            }else{
                LocalDate localDateYear = localDate.plusYears(-1).plusDays(-1);
                dateYear = formatters.format(localDateYear);
            }
            List<Map<String,Object>> measureToday=equipmentDeviceDao.getTodayMeasure(projectId,format);
            List<Map<String,Object>> measureJiDu=equipmentDeviceDao.getJiDuMeasure(projectId,dateYear);
            Map<Long,Integer> dataMapJiDu=new HashMap<>();
            if(!CollectionUtils.isEmpty(measureJiDu)){
                for (Map<String, Object> stringObjectMap : measureJiDu) {
                    Long id =Long.parseLong(stringObjectMap.get("id").toString());
                    Integer count = Integer.parseInt(stringObjectMap.get("total").toString());
                    dataMapJiDu.put(id,count);
                }
            }
            if(!CollectionUtils.isEmpty(measureToday)){
                BigDecimal bigDecimalThou=new BigDecimal(1000);
                for (Map<String, Object> stringObjectMap : measureToday) {
                    Long id = Long.parseLong(stringObjectMap.get("id").toString());
                    String name = stringObjectMap.get("name").toString();
                    String energyName = stringObjectMap.get("energyName").toString();
                    String consumeName = stringObjectMap.get("consumeName").toString();
                    Integer total = Integer.parseInt(stringObjectMap.get("total").toString());
                    if(dataMapJiDu.containsKey(id)){
                        Integer integer = dataMapJiDu.get(id);
                        total=total-integer;
                    }
                    BigDecimal monthConsumeBig=new BigDecimal(total);
                    EnergyDeviceInfo energyDeviceInfo = new EnergyDeviceInfo(id, name, energyName, consumeName, monthConsumeBig.divide(bigDecimalThou, 3, BigDecimal.ROUND_HALF_UP).floatValue(), "");
                    energyDeviceInfoList.add(energyDeviceInfo);
                }
            }
            if(!CollectionUtils.isEmpty(energyDeviceInfoList)){
                energyDeviceInfoList.sort(Comparator.comparing(EnergyDeviceInfo::getTotalData).reversed());
                if(part!=null){
                    List<EnergyDeviceInfo> finalDate=new ArrayList<>();
                    if(energyDeviceInfoList.size()<10){
                        return energyDeviceInfoList;
                    }else{
                        for (int i = 0; i <10 ; i++) {
                            finalDate.add(energyDeviceInfoList.get(i));
                        }
                        return finalDate;
                    }
                }else{
                    return energyDeviceInfoList;
                }
            }
            return null;

        }
    }

    @Override
    public Map<String, String[][]> getExportEnergyConsumeRank(Long projectId) {
        Map<String, String[][]> dataMap=new LinkedHashMap<>();
        List<Area> areaList = regionAreaService.selectByProjectId(projectId);
        List<Building> buildingList = regionBuildingService.selectByProjectId(projectId);
        List<Storey> storeyList = regionStoreyService.selectByProjectId(projectId);
        List<Room> roomList = regionRoomService.selectByProjectId(projectId);
        Map<Long,String> areaMap=new HashMap<>();
        if(!CollectionUtils.isEmpty(areaList)){
            for (Area area : areaList) {
                areaMap.put(area.getId(),area.getName());
            }
        }
        Map<Long,String> buildingMap=new HashMap<>();
        if(!CollectionUtils.isEmpty(buildingList)){
            for (Building building : buildingList) {
                buildingMap.put(building.getId(),building.getName());
            }
        }
        Map<Long,String> storeyMap=new HashMap<>();
        if(!CollectionUtils.isEmpty(storeyList)){
            for (Storey storey : storeyList) {
                storeyMap.put(storey.getId(),storey.getName());
            }
        }
        Map<Long,String> roomMap=new HashMap<>();
        if(!CollectionUtils.isEmpty(roomList)){
            for (Room room : roomList) {
                roomMap.put(room.getId(),room.getName());
            }
        }
        LocalDate localDate= LocalDate.now();
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        String format1 = dateTimeFormatter.format(localDate);
        int du= month / 3 + 1;
        BigDecimal bigDecimalThou=new BigDecimal(1000);
        List<Map<String,Object>> dataList=  equipmentDeviceDao.getMonthMeasure(projectId,format1);
        if(!CollectionUtils.isEmpty(dataList)){
            List<EnergyDeviceInfo> energyDeviceInfoList=new ArrayList<>();
            for (Map<String, Object> stringObjectMap : dataList) {
                Long id = Long.parseLong(stringObjectMap.get("id").toString());
                String name = stringObjectMap.get("name").toString();
                String energyName = stringObjectMap.get("energyName").toString();
                String consumeName = stringObjectMap.get("consumeName").toString();
                Integer monthConsume = Integer.parseInt(stringObjectMap.get("monthConsume").toString());
                BigDecimal monthConsumeBig=new BigDecimal(monthConsume);
                EnergyDeviceInfo energyDeviceInfo = new EnergyDeviceInfo(id, name, energyName, consumeName, monthConsumeBig.divide(bigDecimalThou, 3, BigDecimal.ROUND_HALF_UP).floatValue(), "");
                Object location = stringObjectMap.get("location");
                if(location!=null && !StringUtils.isEmpty(location.toString())){
                    energyDeviceInfo.setLocation(location.toString());
                }else{
                    StringBuffer buffer=new StringBuffer();
                    Long areaId = Long.parseLong(stringObjectMap.get("areaId").toString());
                    buffer.append(areaMap.get(areaId)==null?"":areaMap.get(areaId));
                    Long buildingId = Long.parseLong(stringObjectMap.get("buildingId").toString());
                    buffer.append(buildingMap.get(buildingId)==null?"":buildingMap.get(buildingId));
                    Long storeyId = Long.parseLong(stringObjectMap.get("storeyId").toString());
                    buffer.append(storeyMap.get(storeyId)==null?"":storeyMap.get(storeyId));
                    Long roomId = Long.parseLong(stringObjectMap.get("roomId").toString());
                    buffer.append(roomMap.get(roomId)==null?"":roomMap.get(roomId));
                    energyDeviceInfo.setLocation(buffer.toString());
                }
                energyDeviceInfoList.add(energyDeviceInfo);
            }
            energyDeviceInfoList.sort(Comparator.comparing(EnergyDeviceInfo::getTotalData).reversed());
            String[][] dataArr=new String[energyDeviceInfoList.size()][5];
            for (int i = 0; i <energyDeviceInfoList.size() ; i++) {
                EnergyDeviceInfo energyDeviceInfo = energyDeviceInfoList.get(i);
                dataArr[i][0]=energyDeviceInfo.getName();
                dataArr[i][1]=energyDeviceInfo.getEnergyTypeName();
                dataArr[i][2]=energyDeviceInfo.getConsumeTypeName();
                dataArr[i][3]=energyDeviceInfo.getLocation();
                dataArr[i][4]=energyDeviceInfo.getTotalData().toString();
            }
            dataMap.put("当月设备能耗排行榜",dataArr);
        }
        String dateJi="";
        if(du==1){
            dateJi=(year-1)+"-12-31";
        }else if(du==2){
            dateJi=year+"-03-31";
        }else if(du==3){
            dateJi=year+"-06-30";
        }else if(du==4){
            dateJi=year+"-09-30";
        }
        LocalDate localDateYear = localDate.plusYears(-1).plusDays(-1);
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = formatters.format(localDate);
        String dateYear = formatters.format(localDateYear);
        List<Map<String,Object>> measureToday=equipmentDeviceDao.getTodayMeasure(projectId,format);
        List<Map<String,Object>> measureJiDu=equipmentDeviceDao.getJiDuMeasure(projectId,dateJi);
        List<Map<String,Object>> measureYear=equipmentDeviceDao.getJiDuMeasure(projectId,dateYear);
        Map<Long,Integer> dataMapJiDu=new HashMap<>();
        Map<Long,Integer> dateMapYear=new HashMap<>();
        if(!CollectionUtils.isEmpty(measureJiDu)){
            for (Map<String, Object> stringObjectMap : measureJiDu) {
                Long id =Long.parseLong(stringObjectMap.get("id").toString());
                Integer count = Integer.parseInt(stringObjectMap.get("total").toString());
                dataMapJiDu.put(id,count);
            }
        }
        if(!CollectionUtils.isEmpty(measureYear)){
            for (Map<String, Object> stringObjectMap : measureYear) {
                Long id =Long.parseLong(stringObjectMap.get("id").toString());
                Integer count = Integer.parseInt(stringObjectMap.get("total").toString());
                dateMapYear.put(id,count);
            }
        }
        List<EnergyDeviceInfo> energyDeviceJi=new ArrayList<>();
        List<EnergyDeviceInfo> energyDeviceYear=new ArrayList<>();
        if(!CollectionUtils.isEmpty(measureToday)){
            for (Map<String, Object> stringObjectMap : measureToday) {
                Long id = Long.parseLong(stringObjectMap.get("id").toString());
                String name = stringObjectMap.get("name").toString();
                String energyName = stringObjectMap.get("energyName").toString();
                String consumeName = stringObjectMap.get("consumeName").toString();
                Integer total = Integer.parseInt(stringObjectMap.get("total").toString());
                Integer totalJi=total;
                Integer totalYear=total;
                if(dataMapJiDu.containsKey(id)){
                    Integer integer = dataMapJiDu.get(id);
                    totalJi=totalJi-integer;
                }
                if(dateMapYear.containsKey(id)){
                    Integer integer=dateMapYear.get(id);
                    totalYear=totalYear-integer;
                }
                BigDecimal monthConsumeJi=new BigDecimal(totalJi);
                BigDecimal monthConsumeYear=new BigDecimal(totalYear);
                EnergyDeviceInfo energyDeviceInfoJi = new EnergyDeviceInfo(id, name, energyName, consumeName, monthConsumeJi.divide(bigDecimalThou, 3, BigDecimal.ROUND_HALF_UP).floatValue(), "");
                EnergyDeviceInfo energyDeviceInfoYear = new EnergyDeviceInfo(id, name, energyName, consumeName, monthConsumeYear.divide(bigDecimalThou, 3, BigDecimal.ROUND_HALF_UP).floatValue(), "");
                Object location = stringObjectMap.get("location");
                if(location!=null){
                    energyDeviceInfoJi.setLocation(location.toString());
                }else{
                    StringBuffer buffer=new StringBuffer();
                    Long areaId = Long.parseLong(stringObjectMap.get("areaId").toString());
                    buffer.append(areaMap.get(areaId)==null?"":areaMap.get(areaId));
                    Long buildingId = Long.parseLong(stringObjectMap.get("buildingId").toString());
                    buffer.append(buildingMap.get(buildingId)==null?"":buildingMap.get(buildingId));
                    Long storeyId = Long.parseLong(stringObjectMap.get("storeyId").toString());
                    buffer.append(storeyMap.get(storeyId)==null?"":storeyMap.get(storeyId));
                    Long roomId = Long.parseLong(stringObjectMap.get("roomId").toString());
                    buffer.append(roomMap.get(roomId)==null?"":roomMap.get(roomId));
                    energyDeviceInfoJi.setLocation(buffer.toString());
                }
                energyDeviceJi.add(energyDeviceInfoJi);
                energyDeviceYear.add(energyDeviceInfoYear);
            }

        }
        if(!CollectionUtils.isEmpty(energyDeviceJi)){
            energyDeviceJi.sort(Comparator.comparing(EnergyDeviceInfo::getTotalData).reversed());
            String[][] energyDeviceJiArr=new String[energyDeviceJi.size()][5];
            for (int i = 0; i < energyDeviceJi.size(); i++) {
                EnergyDeviceInfo energyDeviceInfo = energyDeviceJi.get(i);
                energyDeviceJiArr[i][0]=energyDeviceInfo.getName();
                energyDeviceJiArr[i][1]=energyDeviceInfo.getEnergyTypeName();
                energyDeviceJiArr[i][2]=energyDeviceInfo.getConsumeTypeName();
                energyDeviceJiArr[i][3]=energyDeviceInfo.getLocation();
                energyDeviceJiArr[i][4]=energyDeviceInfo.getTotalData().toString();
            }
            dataMap.put("当季设备能耗排行榜",energyDeviceJiArr);
        }
        if(!CollectionUtils.isEmpty(energyDeviceYear)){
            energyDeviceYear.sort(Comparator.comparing(EnergyDeviceInfo::getTotalData).reversed());
            String[][] energyDeviceYearArr=new String[energyDeviceYear.size()][5];
            for (int i = 0; i < energyDeviceYear.size(); i++) {
                EnergyDeviceInfo energyDeviceInfo = energyDeviceYear.get(i);
                energyDeviceYearArr[i][0]=energyDeviceInfo.getName();
                energyDeviceYearArr[i][1]=energyDeviceInfo.getEnergyTypeName();
                energyDeviceYearArr[i][2]=energyDeviceInfo.getConsumeTypeName();
                energyDeviceYearArr[i][3]=energyDeviceInfo.getLocation();
                energyDeviceYearArr[i][4]=energyDeviceInfo.getTotalData().toString();
            }
            dataMap.put("当年设备能耗排行榜",energyDeviceYearArr);
        }
        if(!CollectionUtils.isEmpty(dataMap)){
            return dataMap;
        }else{
            return null;
        }
    }

    @Override
    public List<EnergyEquipmentDevice> findByProjectId(Long projectId) {
        return equipmentDeviceDao.findByProjectId(projectId);
    }
}
