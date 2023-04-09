package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.Contact;

import java.util.List;

public interface ContactDao {
    int deleteByPrimaryKey(Long id);

    int insert(Contact record);

    int insertSelective(Contact record);

    Contact selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Contact record);

    int updateByPrimaryKey(Contact record);

    List<Contact> selectByContact(Contact record);

    Contact selectProjectContact(Long projectId);
}