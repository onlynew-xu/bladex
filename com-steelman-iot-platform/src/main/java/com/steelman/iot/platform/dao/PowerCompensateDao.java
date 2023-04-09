package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.PowerCompensate;
import com.steelman.iot.platform.entity.vo.DeviceCenterVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PowerCompensateDao {
    int deleteByPrimaryKey(Long id);

    int insert(PowerCompensate record);

    int insertSelective(PowerCompensate record);

    PowerCompensate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PowerCompensate record);

    int updateByPrimaryKey(PowerCompensate record);

    List<PowerCompensate> selectByTransformerId( Long transformerId);

    Long selectCountByPowerId(Long powerId);

    List<DeviceCenterVo> selectPowerCompensateCenter(Long powerId);

    Long selectCountByTransformId(Long transformId, Long projectId);

    PowerCompensate selectByTransformerAndName(@Param("compensateName") String compensateName, @Param("transformerId") Long transformerId);

    List<PowerCompensate> findByPowerId(@Param("powerId") Long powerId);
}