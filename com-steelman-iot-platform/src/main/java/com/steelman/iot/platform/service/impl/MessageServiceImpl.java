package com.steelman.iot.platform.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.steelman.iot.platform.entity.*;
import com.steelman.iot.platform.push.GtPushApi;
import com.steelman.iot.platform.service.*;
import com.steelman.iot.platform.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author lsj
 * @DATE 2021/4/19 0019 9:59
 * @Description:
 */
@Service("messageService")
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Autowired
    private RedisTemplate redisTemplate;


    @Value("${spring.mail.host}")
    private String mailHost;
    @Value("${spring.mail.username}")
    private String from;

    @Value("${aliyun.sms.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.sms.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.sms.signName}")
    private String signName;
    @Value("${aliyun.sms.warnTemplateCode}")
    private String warnTemplateCode;
    @Value("${aliyun.sms.offlineTemplateCode}")
    private String offlineTemplateCode;

    private static IAcsClient acsClient;

    @PostConstruct
    public void init() {
        try {
            //可自助调整超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
            //初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "Dysmsapi", "dysmsapi.aliyuncs.com");
            acsClient = new DefaultAcsClient(profile);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("###Exception ===>{}", e);
        }

    }

    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private UserService userService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private AlarmItemService alarmItemService;
    @Resource
    private ContactService contactService;
    @Resource
    private UserProjectRoleService userProjectRoleService;
    @Resource
    private ProjectService projectService;
    @Resource
    private DeviceTypeService deviceTypeService;

    @Override
    public void sendTextMail(String toMail, String content, String subject) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        try {
            mailMessage.setTo(toMail); //发送目标
            mailMessage.setText(content); //发送内容
            mailMessage.setSubject(subject); //标题
            mailMessage.setFrom(from); //发送人
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            log.error("普通文本邮件发送失败", e);
        }
    }


    @Override
    public void sendAlarmMsg(String content) {
        AlarmWarn alarmWarn = JSONObject.parseObject(content, AlarmWarn.class);
        //报警设备
        Device device = deviceService.findById(alarmWarn.getDeviceId());
        DeviceType deviceType = deviceTypeService.findByid(device.getDeviceTypeId());
        //报警类型
        AlarmItem alarmItem = alarmItemService.findByid(alarmWarn.getAlarmItemId());
        Contact contact = contactService.selectProjectContact(alarmWarn.getProjectId());
        String deviceContent = getDeviceContent(device.getBindingType(), device.getId());
        List<String> userNameList = userProjectRoleService.findUserNameByProjectId(alarmWarn.getProjectId());
        String time = DateUtils.format(alarmWarn.getCreateTime().getTime(), "yyyy-MM-dd HH:mm:ss");
        String body = "";
        Project project = projectService.findById(alarmWarn.getProjectId());
        if (alarmWarn.getSystemId() == 1000) {
            body = "在" + project.getName() + "中," + deviceType.getName() + "类型的" + device.getName() + "在" + time + "发生了告警" + alarmItem.getName();
        }
        if (alarmWarn.getSystemId() == 2000) {
            body = "在" + project.getName() + "中," + deviceContent + "在" + time + "发生了告警" + alarmItem.getName();
        }

        log.info("body ==>{}", body);
        List<String> aliasList = new ArrayList<>();
        for (String userName : userNameList) {
            Map<String, String> map = redisTemplate.opsForHash().entries("PUSH_" + userName);
            for (String key : map.keySet()) {
                aliasList.add(map.get(key));
            }
        }
        //推送
        String taskId = GtPushApi.createTask("设备报警通知", body);
        log.info("aliasList ===>{}", aliasList);
        log.info("taskId ===>{}", taskId);
        GtPushApi.pushAliasList(aliasList, taskId);


        //推送内容
        // String body = deviceBindType + "报警,告警项:" + alarmItem.getName();
        //个推
        //GtPushApi.singlePushAlias("", body, aliasList);
        if(contact!=null&&StringUtils.isNotBlank(contact.getEmail())){
            sendTextMail(contact.getEmail(), body, "设备报警通知");
        }
        //短信
        if(contact!=null&&StringUtils.isNotBlank(contact.getTel())){
            sendSms(contact.getTel(), "", warnTemplateCode);
        }
    }


    @Override
    public void sendOfflineMsg(String content) {
        AlarmWarn alarmWarn = JSONObject.parseObject(content, AlarmWarn.class);
        //报警设备
        Device device = deviceService.findById(alarmWarn.getDeviceId());
        DeviceType deviceType = deviceTypeService.findByid(device.getDeviceTypeId());
        String deviceContent = getDeviceContent(device.getBindingType(), device.getId());
        Project project = projectService.findById(alarmWarn.getProjectId());
        String time = DateUtils.format(alarmWarn.getCreateTime().getTime(), "yyyy-MM-dd HH:mm:ss");
        String body = "";
        if (alarmWarn.getSystemId() == 1000) {
            body = "在" + project.getName() + "中," + deviceType.getName() + "类型的" + device.getName() + "在" + time + "离线了";
        }
        if (alarmWarn.getSystemId() == 2000) {
            body = "在" + project.getName() + "中," + deviceContent + "在" + time + "离线了";
        }

        log.info("body ==>{}", body);

        Contact contact = contactService.selectProjectContact(alarmWarn.getProjectId());
        List<String> aliasList = new ArrayList<>();
        List<String> userNameList = userProjectRoleService.findUserNameByProjectId(alarmWarn.getProjectId());
        if(!CollectionUtils.isEmpty(userNameList)){
            for (String userName : userNameList) {
                Map<String, String> map = redisTemplate.opsForHash().entries("PUSH_" + userName);
                if(!CollectionUtils.isEmpty(map)){
                    for (String key : map.keySet()) {
                        aliasList.add(map.get(key));
                    }
                }

            }
        }
        //推送
        if(!CollectionUtils.isEmpty(aliasList)){
            String taskId = GtPushApi.createTask("设备离线通知", body);
            log.info("aliasList ===>{}", aliasList);
            log.info("taskId ===>{}", taskId);
            GtPushApi.pushAliasList(aliasList, taskId);
        }

//
//        //别名 手机号+项目Id
//        aliasList.addAll(userNameList);
//        //发推送
//        GtPushApi.singlePushAlias("设备离线通知", body, aliasList);
        //发送邮件
        if(contact!=null){
            sendTextMail(contact.getEmail(), body, "设备离线通知");
            //发送短信
            sendSms(contact.getTel(), "", offlineTemplateCode);
        }

    }

    @Resource
    private PowerDeviceService powerDeviceService;
    @Resource
    private PowerIncomingDeviceService powerIncomingDeviceService;
    @Resource
    private PowerService powerService;
    @Resource
    private PowerCompensateDeviceService powerCompensateDeviceService;
    @Resource
    private PowerWaveDeviceService powerWaveDeviceService;
    @Resource
    private PowerFeederLoopDeviceService powerFeederLoopDeviceService;
    @Resource
    private PowerFeederService powerFeederService;
    @Resource
    private PowerBoxLoopDeviceService powerBoxLoopDeviceService;
    @Resource
    private PowerBoxService powerBoxService;
    @Resource
    private EnergyEquipmentDeviceService equipmentDeviceService;

    private String getDeviceContent(Integer bindingType, Long id) {
        if (bindingType == 0) {
            String powerName = powerDeviceService.findPowerNameByDeviceId(id);
            return powerName;
        } else if (bindingType == 1) {
            PowerIncoming powerInComing = powerIncomingDeviceService.findInComingByDeviceId(id);
            Power power = powerService.getPowerInfo(powerInComing.getPowerId());
            return power.getName() + powerInComing.getName();
        } else if (bindingType == 2) {
            PowerCompensate powerCompensate = powerCompensateDeviceService.findCompensateByDeviceId(id);
            Power power = powerService.getPowerInfo(powerCompensate.getPowerId());
            return power.getName() + powerCompensate.getName();
            //return "补偿柜设备";
        } else if (bindingType == 3) {
            PowerWave powerWave = powerWaveDeviceService.findWaveByDeviceId(id);
            Power power = powerService.getPowerInfo(powerWave.getPowerId());
            return power.getName() + powerWave.getName();
            //return "滤波柜设备";
        } else if (bindingType == 4) {
            PowerFeederLoop powerFeederLoop = powerFeederLoopDeviceService.findFeederLoopByDeviceId(id);
            PowerFeeder powerFeeder = powerFeederService.getFeederInfo(powerFeederLoop.getFeederId());
            Power power = powerService.getPowerInfo(powerFeederLoop.getPowerId());
            return power.getName() + powerFeeder.getName() + powerFeederLoop.getName();
            //return "馈线柜";
        } else if (bindingType == 5) {
            PowerBoxLoop powerBoxLoop = powerBoxLoopDeviceService.findBoxLoopByDeviceId(id);
            PowerBox powerBox = powerBoxService.getBoxInfo(powerBoxLoop.getBoxId());
            Power power = powerService.getPowerInfo(powerBox.getPowerId());
            return power.getName() + powerBox.getName() + powerBoxLoop.getName();
            //return "配电箱";
        } else if (bindingType == 6) {
            EnergyEquipment equipment = equipmentDeviceService.findEnergyEquipment(id);
            return equipment.getName();
        } else {
            return "未绑定设备";
        }
    }

    /**
     * 发短信
     *
     * @param phone
     * @return
     */
    public SendSmsResponse sendSms(String phone, String param, String templateCode) {
        try {
            SendSmsRequest request = new SendSmsRequest();
            request.setPhoneNumbers(phone);
            request.setSignName(signName);
            request.setTemplateCode(templateCode);
            //request.setTemplateParam("{\"code\":" + "\"" + code + "\"" + "}");
            SendSmsResponse response = send(request);
            if (response.getCode() != null && response.getCode().equals("OK")) {
                log.debug("send sms OK" + response.getBizId());
            }
            return response;
        } catch (Exception e) {
            log.error("### send sms error");
            return null;
        }

    }

    public static SendSmsResponse send(SendSmsRequest request) {
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            log.error("###send sms error ==> {}", e.getMessage());
        }
        return sendSmsResponse;
    }


}
