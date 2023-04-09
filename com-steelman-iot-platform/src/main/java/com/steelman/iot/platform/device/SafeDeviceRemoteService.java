package com.steelman.iot.platform.device;

import com.alibaba.fastjson.JSONObject;
import com.steelman.iot.platform.entity.DeviceParamsSafeElec;
import com.steelman.iot.platform.service.impl.BaseService;
import com.steelman.iot.platform.utils.HttpWebUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service("safeDeviceRemoteService")
public class SafeDeviceRemoteService extends BaseService {
    @Value("${push.url}")
    private String pushUrl;

    /**
     * 电气火灾和消防电源消音
     * @param serialNum
     * @return
     */
    public Boolean erasure(String serialNum) throws  Exception{
        Boolean flag=false;
        if(!StringUtils.isBlank(serialNum)){
            StringBuffer urlBuf=new StringBuffer(pushUrl);
            urlBuf.append("/safe/channel/setErasure/");
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
     * 电气火灾和消防电源自检
     * @param serialNum
     * @return
     */
    public Boolean selfInspection(String serialNum) throws  Exception{
        Boolean flag=false;
        if(!StringUtils.isBlank(serialNum)){
            StringBuffer urlBuf=new StringBuffer(pushUrl);
            urlBuf.append("/safe/channel/setSelfInspection/");
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
     * 电气火灾和消防电源复位
     * @param serialNum
     * @return
     */
    public Boolean restoration(String serialNum) throws  Exception{
        Boolean flag=false;
        if(!StringUtils.isBlank(serialNum)){
            StringBuffer urlBuf=new StringBuffer(pushUrl);
            urlBuf.append("/safe/channel/setRestoration/");
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
     * 设置设备的上报时间间隔
     * @param serialNum
     * @param time
     * @return
     * @throws Exception
     */
    public Boolean setUpInterval(String serialNum,Integer time) throws  Exception{
        Boolean flag=false;
        if(!StringUtils.isBlank(serialNum)&&time!=null){
            StringBuffer urlBuf=new StringBuffer(pushUrl);
            urlBuf.append("/safe/channel/setUpInterval/");
            urlBuf.append(serialNum);
            String url=urlBuf.toString();
            Map<String,String> paramMap=new HashMap<>();
            paramMap.put("interval",String.valueOf(time));
            String resultRe = HttpWebUtils.get(url, paramMap);
            JSONObject jsonObject = JSONObject.parseObject(resultRe);
            String code = jsonObject.get("code").toString();
            if(code.equals("200")){
                flag=true;
            }
        }
        return flag;
    }

    /**
     * 设置电气火灾和消防电源的告警开关
     * @param serialNum
     * @param alarmEn
     * @return
     * @throws Exception
     */
    public Boolean setAlarmSwitch(String serialNum,String alarmEn) throws  Exception{
        Boolean flag=false;
        if((!StringUtils.isBlank(serialNum)) && (!StringUtils.isBlank(alarmEn))){
            StringBuffer urlBuf=new StringBuffer(pushUrl);
            urlBuf.append("/safe/channel/setAlarmSwitch/");
            urlBuf.append(serialNum);
            String url=urlBuf.toString();
            Map<String,String> paramMap=new HashMap<>();
            paramMap.put("alarmEn",alarmEn);
            String resultRe = HttpWebUtils.get(url, paramMap);
            JSONObject jsonObject = JSONObject.parseObject(resultRe);
            String code = jsonObject.get("code").toString();
            if(code.equals("200")){
                flag=true;
            }
        }
        return flag;
    }
    public  Boolean sendDeviceParam(DeviceParamsSafeElec deviceParamsSafeElec) throws Exception{
        Boolean flag=false;
        if(deviceParamsSafeElec!=null&& !(StringUtils.isBlank(deviceParamsSafeElec.getSerialNum()))){
            StringBuffer urlBuf=new StringBuffer(pushUrl);
            urlBuf.append("/safe/channel/setDeviceParam/");
            urlBuf.append(deviceParamsSafeElec.getSerialNum());
            String url=urlBuf.toString();
            Map<String,Object> paramMap=new LinkedHashMap<>();
            String upInterval = deviceParamsSafeElec.getUpInterval();
            if(!StringUtils.isBlank(upInterval)){
                paramMap.put("interval",upInterval);
            }
            String tempInterval = deviceParamsSafeElec.getTempInterval();
            if(!StringUtils.isBlank(tempInterval)){
                paramMap.put("tempAlarmInterval",tempInterval);
            }
            String leakInterval = deviceParamsSafeElec.getLeakInterval();
            if(!StringUtils.isBlank(leakInterval)){
                paramMap.put("leakCurrAlarmInterval",leakInterval);
            }
            String currInterval = deviceParamsSafeElec.getCurrInterval();
            if(!StringUtils.isBlank(currInterval)){
                paramMap.put("currAlarmInterval",currInterval);
            }
            String voltInterval = deviceParamsSafeElec.getVoltInterval();
            if(!StringUtils.isBlank(voltInterval)){
                paramMap.put("voltAlarmInterval",voltInterval);
            }
            String alarmEn = deviceParamsSafeElec.getAlarmEn();
            if(!StringUtils.isBlank(alarmEn)){
                paramMap.put("alarmEn",alarmEn);
            }
            String voltHigh = deviceParamsSafeElec.getVoltHigh();
            if(!StringUtils.isBlank(voltHigh)){
                paramMap.put("voltHigh",voltHigh);
            }
            String voltLow = deviceParamsSafeElec.getVoltLow();
            if(!StringUtils.isBlank(voltLow)){
                paramMap.put("voltLow",voltLow);
            }
            String currLeak = deviceParamsSafeElec.getCurrLeak();
            if(!StringUtils.isBlank(currLeak)){
                paramMap.put("currLeak",currLeak);
            }
            String currHigh = deviceParamsSafeElec.getCurrHigh();
            if(!StringUtils.isBlank(currHigh)){
                paramMap.put("currHigh",currHigh);
            }
            String tempHigh = deviceParamsSafeElec.getTempHigh();
            if(!StringUtils.isBlank(tempHigh)){
                paramMap.put("tempHigh",tempHigh);
            }
            String json= JsonUtils.toJson(paramMap);
            String result = HttpWebUtils.post(url, json);
            JSONObject jsonObject = JSONObject.parseObject(result);
            String code = jsonObject.get("code").toString();
            if(code.equals("200")){
                flag=true;
            }
        }
        return flag;
    }

}
