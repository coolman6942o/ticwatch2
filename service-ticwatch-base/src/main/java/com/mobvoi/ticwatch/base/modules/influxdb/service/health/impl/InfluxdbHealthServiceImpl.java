package com.mobvoi.ticwatch.base.modules.influxdb.service.health.impl;

import org.springframework.stereotype.Service;

/**
 * @Project : ticwatch_backend
 * @Package Name : com.mobvoi.ticwatch.base.modules.influxdb.service.health.impl
 * @Author : xiekun
 * @Desc :
 * @Create Date : 2021年07月23日 23:13
 * ----------------- ----------------- -----------------
 */

@Service
public class InfluxdbHealthServiceImpl {

  private static InfluxdbHealthServiceImpl ourInstance = new InfluxdbHealthServiceImpl();

  public static InfluxdbHealthServiceImpl getInstance() {
    return ourInstance;
  }

  private InfluxdbHealthServiceImpl() {
  }

}
