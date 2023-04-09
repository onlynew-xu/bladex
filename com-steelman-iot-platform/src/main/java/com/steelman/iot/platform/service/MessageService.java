package com.steelman.iot.platform.service;

/**
 * @Author lsj
 * @DATE 2021/4/19 0019 9:55
 * @Description:
 */
public interface MessageService {

    void sendTextMail(String toMail,String content,String subject);

    void sendAlarmMsg(String content);

    void sendOfflineMsg(String content);
}
