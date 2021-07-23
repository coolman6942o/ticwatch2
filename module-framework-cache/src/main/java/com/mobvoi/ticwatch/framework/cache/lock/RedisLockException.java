package com.mobvoi.ticwatch.framework.cache.lock;

public class RedisLockException extends RuntimeException {

  private String prefix;

  public RedisLockException(String prefix) {
    this.prefix = prefix;
  }

  @Override
  public String getMessage() {
    return "redis lock excepiton: " + prefix;
  }
}
