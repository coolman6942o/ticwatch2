package com.mobvoi.ticwatch.appvpa.service.ticwatch;


import com.mobvoi.ticwatch.framework.domain.entitys.ticwatch.TbMotionRecordsEntity;
import com.mobvoi.ticwatch.framework.domain.influxdb.motion.MotionPoint;
import com.mobvoi.ticwatch.framework.domain.vo.MotionPointVO;
import java.util.List;

/**
 * @Project : ticwatch_backend
 * @Package Name : com.mobvoi.ticwatch.appvpa.service
 * @Author : xiekun
 * @Desc :
 * @Create Date : 2021年07月22日 21:20
 * ----------------- ----------------- -----------------
 */
public interface SMotionRecordsService {

  List<TbMotionRecordsEntity> list(String accountId);

  List<TbMotionRecordsEntity> getList(String accountId);

  void save(List<MotionPointVO> list);

  List<MotionPoint> queryList();

}
