package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.PowerBox;
import com.steelman.iot.platform.entity.vo.DeviceCenterVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PowerBoxDao {
    int deleteByPrimaryKey(Long id);

    int insert(PowerBox record);

    int insertSelective(PowerBox record);

    PowerBox selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PowerBox record);

    int updateByPrimaryKey(PowerBox record);

    List<PowerBox> selectBoxByLoopId(@Param("loopId")Long loopId,@Param("parentType") Integer parentType);

    List<PowerBox> selectBoxByTransformerId(Long transformerId);

    List<DeviceCenterVo> selectPowerBoxCenter(Long powerId);

    PowerBox getByBoxNameAndTransformerId(@Param("name") String name, @Param("powerId") Long powerId);

    Long selectCountByPowerId(@Param("powerId") Long powerId);

    List<PowerBox> findByPowerId(@Param("powerId") Long powerId);
}