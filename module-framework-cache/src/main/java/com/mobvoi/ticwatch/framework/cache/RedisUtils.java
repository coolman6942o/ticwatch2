//package com.mobvoi.ticwatch.framework.cache;
//
//import com.mobvoi.ticwatch.framework.core.utils.StringUtil;
//import java.io.Serializable;
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.ListOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.SetOperations;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.data.redis.core.ZSetOperations;
//import org.springframework.data.redis.support.atomic.RedisAtomicLong;
//import org.springframework.stereotype.Component;
//
//@Component
//public class RedisUtils {
//
//  private static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);
//
//  @Autowired
//  private RedisTemplate redisTemplate;
//
//  /**
//   * 加锁
//   *
//   * @param key 被秒杀商品的id
//   * @param expire 当前线程操作时的 System.currentTimeMillis() + 2000，2000是超时时间，这个地方不需要去设置redis的expire，
//   * 也不需要超时后手动去删除该key，因为会存在并发的线程都会去删除，造成上一个锁失效，结果都获得锁去执行，并发操作失败了就。
//   */
//  public boolean lock(String key, Long expire) {
//    String expireTime = String.valueOf(expire);
//    long systemCurrentTimeMills = System.currentTimeMillis();
//    logger.debug("【redis lock】ready to get redis lock,key:{},expireTime:{},systemCurrentTimeMills:{}",
//            key, expireTime, systemCurrentTimeMills);
//    //这个其实就是setnx命令，判断是否存在并有值，存在返回false，不存在返回true
//    boolean result = redisTemplate.opsForValue().setIfAbsent(key, expireTime);
//    if (result) {
//      return true;
//    }
//    //获取key的值，判断是是否超时
//    String curVal = (String) redisTemplate.opsForValue().get(key);
//    logger.debug("【redis lock】get lock value : {} => {}", key, curVal);
//
//    //如果锁过期了
//    if (StringUtil.isNotEmpty(curVal) && Long.parseLong(curVal) < systemCurrentTimeMills) {
//      //获得之前的key值，同时设置当前的传入的value。这个地方可能几个线程同时过来，但是redis本身天然是单线程的，所以getAndSet方法还是会安全执行，
//      //首先执行的线程，此时curVal当然和oldVal值相等，因为就是同一个值，之后该线程set了自己的value，后面的线程就取不到锁了
//
//      //锁已经过期了，设置新的过期时间，并返回上一个锁设置的过期时间
//      String oldVal = (String) redisTemplate.opsForValue().getAndSet(key, expireTime);
//
//      // 只会让一个线程拿到锁，
//      // 如果oldVal和curVal相等，说明是同一个线程，加锁成功；
//      // 如果oldVal和curVal不相等，说明线程A在getset的时候被另外一个线程B抢先加锁了
//      if (StringUtil.isNotEmpty(oldVal) && oldVal.equals(curVal)) {
//        return true;
//      }
//    }
//    logger.debug("【redis lock】get lock failed : {} => {}", key, expireTime);
//    return false;
//  }
//
//  /**
//   * 解锁
//   */
//  public void unlock(String key, Long expire) {
//    try {
//      String curValue = (String) redisTemplate.opsForValue().get(key);
//      logger.debug("【redis lock】unlock the redis lock key:{},curValue:{},expireTime:{}", key,
//          curValue, expire);
//      if (StringUtil.isNotEmpty(curValue) && curValue.equals(String.valueOf(expire))) {
//        logger.debug("【redis lock】delete the redis lock : {}", key);
//        redisTemplate.opsForValue().getOperations().delete(key);
//      }
//    } catch (Exception e) {
//      logger.error("【redis lock】unlock redis lock failed:" + key, e);
//    }
//  }
//
//  /**
//   * 写入缓存
//   */
//  public boolean set(final String key, Object value) {
//    boolean result = false;
//    try {
//      ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
//      operations.set(key, value);
//      result = true;
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    return result;
//  }
//
//  /**
//   * 写入缓存设置时效时间
//   */
//  public boolean set(final String key, Object value, Long expireTime, TimeUnit timeUnit) {
//    boolean result = false;
//    try {
//      ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
//      operations.set(key, value);
//      redisTemplate.expire(key, expireTime, timeUnit);
//      result = true;
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    return result;
//  }
//
//  /**
//   * 批量删除对应的value
//   */
//  public void remove(final String... keys) {
//    for (String key : keys) {
//      remove(key);
//    }
//  }
//
//  /**
//   * 批量删除key
//   */
//  public void removePattern(final String pattern) {
//    Set<Serializable> keys = redisTemplate.keys(pattern);
//    if (keys.size() > 0) {
//      redisTemplate.delete(keys);
//    }
//  }
//
//  /**
//   * 删除对应的value
//   */
//  public void remove(final String key) {
//    if (exists(key)) {
//      redisTemplate.delete(key);
//    }
//  }
//
//  /**
//   * 判断缓存中是否有对应的value
//   */
//  public boolean exists(final String key) {
//    return redisTemplate.hasKey(key);
//  }
//
//  /**
//   * 读取缓存
//   */
//  public Object get(final String key) {
//    Object result = null;
//    ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
//    result = operations.get(key);
//    return result;
//  }
//
//  /**
//   * 哈希 添加
//   */
//  public void hmSet(String key, Object hashKey, Object value) {
//    HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
//    hash.put(key, hashKey, value);
//  }
//
//  /**
//   * 哈希获取数据
//   */
//  public Object hmGet(String key, Object hashKey) {
//    HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
//    return hash.get(key, hashKey);
//  }
//
//  /**
//   * 列表添加
//   */
//  public void lPush(String k, Object v) {
//    ListOperations<String, Object> list = redisTemplate.opsForList();
//    list.rightPush(k, v);
//  }
//
//  /**
//   * 列表获取
//   */
//  public List<Object> lRange(String k, long l, long l1) {
//    ListOperations<String, Object> list = redisTemplate.opsForList();
//    return list.range(k, l, l1);
//  }
//
//  /**
//   * 集合添加
//   */
//  public void add(String key, Object value) {
//    SetOperations<String, Object> set = redisTemplate.opsForSet();
//    set.add(key, value);
//  }
//
//  /**
//   * 集合获取
//   */
//  public Set<Object> setMembers(String key) {
//    SetOperations<String, Object> set = redisTemplate.opsForSet();
//    return set.members(key);
//  }
//
//  /**
//   * 有序集合添加
//   */
//  public void zAdd(String key, Object value, double scoure) {
//    ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
//    zset.add(key, value, scoure);
//  }
//
//  /**
//   * 有序集合获取
//   */
//  public Set<Object> rangeByScore(String key, double scoure, double scoure1) {
//    ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
//    return zset.rangeByScore(key, scoure, scoure1);
//  }
//
//
//  public static void main(String[] args) {
//    RedisUtils utils = new RedisUtils();
//    utils.getIncrId();
//  }
//
//
//  /**
//   * RedisTemplate序列号自增id（当前日期+序列号)
//   */
//  public String getIncrId() {
//    String formatDate = getIncrDate();
//    String key = getIncrKey();
//    Long incr = getIncr(key, getCurrent2TodayEndMillisTime());
//    if (incr == 0) {
//      //从001开始
//      incr = getIncr(key, getCurrent2TodayEndMillisTime());
//    }
//    //三位序列号
//    DecimalFormat df = new DecimalFormat("000");
//    return formatDate.concat(df.format(incr));
//  }
//
//
//  /**
//   * 获取自增的key
//   */
//  private String getIncrKey() {
//    return "key" + getIncrDate();
//  }
//
//  /**
//   * 获取自增的日期
//   */
//  public String getIncrDate() {
//    SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
//    Date date = new Date();
//    return sdf.format(date);
//  }
//
//
//  public Long getIncr(String key, long liveTime) {
//    RedisAtomicLong entityIdCounter = new RedisAtomicLong(key,
//        redisTemplate.getConnectionFactory());
//    Long increment = entityIdCounter.getAndIncrement();
//    //初始设置过期时间
//    boolean flag = (null == increment || increment.longValue() == 0) && liveTime > 0;
//    if (flag) {
//      //单位毫秒
//      entityIdCounter.expire(liveTime, TimeUnit.MILLISECONDS);
//    }
//    return increment;
//  }
//
//  /**
//   * 现在到今天结束的毫秒数
//   */
//  public Long getCurrent2TodayEndMillisTime() {
//    Calendar todayEnd = Calendar.getInstance();
//    // Calendar.HOUR 12小时制
//    // HOUR_OF_DAY 24小时制
//    todayEnd.set(Calendar.HOUR_OF_DAY, 23);
//    todayEnd.set(Calendar.MINUTE, 59);
//    todayEnd.set(Calendar.SECOND, 59);
//    todayEnd.set(Calendar.MILLISECOND, 999);
//    return todayEnd.getTimeInMillis() - System.currentTimeMillis();
//  }
//
//
//}
