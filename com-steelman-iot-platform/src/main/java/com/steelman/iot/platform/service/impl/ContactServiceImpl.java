package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.ContactDao;
import com.steelman.iot.platform.entity.Contact;
import com.steelman.iot.platform.service.ContactService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tang
 * date 2020-11-25
 */
@Service("contactService")
public class ContactServiceImpl extends BaseService implements ContactService {
    @Resource
    private ContactDao contactDao;

    public void insertSelective(Contact record) {
        contactDao.insertSelective(record);
    }
    public void update(Contact record) {
        contactDao.updateByPrimaryKeySelective(record);
    }
    public void deleteById(Long id) {
        contactDao.deleteByPrimaryKey(id);
    }
    public Contact findByid(Long id) {
       return contactDao.selectByPrimaryKey(id);
    }
    public List<Contact> selectByContact(Contact record) {
        return contactDao.selectByContact(record);
    }

    @Override
    public Contact selectProjectContact(Long projectId) {
        return contactDao.selectProjectContact(projectId);
    }
}
