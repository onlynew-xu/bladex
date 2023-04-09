package com.steelman.iot.platform.service;
import com.steelman.iot.platform.entity.Contact;

import java.util.List;


/**
 * @author tang
 * date 2020-11-23
 */
public interface ContactService {

    void insertSelective(Contact record);
    void update(Contact record);
    void deleteById(Long id);
    Contact findByid(Long id);
    List<Contact> selectByContact(Contact record);

    Contact selectProjectContact(Long projectId);
}
