package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.PowerCompensateComponents;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PowerCompensateComponentsDao {
    int deleteByPrimaryKey(Long id);

    int insert(PowerCompensateComponents record);

    int insertSelective(PowerCompensateComponents record);

    PowerCompensateComponents selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PowerCompensateComponents record);

    int updateByPrimaryKey(PowerCompensateComponents record);

    List<PowerCompensateComponents> selectByCompensateId(Long compensateId);
}