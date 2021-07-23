package com.mobvoi.ticwatch.base.modules.influxdb.service.motion.impl;

import com.mobvoi.ticwatch.base.modules.influxdb.config.InfluxDbUtils;
import com.mobvoi.ticwatch.base.modules.influxdb.service.motion.IinfluxdbMotionService;
import com.mobvoi.ticwatch.framework.domain.influxdb.motion.MotionPoint;
import com.mobvoi.ticwatch.framework.domain.vo.MotionPointVO;
import java.util.List;
import java.util.stream.Collectors;
import org.influxdb.InfluxDB;
import org.influxdb.impl.InfluxDBMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Project : ticwatch_backend
 * @Package Name : com.mobvoi.ticwatch.base.modules.influxdb.service
 * @Author : xiekun
 * @Desc :
 * @Create Date : 2021年07月23日 22:36
 * ----------------- ----------------- -----------------
 */

@Service
public class InfluxdbMotionServiceImpl implements IinfluxdbMotionService {

  @Autowired
  private InfluxDB influxDb;

  @Autowired
  private InfluxDBMapper influxDbMapper;

  @Autowired
  private InfluxDbUtils influxDbUtils;

  @Override
  public void save(List<MotionPointVO> voList) {
    List<MotionPoint> list = voList.stream().map(InfluxdbMotionServiceImpl::apply)
        .collect(Collectors.toList());
//    list.forEach(t -> influxDbMapper.save(t));
    influxDbUtils.writeBatchByList(list);
  }

  @Override
  public List<MotionPoint> queryList() {
//    int pageNo = 1;
//    int pageSize = 10;
//    // InfluxDB支持分页查询,因此可以设置分页查询条件
//    String pageQuery = " LIMIT " + pageSize + " OFFSET " + (pageNo - 1) * pageSize;
//    //查询条件暂且为空
//    String queryCondition = "";
//    // 此处查询所有内容,如果
//    String queryCmd = "SELECT * FROM tic_sensor"
//        // 查询指定设备下的日志信息
//        // 要指定从 RetentionPolicyName.measurement中查询指定数据,默认的策略可以不加；
//        // + 策略name + "." + measurement
//        // 添加查询条件(注意查询条件选择tag值,选择field数值会严重拖慢查询速度)
//        + queryCondition
//        // 查询结果需要按照时间排序
//        + " ORDER BY time DESC"
//        // 添加分页查询条件
//        + pageQuery;
//    List<Object> objects = influxDbUtils.fetchRecords(queryCmd);

    List<MotionPoint> list = influxDbMapper.query(MotionPoint.class);
    return list;
  }

  private static MotionPoint apply(MotionPointVO motionPointVO) {
    MotionPoint motionPoint = new MotionPoint();
//    motionPoint.setTime(motionPointVO.getTime());
    motionPoint.setTWwid(motionPointVO.getTWwid());
    motionPoint.setTAccountId(motionPointVO.getTAccountId());
    motionPoint.setTDataType(motionPointVO.getTDataType());
    motionPoint.setTDataSourceName(motionPointVO.getTDataSourceName());
    motionPoint.setTStartTime(motionPointVO.getTStartTime());
    motionPoint.setTEndTime(motionPointVO.getTEndTime());
    motionPoint.setFValue(motionPointVO.getFValue());
    motionPoint.setFValue2(motionPointVO.getFValue2());
    motionPoint.setFValue3(motionPointVO.getFValue3());
    return motionPoint;
  }


}
