package com.mobvoi.ticwatch.framework.cache.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Project : ticwatch_backend
 * @Package Name : com.mobvoi.ticwatch.framework.cache.lock
 * @Author : xiekun
 * @Desc :
 * @Create Date : 2021年07月22日 21:21
 * ----------------- ----------------- -----------------
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {

  String prefix() default "lock_";

  /**
   * 锁超时毫秒数
   */
  long timeOutMilliSec() default 1 * 60 * 1000L;
}
