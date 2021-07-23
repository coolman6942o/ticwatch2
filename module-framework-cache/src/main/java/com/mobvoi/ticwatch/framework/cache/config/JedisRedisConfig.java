//package com.mobvoi.ticwatch.framework.cache.config;
//
//import java.io.Serializable;
//import java.time.Duration;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisPassword;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import redis.clients.jedis.JedisPoolConfig;
//
///**
// * @Project : ticwatch_backend
// * @Package Name : com.mobvoi.ticwatch.framework.cache.config
// * @Author : xiekun
// * @Desc :
// * @Create Date : 2021年07月23日 17:40
// * ----------------- ----------------- -----------------
// */
//
//@Configuration
//public class JedisRedisConfig {
//
//  private static final Logger logger = LoggerFactory.getLogger(JedisRedisConfig.class);
//
//  @Value("${spring.redis.database}")
//  private int database;
//  @Value("${spring.redis.host}")
//  private String host;
//  @Value("${spring.redis.port}")
//  private int port;
//  @Value("${spring.redis.password}")
//  private String password;
//  @Value("${spring.redis.timeout}")
//  private int timeout;
//  @Value("${spring.redis.jedis.pool.max-active}")
//  private int maxActive;
//  @Value("${spring.redis.jedis.pool.max-wait}")
//  private long maxWaitMillis;
//  @Value("${spring.redis.jedis.pool.max-idle}")
//  private int maxIdle;
//  @Value("${spring.redis.jedis.pool.min-idle}")
//  private int minIdle;
//
//  /**
//   * 连接池配置信息
//   */
//
//  @Bean
//  public JedisPoolConfig jedisPoolConfig() {
//    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//    // 最大连接数
//    jedisPoolConfig.setMaxTotal(maxActive);
//    // 当池内没有可用连接时，最大等待时间
//    jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
//    // 最大空闲连接数
//    jedisPoolConfig.setMinIdle(maxIdle);
//    // 最小空闲连接数
//    jedisPoolConfig.setMinIdle(minIdle);
//    // 其他属性可以自行添加
//    return jedisPoolConfig;
//  }
//
//  /**
//   * Jedis 连接
//   */
//  @Bean
//  public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
//    JedisClientConfiguration jedisClientConfig = JedisClientConfiguration.builder()
//        .usePooling()
//        .poolConfig(jedisPoolConfig).and().readTimeout(Duration.ofMillis(timeout)).build();
//    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
//    config.setHostName(host);
//    config.setPort(port);
//    config.setPassword(RedisPassword.of(password));
//    return new JedisConnectionFactory(config, jedisClientConfig);
//  }
//
//  @Bean
//  public RedisTemplate<String, Serializable> redisTemplate() {
//    RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
//    redisTemplate.setKeySerializer(new StringRedisSerializer());
//    redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//    redisTemplate.setConnectionFactory(jedisConnectionFactory(jedisPoolConfig()));
//    logger.info("Redis init complete host:{},port:{}", host, port);
//    return redisTemplate;
//  }
//
//}
