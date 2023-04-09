package com.steelman.iot.platform.activemq;

import com.steelman.iot.platform.service.MessageService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tang
 * date 2020-12-24
 */
@Component
public class ActiveMQConsumer {

    @Resource
    private MessageService messageService;

    @JmsListener(destination = "${spring.activemq.alarmQueue}")
    public void receiveAlarmQueue(String text) {
        System.out.println("Consumer收到的报文为:"+text);
        messageService.sendAlarmMsg(text);
        System.out.println("=================");
    }
    @JmsListener(destination = "${spring.activemq.offlineQueue}")
    public void receiveOfflineQueue(String text) {
        System.out.println("Consumer收到的报文为:"+text);
        messageService.sendOfflineMsg(text);
        System.out.println("=================");
    }
}
