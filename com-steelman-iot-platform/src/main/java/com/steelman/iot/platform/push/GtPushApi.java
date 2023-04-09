package com.steelman.iot.platform.push;

import com.getui.push.v2.sdk.ApiHelper;
import com.getui.push.v2.sdk.GtApiConfiguration;
import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.api.UserApi;
import com.getui.push.v2.sdk.common.ApiResult;
import com.getui.push.v2.sdk.dto.CommonEnum;
import com.getui.push.v2.sdk.dto.req.*;
import com.getui.push.v2.sdk.dto.req.message.PushChannel;
import com.getui.push.v2.sdk.dto.req.message.PushDTO;
import com.getui.push.v2.sdk.dto.req.message.PushMessage;
import com.getui.push.v2.sdk.dto.req.message.android.AndroidDTO;
import com.getui.push.v2.sdk.dto.req.message.android.GTNotification;
import com.getui.push.v2.sdk.dto.req.message.android.ThirdNotification;
import com.getui.push.v2.sdk.dto.req.message.android.Ups;
import com.getui.push.v2.sdk.dto.req.message.ios.Alert;
import com.getui.push.v2.sdk.dto.req.message.ios.Aps;
import com.getui.push.v2.sdk.dto.req.message.ios.IosDTO;
import com.getui.push.v2.sdk.dto.res.TaskIdDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author lsj
 * @DATE 2021/4/19 0019 10:44
 * @Description:
 */
@Slf4j
public class GtPushApi {

    private static final String appId = "xnXlRXftCn8bXPvLhygFP5";
    private static final String appKey = "HGBwb3bnTK9mGmRMWQe2Q1";
    private static final String masterSecret = "yDSKjzPkHj5yHN6Q5ZZNg";
    private static final String url = "https://restapi.getui.com/v2/";


    //推送api
    public static PushApi getPushApi() {
        ApiHelper apiHelper = getApiHelper();
        // 创建对象，建议复用。目前有PushApi、StatisticApi、UserApi
        PushApi pushApi = apiHelper.creatApi(PushApi.class);
        return pushApi;
    }

    public static UserApi getUserApi() {
        ApiHelper apiHelper = getApiHelper();
        UserApi userApi = apiHelper.creatApi(UserApi.class);
        return userApi;
    }


    public static ApiHelper getApiHelper() {
        GtApiConfiguration apiConfiguration = new GtApiConfiguration();
        //填写应用配置
        apiConfiguration.setAppId(appId);
        apiConfiguration.setAppKey(appKey);
        apiConfiguration.setMasterSecret(masterSecret);
        // 接口调用前缀，请查看文档: 接口调用规范 -> 接口前缀, 可不填写appId
        apiConfiguration.setDomain(url);
        ApiHelper apiHelper = ApiHelper.build(apiConfiguration);
        return apiHelper;
    }

    //绑定别名
    public static void bindAlias(String clientId, String alias) {
        System.out.println(clientId);
        CidAliasListDTO cidAliasListDTO = new CidAliasListDTO();
        cidAliasListDTO.add(new CidAliasListDTO.CidAlias(clientId, alias));
        ApiResult result = getUserApi().bindAlias(cidAliasListDTO);

    }

    //解绑别名
    public static void unbindAlias(String clientId, String alias) {
        CidAliasListDTO cidAliasListDTO = new CidAliasListDTO();
        cidAliasListDTO.add(new CidAliasListDTO.CidAlias(clientId, alias));
        getUserApi().batchUnbindAlias(cidAliasListDTO);
    }

    public static void singlePushAlias(String title, String body, List<String> aliasList) {
        PushDTO<Audience> pushDTO = pushDTO(title, body, aliasList);
        //  fullAlias(pushDTO);
        ApiResult<Map<String, Map<String, String>>> apiResult = getPushApi().pushToSingleByAlias(pushDTO);
        log.info("pushApiResult ===>{}", apiResult);
    }

    /**
     * 创建推送任务
     *
     * @param title
     * @param body
     * @return
     */
    public static String createTask(String title, String body) {
        PushDTO pushDTO = pushDTO(title, body, null);
        ApiResult<TaskIdDTO> result = getPushApi().createMsg(pushDTO);
        return result.getData().getTaskId();
    }

