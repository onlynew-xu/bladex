package com.steelman.iot.platform.device;

import com.alibaba.fastjson.JSONObject;
import com.steelman.iot.platform.entity.DeviceParamSmartSuper;
import com.steelman.iot.platform.utils.HttpWebUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("mT300SDeviceRemoteService")
public class MT300SDeviceRemoteService {

    @Value("${push.url}")
    private String pushUrl;

    /**
     * 智慧用电1129 设备消音
     * @param serialNum
     * @return
     * @throws Exception
     */
    public Boolean setErasure(String serialNum) throws Exception{
        Boolean flag=false;
        if(!StringUtils.isBlank(serialNum)){
            StringBuffer urlBuf=new StringBuffer(pushUrl);
            urlBuf.append("/super/channel/setErasure/");
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
     * 智慧用电1129 设备自检
     * @param serialNum
     * @return
     * @throws Exception
     */
    public Boolean selfInspection(String serialNum) throws Exception{
        Boolean flag=false;
        if(!StringUtils.isBlank(serialNum)){
            StringBuffer urlBuf=new StringBuffer(pushUrl);
            urlBuf.append("/super/channel/setSelfInspection/");
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
     * 智慧用电1129 设备复位
     * @param serialNum
     * @return
     * @throws Exception
     */
    public Boolean setRestoration(String serialNum) throws Exception{
        Boolean flag=false;
        if(!StringUtils.isBlank(serialNum)){
            StringBuffer urlBuf=new StringBuffer(pushUrl);
            urlBuf.append("/super/channel/setRestoration/");
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
     * 智慧用电1129 设备设置数据上报间隔
     * @param serialNum
     * @return
     * @throws Exception
     */
    public Boolean setInterval(String serialNum,Integer interval) throws Exception{
        Boolean flag=false;
        if(!StringUtils.isBlank(serialNum)){
            StringBuffer urlBuf=new StringBuffer(pushUrl);
            urlBuf.append("/super/channel/setDeviceParameter/");
            urlBuf.append(serialNum);
            String url=urlBuf.toString();
            Map<String,String> paramMap=new HashMap<>();
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
     * @param deviceParamSmartSuper
     * @return
     * @throws Exception
     */
    public Boolean setDeviceParameter(String serialNum, DeviceParamSmartSuper deviceParamSmartSuper) throws Exception{
        Boolean flag=false;
        if(!StringUtils.isBlank(serialNum)){
            StringBuffer urlBuf=new StringBuffer(pushUrl);
            urlBuf.append("/smart/channel/setDeviceParameter/");
            urlBuf.append(serialNum);
            String url=urlBuf.toString();
            Map<String,Object> paramMap=new HashMap<>();

            String modbusAddress = deviceParamSmartSuper.getModbusAddress();
            if(!StringUtils.isBlank(modbusAddress)){
                paramMap.put("ModbusAddress",modbusAddress);
            }
            String modbusBaudrate = deviceParamSmartSuper.getModbusBaudrate();
            if(!StringUtils.isBlank(modbusBaudrate)){
                paramMap.put("ModbusBaudrate",modbusBaudrate);
            }
            String reportInterval = deviceParamSmartSuper.getReportTime();
            if(!StringUtils.isBlank(reportInterval)){
                paramMap.put("ReportInterval",reportInterval);
            }
            String spikeReportTime = deviceParamSmartSuper.getSpikeReportTime();
            if(StringUtils.isNotBlank(spikeReportTime)){
                paramMap.put("SpikeReportTime",spikeReportTime);
            }

            String peakReportTime = deviceParamSmartSuper.getPeakReportTime();
            if(!StringUtils.isBlank(peakReportTime)){
                paramMap.put("PeakReportTime",peakReportTime);
            }
            String normalReportTime = deviceParamSmartSuper.getNormalReportTime();
            if(!StringUtils.isBlank(normalReportTime)){
                paramMap.put("NormalReportTime",normalReportTime);
            }
            String valleyReportTime = deviceParamSmartSuper.getValleyReportTime();
            if(!StringUtils.isBlank(valleyReportTime)){
                paramMap.put("ValleyReportTime",valleyReportTime);
            }

            String delayAlarmLeaked = deviceParamSmartSuper.getDelayAlarmLeaked();
            String delayAlarmTemp = deviceParamSmartSuper.getDelayAlarmTemp();
            String delayAlarmVolt = deviceParamSmartSuper.getDelayAlarmVolt();
            String delayAlarmAmp = deviceParamSmartSuper.getDelayAlarmAmp();
            if(StringUtils.isNotBlank(delayAlarmLeaked)&& StringUtils.isNotBlank(delayAlarmTemp) && StringUtils.isNotBlank(delayAlarmVolt) && StringUtils.isNotBlank(delayAlarmAmp)){
                paramMap.put("DelayAlarmLeaked",delayAlarmLeaked);
                paramMap.put("DelayAlarmTemp",delayAlarmTemp);
                paramMap.put("DelayAlarmVolt",delayAlarmVolt);
                paramMap.put("DelayAlarmAmp",delayAlarmAmp);
            }

            String ctRatioA = deviceParamSmartSuper.getCtRatioA();
            String ctRatioB = deviceParamSmartSuper.getCtRatioB();
            String ctRatioC = deviceParamSmartSuper.getCtRatioC();
            String ctRatioD = deviceParamSmartSuper.getCtRatioD();
            if(StringUtils.isNotBlank(ctRatioA)&&StringUtils.isNotBlank(ctRatioB)&&StringUtils.isNotBlank(ctRatioC)&&StringUtils.isNotBlank(ctRatioD)){
                paramMap.put("CtRatioA",ctRatioA);
                paramMap.put("CtRatioB",ctRatioB);
                paramMap.put("CtRatioC",ctRatioC);
                paramMap.put("CtRatioD",ctRatioD);
            }

            String powerFactorA = deviceParamSmartSuper.getPowerFactorA();
            String powerFactorB = deviceParamSmartSuper.getPowerFactorB();
            String powerFactorC = deviceParamSmartSuper.getPowerFactorC();
            if(StringUtils.isNotBlank(powerFactorA)&&StringUtils.isNotBlank(powerFactorB)&&StringUtils.isNotBlank(powerFactorC)){
                paramMap.put("PowerFactorA",powerFactorA);
                paramMap.put("PowerFactorB",powerFactorB);
                paramMap.put("PowerFactorC",powerFactorC);
            }
            String overVoltageA = deviceParamSmartSuper.getOverVoltageA();
            String overVoltageB = deviceParamSmartSuper.getOverVoltageB();
            String overVoltageC = deviceParamSmartSuper.getOverVoltageC();
            if(StringUtils.isNotBlank(overVoltageA)&&StringUtils.isNotBlank(overVoltageB)&&StringUtils.isNotBlank(overVoltageC)){
                paramMap.put("OverVoltageA",overVoltageA);
                paramMap.put("OverVoltageB",overVoltageB);
                paramMap.put("OverVoltageC",overVoltageC);
            }
            String underVoltageA = deviceParamSmartSuper.getUnderVoltageA();
            String underVoltageB = deviceParamSmartSuper.getUnderVoltageB();
            String underVoltageC = deviceParamSmartSuper.getUnderVoltageC();
            if(StringUtils.isNotBlank(underVoltageA)&&StringUtils.isNotBlank(underVoltageB)&&StringUtils.isNotBlank(underVoltageC)){
                paramMap.put("UnderVoltageA",underVoltageA);
                paramMap.put("UnderVoltageB",underVoltageB);
                paramMap.put("UnderVoltageC",underVoltageC);
            }
            String lackPhaseA = deviceParamSmartSuper.getLackPhaseA();
            String lackPhaseB = deviceParamSmartSuper.getLackPhaseB();
            String lackPhaseC = deviceParamSmartSuper.getLackPhaseC();
            if(StringUtils.isNotBlank(lackPhaseA)&&StringUtils.isNotBlank(lackPhaseB)&&StringUtils.isNotBlank(lackPhaseC)){
                paramMap.put("LackPhaseA",lackPhaseA);
                paramMap.put("LackPhaseB",lackPhaseB);
                paramMap.put("LackPhaseC",lackPhaseC);
            }
            String overCurrentA = deviceParamSmartSuper.getOverCurrentA();
            String overCurrentB = deviceParamSmartSuper.getOverCurrentB();
            String overCurrentC = deviceParamSmartSuper.getOverCurrentC();
            if(StringUtils.isNotBlank(overCurrentA)&&StringUtils.isNotBlank(overCurrentB)&&StringUtils.isNotBlank(overCurrentC)){
                paramMap.put("OverCurrentA",overCurrentA);
                paramMap.put("OverCurrentB",overCurrentB);
                paramMap.put("OverCurrentC",overCurrentC);
            }
            String currentThdA = deviceParamSmartSuper.getCurrentThdA();
            String currentThdB = deviceParamSmartSuper.getCurrentThdB();
            String currentThdC = deviceParamSmartSuper.getCurrentThdC();
            if(StringUtils.isNotBlank(currentThdA)&&StringUtils.isNotBlank(currentThdB)&&StringUtils.isNotBlank(currentThdC)){
                paramMap.put("CurrentThdA",currentThdA);
                paramMap.put("CurrentThdB",currentThdB);
                paramMap.put("CurrentThdC",currentThdC);
            }
            String voltageThdA = deviceParamSmartSuper.getVoltageThdA();
            String voltageThdB = deviceParamSmartSuper.getVoltageThdB();
            String voltageThdC = deviceParamSmartSuper.getVoltageThdC();
            if(StringUtils.isNotBlank(voltageThdA)&&StringUtils.isNotBlank(voltageThdB)&&StringUtils.isNotBlank(voltageThdC)){
                paramMap.put("VoltageThdA",voltageThdA);
                paramMap.put("VoltageThdB",voltageThdB);
                paramMap.put("VoltageThdC",voltageThdC);
            }

            String overLeaked = deviceParamSmartSuper.getOverLeaked();
            if(StringUtils.isNotBlank(overLeaked)){
                paramMap.put("OverLeaked",overLeaked);
            }
            String overTemper= deviceParamSmartSuper.getOverTemper();
            if(StringUtils.isNotBlank(overTemper)){
                paramMap.put("OverTemper",overTemper);
            }
            String currentUnbalance = deviceParamSmartSuper.getCurrentUnbalance();
            if(StringUtils.isNotBlank(currentUnbalance)){
                paramMap.put("CurrentUnbalance",currentUnbalance);
            }
            String voltageUnbalance = deviceParamSmartSuper.getVoltageUnbalance();
            if(StringUtils.isNotBlank(voltageUnbalance)){
                paramMap.put("VoltageUnbalance",voltageUnbalance);
            }

            String didoSet = deviceParamSmartSuper.getDidoSet();
            if(StringUtils.isNotBlank(didoSet)){
                paramMap.put("DidoSet",didoSet);
            }

            String systemSw = deviceParamSmartSuper.getSystemSw();
            if(StringUtils.isNotBlank(systemSw)){
                paramMap.put("SystemSw",systemSw);
            }

            String faultEn = deviceParamSmartSuper.getFaultEn();
            if(StringUtils.isNotBlank(faultEn)){
                paramMap.put("FaultEn",faultEn);
            }
            String alarmSw = deviceParamSmartSuper.getAlarmSw();
            if(StringUtils.isNotBlank(alarmSw)){
                paramMap.put("AlarmSw",alarmSw);
            }
            String spike = deviceParamSmartSuper.getSpike();
            if(StringUtils.isNotBlank(spike)){
                paramMap.put("spike",spike);
            }
            String peak = deviceParamSmartSuper.getPeak();
            if(StringUtils.isNotBlank(peak)){
                paramMap.put("peak",peak);
            }
            String normal = deviceParamSmartSuper.getNormal();
            if(StringUtils.isNotBlank(normal)){
                paramMap.put("normal",normal);
            }
            String valley = deviceParamSmartSuper.getValley();
            if(StringUtils.isNotBlank(valley)){
                paramMap.put("valley",valley);
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
