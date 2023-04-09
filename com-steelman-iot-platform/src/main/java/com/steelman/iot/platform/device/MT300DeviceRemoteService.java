package com.steelman.iot.platform.device;

import com.alibaba.fastjson.JSONObject;
import com.steelman.iot.platform.entity.DeviceParamsSmartElec;
import com.steelman.iot.platform.service.impl.BaseService;
import com.steelman.iot.platform.utils.HttpWebUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("mT300DeviceRemoteService")
public class MT300DeviceRemoteService extends BaseService {

    @Value("${push.url}")
    private String pushUrl;

    /**
     * 智慧用电1128 设备消音
     * @param serialNum
     * @return
     * @throws Exception
     */
    public Boolean setErasure(String serialNum) throws Exception{
        Boolean flag=false;
        if(!StringUtils.isBlank(serialNum)){
            StringBuffer urlBuf=new StringBuffer(pushUrl);
            urlBuf.append("/smart/channel/setErasure/");
            urlBuf.append(serialNum);
            String url=urlBuf.toString();
            String resultRe = HttpWebUtils.get(url, null);
            JSONObject jsonObject = JSONObject.parseObject(resultRe);
            String code = jsonObject.get("code").toString();
            if(code.equals("200")){
                flag=true;
            }
        }
        return flag;
    }

    /**
     * 智慧用电1128 设备自检
     * @param serialNum
     * @return
     * @throws Exception
     */
    public Boolean selfInspection(String serialNum) throws Exception{
        Boolean flag=false;
        if(!StringUtils.isBlank(serialNum)){
            StringBuffer urlBuf=new StringBuffer(pushUrl);
            urlBuf.append("/smart/channel/setSelfInspection/");
            urlBuf.append(serialNum);
            String url=urlBuf.toString();
            String resultRe = HttpWebUtils.get(url, null);
            JSONObject jsonObject = JSONObject.parseObject(resultRe);
            String code = jsonObject.get("code").toString();
            if(code.equals("200")){
                flag=true;
            }
        }
        return flag;
    }
    /**
     * 智慧用电1128 设备复位
     * @param serialNum
     * @return
     * @throws Exception
     */
    public Boolean setRestoration(String serialNum) throws Exception{
        Boolean flag=false;
        if(!StringUtils.isBlank(serialNum)){
            StringBuffer urlBuf=new StringBuffer(pushUrl);
            urlBuf.append("/smart/channel/setRestoration/");
            urlBuf.append(serialNum);
            String url=urlBuf.toString();
            String resultRe = HttpWebUtils.get(url, null);
            JSONObject jsonObject = JSONObject.parseObject(resultRe);
            String code = jsonObject.get("code").toString();
            if(code.equals("200")){
                flag=true;
            }
        }
        return flag;
    }

    /**
     * 智慧用电1128 设备设置数据上报间隔
     * @param serialNum
     * @return
     * @throws Exception
     */
    public Boolean setInterval(String serialNum,Integer interval) throws Exception{
        Boolean flag=false;
        if(!StringUtils.isBlank(serialNum)){
            StringBuffer urlBuf=new StringBuffer(pushUrl);
            urlBuf.append("/smart/channel/setDeviceParameter/");
            urlBuf.append(serialNum);
            String url=urlBuf.toString();
            Map<String,String>  paramMap=new HashMap<>();
            paramMap.put("ReportInterval",String.valueOf(interval));
            String resultRe = HttpWebUtils.post(url, JsonUtils.toJson(paramMap));
            JSONObject jsonObject = JSONObject.parseObject(resultRe);
            String code = jsonObject.get("code").toString();
            if(code.equals("200")){
                flag=true;
            }
        }
        return flag;
    }

