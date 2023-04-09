package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.PowerTransformer;
import com.steelman.iot.platform.entity.vo.DeviceCenterVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PowerTransformerDao {
    int deleteByPrimaryKey(Long id);

    int insert(PowerTransformer record);

    int insertSelective(PowerTransformer record);

    PowerTransformer selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PowerTransformer record);

    int updateByPrimaryKey(PowerTransformer record);

    List<PowerTransformer> selectByPowerId(Long powerId);

    void deleteByPowerId(Long id);

    List<DeviceCenterVo> selectPowerTransformerCenter(Long powerId);

    PowerTransformer selectByTransformNameAndPowerId(@Param("name") String name, @Param("powerId") Long powerId);

    int updateRelationStatus(@Param("transformerId") Long transformerId, @Param("status") Integer status, @Param("updateTime") Date updateTime);

    List<PowerTransformer> getUnBandingTransformer(Long powerId);
}