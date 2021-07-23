package com.mobvoi.ticwatch.base.modules.influxdb.config;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.impl.InfluxDBMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @Project : ticwatch_backend
 * @Package Name : com.mobvoi.ticwatch.base.modules.influxdb.config
 * @Author : xiekun
 * @Desc :
 * @Create Date : 2021年07月23日 22:47
 * ----------------- ----------------- -----------------
 */

@Configuration
public class InfluxDbConfig {

  private static final Logger logger = LoggerFactory.getLogger(InfluxDbConfig.class);

  @Value("${spring.influx.url:''}")
  private String influxDBUrl;

  @Value("${spring.influx.user:''}")
  private String userName;

  @Value("${spring.influx.password:''}")
  private String password;

  @Value("${spring.influx.database:''}")
  private String database;

  @Value("${spring.influx.retentionPolicy:autogen}")
  private String retentionPolicy;

  private static InfluxDB influxDB = null;

  @Bean("influxDbInit")
  public InfluxDB influxDbInit() {
    if (influxDB == null) {
      logger.info("Initializing connection with influxdb " + influxDBUrl + "?dbname=" + database);
      influxDB = InfluxDBFactory.connect(influxDBUrl, userName, password);
      try {
        createDB(influxDB, database);
        influxDB.setDatabase(database);
      } catch (Exception e) {
        logger.error("create controller db failed", e);
      } finally {
        //设置默认策略
        influxDB.setRetentionPolicy(retentionPolicy);
      }
      //设置日志输出级别
      influxDB.setLogLevel(InfluxDB.LogLevel.BASIC);
    }
    return influxDB;
  }

  @Bean("influxDbMapperInit")
  @DependsOn("influxDbInit")
  public InfluxDBMapper initInfluxDbMapper() {
    logger.info("Initializing influxDBMapper");
    return new InfluxDBMapper(influxDB);
  }

  /****
   *  创建数据库
   * @param database
   */
  private void createDB(InfluxDB influxDB, String database) {
    influxDB.query(new Query("CREATE DATABASE " + database));
  }
}
