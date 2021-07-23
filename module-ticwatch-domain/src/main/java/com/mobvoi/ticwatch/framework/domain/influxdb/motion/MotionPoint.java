package com.mobvoi.ticwatch.framework.domain.influxdb.motion;

import java.util.concurrent.TimeUnit;
import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;
import org.influxdb.annotation.TimeColumn;

/**
 * @Project : ticwatch
 * @Package Name : com.mobvoi.ticwatch.framework.domain.influxdb
 * @Author : xiekun
 * @Desc : 在InfluxDB中建立存储策略  CREATE RETENTION POLICY "rp_order_payment" ON "db_order" DURATION 30d REPLICATION 1 DEFAULT
 * @Create Date : 2021年07月14日 22:56
 * ----------------- ----------------- -----------------
 */
@Data
@Measurement(name = "tic_sensor", database = "ticwatch", retentionPolicy = "autogen", timeUnit = TimeUnit.MILLISECONDS)
public class MotionPoint {

  /**
   * Column中的name为measurement中的列名
   * 此外,需要注意InfluxDB中时间戳均是以UTC时区保存,在保存以及提取过程中需要注意时区转换
   */
  @TimeColumn
  @Column(name = "time")
  private String time;

  /**
   * 注解中添加tag = true,表示当前字段内容为tag内容,InfluxDB中Tag只能是字符串类型
   */
  @Column(name = "t_wwid", tag = true)
  private String tWwid;

  @Column(name = "t_account_id", tag = true)
  private String tAccountId;

  @Column(name = "t_data_type", tag = true)
  private String tDataType;

  @Column(name = "t_data_source_name", tag = true)
  private String tDataSourceName;

  @Column(name = "t_start_time")
  private long tStartTime;

  @Column(name = "t_end_time")
  private long tEndTime;

  @Column(name = "f_value")
  private int fValue;

  @Column(name = "f_value2")
  private double fValue2;

  @Column(name = "f_value3")
  private double fValue3;
}