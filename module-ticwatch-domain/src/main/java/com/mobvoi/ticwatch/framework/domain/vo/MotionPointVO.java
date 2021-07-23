package com.mobvoi.ticwatch.framework.domain.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @Project : ticwatch_backend
 * @Package Name : com.mobvoi.ticwatch.framework.domain.vo
 * @Author : xiekun
 * @Desc : InfluxDB中Tag只能是字符串类型，tag有索引，fields没有索引
 * @Create Date : 2021年07月23日 23:39
 * ----------------- ----------------- -----------------
 */

@Builder
@Data
public class MotionPointVO {

  private String time;

  private String tWwid;

  private String tAccountId;

  private String tDataType;

  private String tDataSourceName;

  private long tStartTime;

  private long tEndTime;

  private String fKey;

  private int fValue;

  private float fValue2;

  private double fValue3;

}
