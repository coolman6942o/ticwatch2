package com.ticwatch.moduleframeworkcache;

import com.mobvoi.ticwatch.framework.cache.cache.IRedisCache;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CacheApplicationTests.class)
class CacheApplicationTests {

  @Autowired
  private IRedisCache globalCache;

  @Test
  public void test() {
    globalCache.set("key2", "value3");
    globalCache.lSetAll("list", Arrays.asList("hello", "redis"));
    List<Object> list = globalCache.lGet("list", 0, -1);
    System.out.println(list);
    System.out.println(globalCache.get("key2"));
  }

}