    /**
     * 设置设备参数
     * @param serialNum
     * @param deviceParamsSmartElec
     * @return
     * @throws Exception
     */
    public Boolean setDeviceParameter(String serialNum, DeviceParamsSmartElec deviceParamsSmartElec) throws Exception{
        Boolean flag=false;
        if(!StringUtils.isBlank(serialNum)){
            StringBuffer urlBuf=new StringBuffer(pushUrl);
            urlBuf.append("/smart/channel/setDeviceParameter/");
            urlBuf.append(serialNum);
            String url=urlBuf.toString();
            Map<String,Object> paramMap=new HashMap<>();
            String transCapacity = deviceParamsSmartElec.getTransCapacity();
            if(!StringUtils.isBlank(transCapacity)){
                paramMap.put("TransCapacity",transCapacity);
            }
            String modbusAddress = deviceParamsSmartElec.getModbusAddress();
            if(!StringUtils.isBlank(modbusAddress)){
                paramMap.put("ModbusAddress",modbusAddress);
            }
            String modbusBaudrate = deviceParamsSmartElec.getModbusBaudrate();
            if(!StringUtils.isBlank(modbusBaudrate)){
                paramMap.put("ModbusBaudrate",modbusBaudrate);
            }
            String reportInterval = deviceParamsSmartElec.getReportInterval();
            if(!StringUtils.isBlank(reportInterval)){
                paramMap.put("ReportInterval",reportInterval);
            }
            String peakReportTime = deviceParamsSmartElec.getPeakReportTime();
            if(!StringUtils.isBlank(peakReportTime)){
                paramMap.put("PeakReportTime",peakReportTime);
            }
            String normalReportTime = deviceParamsSmartElec.getNormalReportTime();
            if(!StringUtils.isBlank(normalReportTime)){
                paramMap.put("NormalReportTime",normalReportTime);
            }
            String valleyReportTime = deviceParamsSmartElec.getValleyReportTime();
            if(!StringUtils.isBlank(valleyReportTime)){
                paramMap.put("ValleyReportTime",valleyReportTime);
            }
            String ctRatioA = deviceParamsSmartElec.getCtRatioA();
            String ctRatioB = deviceParamsSmartElec.getCtRatioB();
            String ctRatioC = deviceParamsSmartElec.getCtRatioC();
            String ctRatioD = deviceParamsSmartElec.getCtRatioD();
            if(StringUtils.isNotBlank(ctRatioA)&&StringUtils.isNotBlank(ctRatioB)&&StringUtils.isNotBlank(ctRatioC)&&StringUtils.isNotBlank(ctRatioD)){
                paramMap.put("CtRatioA",ctRatioA);
                paramMap.put("CtRatioB",ctRatioB);
                paramMap.put("CtRatioC",ctRatioC);
                paramMap.put("CtRatioD",ctRatioD);
            }
            String transLoad = deviceParamsSmartElec.getTransLoad();
            if(StringUtils.isNotBlank(transLoad)){
                paramMap.put("TransLoad",transLoad);
            }
            String powerFactorA = deviceParamsSmartElec.getPowerFactorA();
            String powerFactorB = deviceParamsSmartElec.getPowerFactorB();
            String powerFactorC = deviceParamsSmartElec.getPowerFactorC();
            if(StringUtils.isNotBlank(powerFactorA)&&StringUtils.isNotBlank(powerFactorB)&&StringUtils.isNotBlank(powerFactorC)){
                paramMap.put("PowerFactorA",powerFactorA);
                paramMap.put("PowerFactorB",powerFactorB);
                paramMap.put("PowerFactorC",powerFactorC);
            }
            String overVoltageA = deviceParamsSmartElec.getOverVoltageA();
            String overVoltageB = deviceParamsSmartElec.getOverVoltageB();
            String overVoltageC = deviceParamsSmartElec.getOverVoltageC();
            if(StringUtils.isNotBlank(overVoltageA)&&StringUtils.isNotBlank(overVoltageB)&&StringUtils.isNotBlank(overVoltageC)){
                paramMap.put("OverVoltageA",overVoltageA);
                paramMap.put("OverVoltageB",overVoltageB);
                paramMap.put("OverVoltageC",overVoltageC);
            }
            String underVoltageA = deviceParamsSmartElec.getUnderVoltageA();
            String underVoltageB = deviceParamsSmartElec.getUnderVoltageB();
            String underVoltageC = deviceParamsSmartElec.getUnderVoltageC();
            if(StringUtils.isNotBlank(underVoltageA)&&StringUtils.isNotBlank(underVoltageB)&&StringUtils.isNotBlank(underVoltageC)){
                paramMap.put("UnderVoltageA",underVoltageA);
                paramMap.put("UnderVoltageB",underVoltageB);
                paramMap.put("UnderVoltageC",underVoltageC);
            }
            String lackPhaseA = deviceParamsSmartElec.getLackPhaseA();
            String lackPhaseB = deviceParamsSmartElec.getLackPhaseB();
            String lackPhaseC = deviceParamsSmartElec.getLackPhaseC();
            if(StringUtils.isNotBlank(lackPhaseA)&&StringUtils.isNotBlank(lackPhaseB)&&StringUtils.isNotBlank(lackPhaseC)){
                paramMap.put("LackPhaseA",lackPhaseA);
                paramMap.put("LackPhaseB",lackPhaseB);
                paramMap.put("LackPhaseC",lackPhaseC);
            }
            String overCurrentA = deviceParamsSmartElec.getOverCurrentA();
            String overCurrentB = deviceParamsSmartElec.getOverCurrentB();
            String overCurrentC = deviceParamsSmartElec.getOverCurrentC();
            if(StringUtils.isNotBlank(overCurrentA)&&StringUtils.isNotBlank(overCurrentB)&&StringUtils.isNotBlank(overCurrentC)){
                paramMap.put("OverCurrentA",overCurrentA);
                paramMap.put("OverCurrentB",overCurrentB);
                paramMap.put("OverCurrentC",overCurrentC);
            }
            String currentThdA = deviceParamsSmartElec.getCurrentThdA();
            String currentThdB = deviceParamsSmartElec.getCurrentThdB();
            String currentThdC = deviceParamsSmartElec.getCurrentThdC();
            if(StringUtils.isNotBlank(currentThdA)&&StringUtils.isNotBlank(currentThdB)&&StringUtils.isNotBlank(currentThdC)){
                paramMap.put("CurrentThdA",currentThdA);
                paramMap.put("CurrentThdB",currentThdB);
                paramMap.put("CurrentThdC",currentThdC);
            }
            String voltageThdA = deviceParamsSmartElec.getVoltageThdA();
            String voltageThdB = deviceParamsSmartElec.getVoltageThdB();
            String voltageThdC = deviceParamsSmartElec.getVoltageThdC();
            if(StringUtils.isNotBlank(voltageThdA)&&StringUtils.isNotBlank(voltageThdB)&&StringUtils.isNotBlank(voltageThdC)){
                paramMap.put("VoltageThdA",voltageThdA);
                paramMap.put("VoltageThdB",voltageThdB);
                paramMap.put("VoltageThdC",voltageThdC);
            }
            String didoSet = deviceParamsSmartElec.getDidoSet();
            if(StringUtils.isNotBlank(didoSet)){
                paramMap.put("DidoSet",didoSet);
            }
            String systemSw = deviceParamsSmartElec.getSystemSw();
            if(StringUtils.isNotBlank(systemSw)){
                paramMap.put("SystemSw",systemSw);
            }
            String alarmSw = deviceParamsSmartElec.getAlarmSw();
            if(StringUtils.isNotBlank(alarmSw)){
                paramMap.put("AlarmSw",alarmSw);
            }
            String peak = deviceParamsSmartElec.getPeak();
            if(StringUtils.isNotBlank(peak)){
                paramMap.put("peak",peak);
            }
            String normal = deviceParamsSmartElec.getNormal();
            if(StringUtils.isNotBlank(normal)){
                paramMap.put("normal",normal);
            }
            String valley = deviceParamsSmartElec.getValley();
            if(StringUtils.isNotBlank(valley)){
                paramMap.put("valley",valley);
            }
            String peakPrice = deviceParamsSmartElec.getPeakPrice();
            if(StringUtils.isNotBlank(peakPrice)){
                paramMap.put("PeakPrice",peakPrice);
            }
            String normalPrice = deviceParamsSmartElec.getNormalPrice();
            if(StringUtils.isNotBlank(normalPrice)){
                paramMap.put("NormalPrice",normalPrice);
            }
            String valleyPrice = deviceParamsSmartElec.getValleyPrice();
            if(StringUtils.isNotBlank(valleyPrice)){
                paramMap.put("ValleyPrice",valleyPrice);
            }
            String overLeaked = deviceParamsSmartElec.getOverLeaked();
            if(StringUtils.isNotBlank(overLeaked)){
                paramMap.put("OverLeaked",overLeaked);
            }
            String overTemper = deviceParamsSmartElec.getOverTemper();
            if(StringUtils.isNotBlank(overTemper)){
                paramMap.put("OverTemper",overTemper);
            }
            String resultRe = HttpWebUtils.post(url, JsonUtils.toJson(paramMap));
            JSONObject jsonObject = JSONObject.parseObject(resultRe);
            String code = jsonObject.get("code").toString();
            if(code.equals("200")){
                flag=true;
            }
        }
        return flag;
    }


}
