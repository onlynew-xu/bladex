package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.PowerFeederLoop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PowerFeederLoopDao {
    int deleteByPrimaryKey(Long id);

    int insert(PowerFeederLoop record);

    int insertSelective(PowerFeederLoop record);

    PowerFeederLoop selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PowerFeederLoop record);

    int updateByPrimaryKey(PowerFeederLoop record);

    List<PowerFeederLoop> selectLoopByFeederId( Long feederId);

    PowerFeederLoop findByFeederIdAndLoop(@Param("feederId") Long feederId,@Param("loopName") String loopName);

    int deleteByFeederId(@Param("feederId") Long feederId);

    List<PowerFeederLoop> getLoopList(@Param("feederId") Long feederId);
}