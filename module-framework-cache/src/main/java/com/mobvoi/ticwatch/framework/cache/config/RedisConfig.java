package com.mobvoi.ticwatch.framework.cache.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Project : ticwatch_backend
 * @Package Name : com.mobvoi.ticwatch.framework.cache.config
 * @Author : xiekun
 * @Desc :
 * @Create Date : 2021年07月23日 18:04
 * ----------------- ----------------- -----------------
 */

@EnableCaching
@Configuration
public class RedisConfig {

  private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

  @Value("${spring.redis.host}")
  private String host;
  @Value("${spring.redis.database}")
  private Integer database;
  @Value("${spring.redis.port}")
  private Integer port;
  @Value("${spring.redis.password}")
  private String pwd;
  @Value("${spring.redis.timeout}")
  private int timeout;
  @Value("${spring.redis.jedis.pool.max-active}")
  private int maxActive;
  @Value("${spring.redis.jedis.pool.max-wait}")
  private long maxWaitMillis;
  @Value("${spring.redis.jedis.pool.max-idle}")
  private int maxIdle;
  @Value("${spring.redis.jedis.pool.min-idle}")
  private int minIdle;

  @Primary
  @Bean(name = "jedisPoolConfig")
  @ConfigurationProperties(prefix = "spring.redis.pool")
  public JedisPoolConfig jedisPoolConfig() {
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    // 最大连接数
    jedisPoolConfig.setMaxTotal(maxActive);
    // 当池内没有可用连接时，最大等待时间
    jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
    // 最大空闲连接数
    jedisPoolConfig.setMinIdle(maxIdle);
    // 最小空闲连接数
    jedisPoolConfig.setMinIdle(minIdle);
    // 其他属性可以自行添加
    jedisPoolConfig.setMaxWaitMillis(10000);
    return jedisPoolConfig;
  }

  @Bean
  public RedisConnectionFactory redisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
    redisStandaloneConfiguration.setHostName(host);
    redisStandaloneConfiguration.setDatabase(database);
    redisStandaloneConfiguration.setPassword(pwd);
    redisStandaloneConfiguration.setPort(port);
    JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb = (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration
        .builder();
    jpcb.poolConfig(jedisPoolConfig);
    JedisClientConfiguration jedisClientConfiguration = jpcb.build();
    return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
  }

  /**
   * 配置redisTemplate针对不同key和value场景下不同序列化的方式
   *
   * @param factory Redis连接工厂
   */
  @Primary
  @Bean(name = "redisTemplate")
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(factory);
    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    template.setKeySerializer(stringRedisSerializer);
    template.setHashKeySerializer(stringRedisSerializer);
    Jackson2JsonRedisSerializer redisSerializer = new Jackson2JsonRedisSerializer(Object.class);
    template.setValueSerializer(redisSerializer);
    template.setHashValueSerializer(redisSerializer);
    template.afterPropertiesSet();
    logger.info("Redis init complete host:{},port:{}", host, port);
    return template;
  }

}
