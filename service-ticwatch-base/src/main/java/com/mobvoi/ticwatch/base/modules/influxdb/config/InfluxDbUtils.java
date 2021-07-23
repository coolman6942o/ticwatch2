package com.mobvoi.ticwatch.base.modules.influxdb.config;

import static org.influxdb.querybuilder.BuiltQuery.QueryBuilder.select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Project : influxdb
 * @Package Name : com.mdl.influxdb.config
 * @Author : xiekun
 * @Desc : 查询语句参考 https://github.com/influxdata/influxdb-java/blob/master/QUERY_BUILDER.md
 * 参考 ：https://blog.csdn.net/x541211190/article/details/83216589
 * 参考 ：https://github.com/influxdata/influxdb-java/blob/master/INFLUXDB_MAPPER.md
 * 参考 ：https://github.com/influxdata/influxdb-client-java/tree/master/client#influxdb-client-java
 * @Create Date : 2021年06月15日 17:39
 * ----------------- ----------------- -----------------
 */

@Component
public class InfluxDbUtils {

  private static final Logger logger = LoggerFactory.getLogger(InfluxDbUtils.class);

  @Value("${spring.influx.database:''}")
  private String database;

  @Value("${spring.influx.retentionPolicy:'autogen'}")
  private String retentionPolicy;

  @Autowired
  private InfluxDB influxDb;

  @Autowired
  private InfluxDBMapper influxDbMapper;


