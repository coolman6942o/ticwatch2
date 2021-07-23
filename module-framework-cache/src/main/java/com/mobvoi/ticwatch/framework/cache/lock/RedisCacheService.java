//package com.mobvoi.ticwatch.framework.cache.lock;
//
//
//import com.mobvoi.ticwatch.framework.core.utils.StringUtil;
//import java.io.UnsupportedEncodingException;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.Future;
//import java.util.concurrent.FutureTask;
//import java.util.concurrent.TimeUnit;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataAccessException;
//import org.springframework.data.redis.connection.RedisConnection;
//import org.springframework.data.redis.connection.ReturnType;
//import org.springframework.data.redis.core.BoundHashOperations;
//import org.springframework.data.redis.core.ConvertingCursor;
//import org.springframework.data.redis.core.Cursor;
//import org.springframework.data.redis.core.RedisCallback;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ScanOptions;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.stereotype.Service;
//
///**
// * Redis操作方法
// *
// * @author tandy
// */
//@Service
//@SuppressWarnings({"unchecked", "rawtypes"})
//public class RedisCacheService {
//
//  private static final Logger logger = LoggerFactory.getLogger(RedisCacheService.class);
//
//  private static final ConcurrentHashMap<String, Future<String>> LUA_SCRIPT_CACHE = new ConcurrentHashMap<>();
//
//  @Autowired
//  private RedisTemplate redisTemplate;
//
//  private static String redisCode = "utf-8";
//
//
//
//  private String getSha1Promise(RedisConnection jedis, String script) {
//    String sha1Id = null;
//    Future<String> f = LUA_SCRIPT_CACHE.get(script);
//    if (f == null) {
//      FutureTask<String> task = new FutureTask<>(() -> {
//        logger.info("【redis lock】load redis script:\n{}", script);
//        return jedis.scriptLoad(script.getBytes());
//      });
//      Future<String> f1 = LUA_SCRIPT_CACHE.putIfAbsent(script, task);
//      if (f1 == null) {
//        task.run();
//        f = task;
//      } else {
//        f = f1;
//      }
//    }
//    try {
//      sha1Id = f.get();
//    } catch (Throwable e) {
//      logger.info("【redis lock】获取redis script sha1失败", e);
//    }
//    return sha1Id;
//  }
//
//  private Object eval(final RedisConnection jedis, final String script, final int keyCount,
//      final String... params) {
//    long start = 0;
//    if (logger.isDebugEnabled()) {
//      start = System.currentTimeMillis();
//    }
//    Object obj;
//    String sha1Id = getSha1Promise(jedis, script);
//    if (sha1Id != null) {
//      //<T> T evalSha(String scriptSha, ReturnType returnType, int numKeys, byte[]... keysAndArgs);
//      obj = jedis.evalSha(sha1Id, ReturnType.VALUE, keyCount, toByteArray(params));
//    } else {
//      obj = jedis.eval(script.getBytes(), ReturnType.VALUE, keyCount, toByteArray(params));
//    }
//    if (logger.isDebugEnabled()) {
//      logger.info("【redis lock】执行redis script耗时={}", (System.currentTimeMillis() - start));
//    }
//    return obj;
//  }
//
//  private byte[][] toByteArray(String... params) {
//    byte[][] x = new byte[params.length][];
//    for (int i = 0; i < x.length; i++) {
//      x[i] = params[i].getBytes();
//    }
//    return x;
//  }
//
//  public Object eval(final String script, final int keyCount, final String... params) {
//    return redisTemplate
//        .execute((RedisCallback) connection -> eval(connection, script, keyCount, params));
//  }
//
//  public Object eval(final String script) {
//    return redisTemplate.execute((RedisCallback) connection -> eval(connection, script, 0));
//  }
//
//  /**
//   * 执行设置值，如果由于并发导致设置标记位导致设置失败，丢出TransactionExecFailedException异常
//   */
//  public void setTransactionFlag(final String key, final String value, final long expire) {
//    boolean result = (boolean) redisTemplate
//        .execute((RedisCallback) connection -> {
//          if (logger.isDebugEnabled()) {
//            logger.debug("ready to set nx:" + key + ">>>>" + value);
//          }
//          boolean ret = connection.setNX(key.getBytes(), value.getBytes());
//          if (ret) {//防止没获取到锁也能刷新锁的过期时间
//            //默认缓存2天
//            connection.expire(key.getBytes(), expire);
//          }
//          if (logger.isDebugEnabled()) {
//            logger.debug("set nx result:" + ret);
//          }
//          return ret;
//        });
//  }
//
//  /**
//   *
//   */
//
//  public long del(final String... keys) {
//    return (long) redisTemplate.execute((RedisCallback) connection -> {
//      long result = 0;
//      for (int i = 0; i < keys.length; i++) {
//        result = connection.del(keys[i].getBytes());
//      }
//      return result;
//    });
//  }
//
//  /**
//   *
//   */
//  public void set(final byte[] key, final byte[] value, final long seconds) {
//    redisTemplate.execute(new RedisCallback() {
//      @Override
//      public Long doInRedis(RedisConnection connection) throws DataAccessException {
//        connection.set(key, value);
//        if (seconds > 0) {
//          connection.expire(key, seconds);
//        }
//        return 1L;
//      }
//    });
//  }
//
//  /**
//   *
//   */
//  public void expire(final byte[] key, final long seconds) {
//    redisTemplate.execute(new RedisCallback() {
//      @Override
//      public Long doInRedis(RedisConnection connection) throws DataAccessException {
//        if (seconds > 0) {
//          connection.expire(key, seconds);
//        }
//        return 1L;
//      }
//    });
//  }
//
//  /**
//   * 设置值
//   *
//   * @param seconds 过期时间单位为秒
//   */
//  public void set(String key, String value, long seconds) {
//    this.set(key.getBytes(), value.getBytes(), seconds);
//  }
//
//  /**
//   * 设置过期时间
//   *
//   * @param seconds 过期时间单位为秒
//   */
//  public void expire(String key, long seconds) {
//    this.expire(key.getBytes(), seconds);
//  }
//
//  /**
//   *
//   */
//  public void set(String key, String value) {
//
//    this.set(key, value, 0L);
//  }
//
//  /**
//   *
//   */
//  public void set(byte[] key, byte[] value) {
//    this.set(key, value, 0L);
//  }
//
//  /**
//   *
//   */
//  public String get(final String key) {
//    String result = (String) redisTemplate.execute((RedisCallback) connection -> {
//      try {
//        byte[] obj = connection.get(key.getBytes());
//        if (obj != null) {
//          return new String(obj, redisCode);
//        } else {
//          return null;
//        }
//      } catch (Exception e) {
//        logger.error("get cache exception key is:" + key, e);
//      }
//      return null;
//    });
//    logger.debug("get cache value:{}=>{}", key, StringUtil.trimLog(result));
//    return result;
//  }
//
//  public String getAndSet(final String key, final String value) {
//    String result = (String) redisTemplate.boundValueOps(key).getAndSet(value);
//    return result;
//  }
//
//  public String getAndSet(final String key, final String value, long expires) {
//    String result = (String) redisTemplate.boundValueOps(key).getAndSet(value);
//    redisTemplate.expire(key, expires, TimeUnit.SECONDS);
//    return result;
//  }
//
//  /**
//   *
//   */
//  public Set keys(String pattern) {
//    return redisTemplate.keys(pattern);
//  }
//
//  /**
//   *
//   */
//  public boolean exists(final String key) {
//    logger.debug("【REDIS】redis exist :{}", key);
//    Object result = redisTemplate.execute((RedisCallback) connection -> {
//      logger.debug("【REDIS】 redis exist  conn:{}, key:{}", connection, key);
//      return connection.exists(key.getBytes());
//    });
//    logger.debug("【REDIS】 redis exist result: {} -> {}", key, result);
//    if (null == result) {
//      result = false;
//    }
//    return (boolean) result;
//  }
//
//  /**
//   *
//   */
//  public String flushDB() {
//    return (String) redisTemplate.execute(new RedisCallback() {
//      @Override
//      public String doInRedis(RedisConnection connection) throws DataAccessException {
//        connection.flushDb();
//        return "ok";
//      }
//    });
//  }
//
//  /**
//   *
//   */
//  public long dbSize() {
//    return (long) redisTemplate.execute(new RedisCallback() {
//      @Override
//      public Long doInRedis(RedisConnection connection) throws DataAccessException {
//        return connection.dbSize();
//      }
//    });
//  }
//
//  /**
//   *
//   */
//  public String ping() {
//    return (String) redisTemplate.execute(new RedisCallback() {
//      @Override
//      public String doInRedis(RedisConnection connection) throws DataAccessException {
//        return connection.ping();
//      }
//    });
//  }
//
//  /**
//   * 执行设置值，如果由于并发导致设置标记位导致设置失败，丢出TransactionExecFailedException异常
//   */
//  public void setTransactionFlag(final String key, final String value) {
//    this.setTransactionFlag(key, value, 48 * 60 * 60);
//  }
//
//
//  /**
//   * 针对redis incr命令的封装，实现指定key的值自增长
//   *
//   * @param key key值
//   * @return 自增长后的值
//   */
//  public long incr(final String key, Long expire) {
//    long result = (long) redisTemplate
//        .execute(new RedisCallback() {
//          @Override
//          public Object doInRedis(RedisConnection connection)
//              throws DataAccessException {
//            long result = connection.incr(key.getBytes());
//            if (expire != null) {
//              connection.expire(key.getBytes(), expire);
//            }
//            return result;
//          }
//        });
//    return result;
//  }
//
//  /**
//   * 不带过期时间的自增长
//   */
//  public long incr(final String key) {
//    return incr(key, null);
//  }
//
//  /**
//   * 针对redis INCRBY，实现指定key的值的增长
//   *
//   * @param key key值
//   * @param incr 增长的值
//   * @return 自增长后的值
//   */
//  public long incrBy(final String key, Long incr) {
//    long result = (long) redisTemplate
//        .execute(new RedisCallback() {
//          @Override
//          public Object doInRedis(RedisConnection connection)
//              throws DataAccessException {
//            return connection.incrBy(key.getBytes(), incr);
//          }
//
//        });
//    return result;
//  }
//
//  /**
//   * 获取缓存的值，之后迅速删除掉
//   *
//   * @param key 缓存key
//   * @return 返回指定key对应的值
//   */
//  public String getAndRemove(final String key) {
//    return (String) redisTemplate.execute(new RedisCallback() {
//      @Override
//      public String doInRedis(RedisConnection connection) throws DataAccessException {
//        try {
//          byte[] obj = connection.get(key.getBytes());
//          if (obj != null) {
//            connection.del(key.getBytes());
//            return new String(obj, redisCode);
//          } else {
//            return null;
//          }
//        } catch (UnsupportedEncodingException e) {
//          logger.error("不支持的编码转换", e);
//        }
//        return null;
//      }
//    });
//  }
//
//  /**
//   *
//   */
//  public void zrem(final String key, final String value) {
//    redisTemplate.opsForZSet().remove(key, value);
//  }
//
//  public void zrem(String key, Object... array) {
//    redisTemplate.opsForZSet().remove(key, array);
//
//  }
//
//  public long zsize(String key) {
//    return redisTemplate.opsForZSet().size(key);
//  }
//
//  public Set zReverseRange(final String key, final long start, final long end) {
//    return redisTemplate.opsForZSet().reverseRange(key, start, end);
//  }
//
//  public Set zRange(final String key, final long start, final long end) {
//    return redisTemplate.opsForZSet().range(key, start, end);
//  }
//
//  public Set zRangeScore(final String key, final long min, final long max) {
//    return redisTemplate.opsForZSet().rangeByScore(key, min, max);
//  }
//
//  public void zadd(final String key, final String value, double score) {
//    redisTemplate.opsForZSet().add(key, value, score);
//  }
//
//  public Double zScore(final String key, final String value) {
//    return redisTemplate.opsForZSet().score(key, value);
//  }
//
//  public Long zCount(final String key, final double score1, double score2) {
//    return redisTemplate.opsForZSet().count(key, score1, score2);
//  }
//
//  public void sremove(final String key, final String... value) {
//    redisTemplate.opsForSet().remove(key, value);
//  }
//
//  public void sadd(final String key, final Object... values) {
//    redisTemplate.opsForSet().add(key, values);
//  }
//
//  public Set smembers(final String key) {
//    Set set = redisTemplate.opsForSet().members(key);
//    return set;
//  }
//
//  public boolean sismember(final String key, final String value) {
//    return redisTemplate.opsForSet().isMember(key, value);
//  }
//
//  public long ssize(final String key) {
//    return redisTemplate.opsForSet().size(key);
//  }
//
//  public Map hgetAll(final String key) {
//    return redisTemplate.opsForHash().entries(key);
//  }
//
//  public Object hget(final String key, final String field) {
//    return redisTemplate.opsForHash().get(key, field);
//  }
//
//  public void hputAll(final String key, final Map<String, String> map) {
//    redisTemplate.opsForHash().putAll(key, map);
//  }
//
//  public void hput(final String key, final String field, final String value) {
//    redisTemplate.opsForHash().put(key, field, value);
//  }
//
//  public boolean hputIfAbsent(final String key, final String field, final String value) {
//    return redisTemplate.opsForHash().putIfAbsent(key, field, value);
//  }
//
//  public void hdel(final String key, final String... fields) {
//    redisTemplate.opsForHash().delete(key, fields);
//  }
//
//  public BoundHashOperations getHashOps(String key) {
//    return redisTemplate.boundHashOps(key);
//  }
//
//
//  @SuppressWarnings("unchecked")
//  public Cursor<String> scan(String pattern, int limit) {
//    ScanOptions options = ScanOptions.scanOptions().match(pattern).count(limit).build();
//    RedisSerializer<String> redisSerializer = (RedisSerializer<String>) redisTemplate
//        .getKeySerializer();
//    return (Cursor) redisTemplate.executeWithStickyConnection(new RedisCallback() {
//      @Override
//      public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
//        return new ConvertingCursor<>(redisConnection.scan(options), redisSerializer::deserialize);
//      }
//    });
//  }
//}
