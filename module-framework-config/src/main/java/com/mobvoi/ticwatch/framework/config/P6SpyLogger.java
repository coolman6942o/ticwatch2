package com.mobvoi.ticwatch.framework.config;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Project : ticwatch_backend
 * @Package Name : com.mobvoi.ticwatch.framework.config
 * @Author : xiekun
 * @Desc :
 * @Create Date : 2021年07月21日 17:39
 * ----------------- ----------------- -----------------
 */
public class P6SpyLogger implements MessageFormattingStrategy {

  private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

  @Override
  public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql) {
    return !"".equals(sql.trim()) ? this.format.format(new Date()) + " | took " + elapsed + "ms | " + category + " | connection " + connectionId + "\n " + sql + ";" : "";
  }

}