  /**
   * 测试连接是否正常
   *
   * @return true 正常
   */
  public boolean ping() {
    boolean isConnected = false;
    Pong pong;
    try {
      pong = influxDb.ping();
      if (pong != null) {
        isConnected = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return isConnected;
  }

  /**
   * 创建自定义保留策略
   *
   * @param policyName 策略名
   * @param duration 保存天数
   * @param replication 保存副本数量
   * @param isDefault 是否设为默认保留策略
   */
  public void createRetentionPolicy(String policyName, String duration, int replication,
      Boolean isDefault) {
    String sql = String
        .format("CREATE RETENTION POLICY \"%s\" ON \"%s\" DURATION %s REPLICATION %s ", policyName,
            database, duration, replication);
    if (isDefault) {
      sql = sql + " DEFAULT";
    }
    this.query(sql);
  }

  /**
   * 创建默认的保留策略
   *
   * RetentionPolicy：default，保存天数：30天，保存副本数量：1 设为默认保留策略
   */
  public void createDefaultRetentionPolicy() {
    String command = String
        .format("CREATE RETENTION POLICY \"%s\" ON \"%s\" DURATION %s REPLICATION %s DEFAULT",
            "default", database, "30d", 1);
    this.query(command);
  }

  /**
   * 查询
   *
   * @param command 查询语句
   */
  public QueryResult query(String command) {
    return influxDb.query(new Query(command, database));
  }


  /**
   * 写单条数据
   */
  public void writeOne(Object obj) {
    Point point = Point.measurementByPOJO(obj.getClass())
        .addFieldsFromPOJO(obj)
        .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
        .build();

    BatchPoints batchPoints = BatchPoints
        .database(database)
        .consistency(InfluxDB.ConsistencyLevel.ALL)
        .build();

    logger.info("Write metrics:: point: " + point);
    batchPoints.point(point);
    influxDb.write(batchPoints);
  }

  /**
   * 写单条数据
   * @param obj
   */
  public void saveOne(Object obj) {
    influxDbMapper.save(obj);
  }


  /**
   * 批量写入
   */
  public void writeBatchByList(List<?> list) {
    BatchPoints batchPoints = BatchPoints
        .database(database)
        .consistency(InfluxDB.ConsistencyLevel.ALL)
        .build();

    list.forEach(obj -> {
      Point point = Point.measurementByPOJO(obj.getClass())
          .addFieldsFromPOJO(obj)
          .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
          .build();
      batchPoints.point(point);
    });
    influxDb.write(batchPoints);
  }


  /**
   * 手动拼接写入
   */
  public void writeMap(String measurement, Map<String, String> tags, Map<String, Object> fields) {
    BatchPoints batchPoints = BatchPoints
        .database(database)
        .consistency(InfluxDB.ConsistencyLevel.ALL)
        .retentionPolicy(retentionPolicy)
        .build();
    Point point = Point.measurement(measurement)
        .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
        .tag(tags)
        .fields(fields)
        .build();
    logger.info("Write metrics:: tags: " + getTags(tags) + " fields: " + getFields(fields));
    batchPoints.point(point);
    influxDb.write(batchPoints);
  }


  public String getTags(Map<String, String> tags) {
    String tags_ = "";
    Set set = tags.entrySet();
    Iterator iterator = set.iterator();
    while (iterator.hasNext()) {
      Map.Entry mentry = (Map.Entry) iterator.next();
      tags_ = tags_ + " " + mentry.getKey() + ": " + mentry.getValue();
    }
    return tags_;
  }

  public String getFields(Map<String, Object> fields) {
    String fields_ = "";
    Set set = fields.entrySet();
    Iterator iterator = set.iterator();
    while (iterator.hasNext()) {
      Map.Entry mentry = (Map.Entry) iterator.next();
      fields_ = fields_ + " " + mentry.getKey() + ": " + mentry.getValue();
    }
    return fields_;
  }

  public Query query(String database, String table) {
    Query query = select().from(database, table);
    return query;
  }

  /**********************************************************************************/


  /**
   * 查询，返回Map集合
   *
   * @param query 完整的查询语句
   */
  public List<Object> fetchRecords(String query) {
    List<Object> results = new ArrayList<>();
    QueryResult queryResult = influxDb.query(new Query(query, database));
    queryResult.getResults().forEach(result -> {
      if (result.getSeries() != null) {
        result.getSeries().forEach(serial -> {
          if (serial != null) {
            List<String> columns = serial.getColumns();
            int fieldSize = columns.size();
            serial.getValues().forEach(value -> {
              Map<String, Object> obj = new HashMap<>(16);
              for (int i = 0; i < fieldSize; i++) {
                obj.put(columns.get(i), value.get(i));
              }
              results.add(obj);
            });
          }
        });
      }
    });
    return results;
  }

//  /**
//   * 查询，返回对象的list集合
//   */
//  @SuppressWarnings({"rawtypes", "unchecked"})
//  public <T> List<T> fetchResults(String query, Class<?> clasz) {
//    List results = new ArrayList<>();
//    Map keyMap = getAnnotationAndColumnName(clasz);
//    QueryResult queryResult = influxDb.query(new Query(query, database));
//    queryResult.getResults().forEach(result -> {
//      if (result.getSeries() != null) {
//        result.getSeries().forEach(serial -> {
//          if (serial != null) {
//            List<String> columns = serial.getColumns();
//            int fieldSize = columns.size();
//            serial.getValues().forEach(value -> {
//              Object obj = null;
//              try {
//                obj = clasz.newInstance();
//                for (int i = 0; i < fieldSize; i++) {
//                  String fieldAnnotationName = columns.get(i);
//                  String fieldName = keyMap.get(fieldAnnotationName).toString();
//                  Field field = clasz.getDeclaredField(fieldName);
//                  field.setAccessible(true);
//                  Class<?> type = field.getType();
//                  if (type == int.class) {
//                    field.set(obj, Integer.valueOf(value.get(i).toString()));
//                  } else if (type == float.class) {
//                    field.set(obj, Float.valueOf(value.get(i).toString()));
//                  } else if (type == double.class) {
//                    field.set(obj, Double.valueOf(value.get(i).toString()));
//                  } else {
//                    field.set(obj, value.get(i));
//                  }
//                }
//              } catch (NoSuchFieldException | SecurityException | InstantiationException | IllegalAccessException e) {
//                e.printStackTrace();
//              }
//              results.add(obj);
//            });
//          }
//        });
//      }
//    });
//    return results;
//  }
//
//
//  /**
//   * 获取实体类 @Column 的注解名和属性名的map映射
//   */
//  public static Map getAnnotationAndColumnName(Class clazz) {
//    Map map = new ConcurrentHashMap<>();
//    Field[] fields = clazz.getDeclaredFields();
//    for (Field field : fields) {
//      if (field.isAnnotationPresent(Column.class)) {
//        Column declaredAnnotation = field.getDeclaredAnnotation(Column.class);
//        String column = declaredAnnotation.name();
//        map.put(column, field.getName());
//      }
//    }
//    return map;
//  }
//
//
//  /**
//   * 查询，返回map集合
//   *
//   * @param fieldKeys 查询的字段，不可为空；不可为单独的tag
//   * @param measurement 度量，不可为空；
//   */
//  public List<Object> fetchRecords(String fieldKeys, String measurement) {
//    StringBuilder query = new StringBuilder();
//    query.append("select ").append(fieldKeys).append(" from ").append(measurement);
//    return this.fetchRecords(query.toString());
//  }
//
//  /**
//   * 查询，返回map集合
//   *
//   * @param fieldKeys 查询的字段，不可为空；不可为单独的tag
//   * @param measurement 度量，不可为空；
//   */
//  public List<Object> fetchRecords(String fieldKeys, String measurement, String order) {
//    StringBuilder query = new StringBuilder();
//    query.append("select ").append(fieldKeys).append(" from ").append(measurement);
//    query.append(" order by ").append(order);
//    return this.fetchRecords(query.toString());
//  }
//
//  /**
//   * 查询，返回map集合
//   *
//   * @param fieldKeys 查询的字段，不可为空；不可为单独的tag
//   * @param measurement 度量，不可为空；
//   */
//  public List<Object> fetchRecords(String fieldKeys, String measurement, String order,
//      String limit) {
//    StringBuilder query = new StringBuilder();
//    query.append("select ").append(fieldKeys).append(" from ").append(measurement);
//    query.append(" order by ").append(order);
//    query.append(limit);
//    return this.fetchRecords(query.toString());
//  }
//
//
//  /**
//   * 查询，返回对象的list集合
//   */
//  public <T> List<T> fetchResults(String fieldKeys, String measurement, Class<?> clasz) {
//    StringBuilder query = new StringBuilder();
//    query.append("select ").append(fieldKeys).append(" from ").append(measurement);
//    return this.fetchResults(query.toString(), clasz);
//  }
//
//  /**
//   * 查询，返回对象的list集合
//   */
//  public <T> List<T> fetchResults(String fieldKeys, String measurement, String order,
//      Class<?> clasz) {
//    StringBuilder query = new StringBuilder();
//    query.append("select ").append(fieldKeys).append(" from ").append(measurement);
//    query.append(" order by ").append(order);
//    return this.fetchResults(query.toString(), clasz);
//  }
//
//  /**
//   * 查询，返回对象的list集合
//   */
//  public <T> List<T> fetchResults(String fieldKeys, String measurement, String order, String limit,
//      Class<?> clasz) {
//    StringBuilder query = new StringBuilder();
//    query.append("select ").append(fieldKeys).append(" from ").append(measurement);
//    query.append(" order by ").append(order);
//    query.append(limit);
//    return this.fetchResults(query.toString(), clasz);
//  }


}
