package com.mobvoi.ticwatch.base.modules.influxdb.service.motion;

import com.mobvoi.ticwatch.framework.domain.influxdb.motion.MotionPoint;
import com.mobvoi.ticwatch.framework.domain.vo.MotionPointVO;
import java.util.List;

/**
 * @Project : ticwatch_backend
 * @Package Name : com.mobvoi.ticwatch.base.modules.influxdb.service.motion
 * @Author : xiekun
 * @Desc :
 * @Create Date : 2021年07月23日 23:12
 * ----------------- ----------------- -----------------
 */
public interface IinfluxdbMotionService {

  void save(List<MotionPointVO> list);

  List<MotionPoint> queryList();

}