    public static void pushAliasList(List<String> aliasList, String taskId) {
        AudienceDTO audienceDTO = new AudienceDTO();
        audienceDTO.setTaskid(taskId);
        Audience audience = new Audience();
        audience.setAlias(aliasList);
        audienceDTO.setAudience(audience);
        ApiResult<Map<String, Map<String, String>>> apiResult =   getPushApi().pushListByAlias(audienceDTO);
        log.info("pushApiResult ===>{}", apiResult);
    }


    private static Settings getSettings() {
        Settings settings = new Settings();
        Strategy strategy = new Strategy();
        //厂商通道下发策略
        strategy.setDef(1);
        strategy.setIos(2);
        settings.setStrategy(strategy);
        return settings;
    }

    //推送ios
    private static IosDTO getIosDTO(String title, String body) {
        IosDTO iosDTO = new IosDTO();
        Aps aps = new Aps();
        Alert alert = new Alert();
        alert.setTitle(title); //标题
        alert.setBody(body); //内容
        aps.setAlert(alert);
        iosDTO.setAps(aps);
        iosDTO.setAutoBadge("+1");
        return iosDTO;
    }

    //安卓通知消息
    private static AndroidDTO getAndroidDTONotification(String title, String body) {
        //推送安卓
        AndroidDTO androidDTO = new AndroidDTO();
        Ups ups = new Ups();
        ThirdNotification thirdNotification = new ThirdNotification();
        thirdNotification.setBody(body); //推送内容
        thirdNotification.setTitle(title); //推送标题
        thirdNotification.setClickType(CommonEnum.ClickTypeEnum.TYPE_STARTAPP.type);
        ups.setNotification(thirdNotification);
        androidDTO.setUps(ups);
        return androidDTO;
    }

    //安卓透传消息
    private static AndroidDTO getAndroidDTOTransmission(String body) {
        //推送安卓
        AndroidDTO androidDTO = new AndroidDTO();
        Ups ups = new Ups();
        ups.setTransmission(body);
        androidDTO.setUps(ups);
        return androidDTO;
    }

    private static PushDTO pushDTO(String title, String body, List<String> aliasList) {
        PushDTO pushDTO = new PushDTO();
        pushDTO.setRequestId(System.currentTimeMillis() + "");
        pushDTO.setGroupName("g-name1");

        Audience audience = new Audience();
        //推送别名
        if (!Objects.isNull(aliasList)) {
            audience.setAlias(aliasList);
            pushDTO.setAudience(audience);

        }
        pushDTO.setSettings(getSettings());

        PushMessage pushMessage = new PushMessage();
        GTNotification notification = new GTNotification();
        notification.setLogoUrl("https://url"); //图标url
        notification.setTitle(title); //消息标题
        notification.setBody(body); //消息内容
        //intent 打开应用内特定页面
        //url 打开网页地址
        //payload 自定义消息内容启动应用，
        //payload_custom 自定义消息内容不启动应用
        //startapp 打开应用首页
        //none 纯通知，无后续动作
        //notification.setUrl("https//:www.getui.com"); //点击链接
        notification.setClickType(CommonEnum.ClickTypeEnum.TYPE_STARTAPP.type); //点击无反应
        pushMessage.setNotification(notification);
        PushChannel pushChannel = new PushChannel();

        pushChannel.setIos(getIosDTO(title, body));
        pushChannel.setAndroid(getAndroidDTONotification(title, body));

        pushDTO.setPushMessage(pushMessage);
        pushDTO.setPushChannel(pushChannel);
        return pushDTO;
    }


    public static void main(String[] args) {
       // bindAlias("9f24bf9c913d3ac1504d15f82476d32d", "admin_9f24bf9c913d3ac1504d15f82476d32d");
      // bindAlias("86f3de29b2f3db063a536837e4ff9a50", "adminsadadad_9f24bf9c913d3ac1504d15f82476d32d");

       // String taskId = createTask("标题Asss", "内容sss");
//        //unbindAlias("86f3de29b2f3db063a536837e4ff9a50", "lsy");
//        //System.out.println("=================================================================");
        List<String> a = new ArrayList<>();
        //a.add("li|9f24bf9c913d3ac1504d15f82476d32d");
        a.add("li_1627541186512");
       // pushAliasList(a, taskId);
       singlePushAlias("裂开了3", "dddd", a);
    }
}
