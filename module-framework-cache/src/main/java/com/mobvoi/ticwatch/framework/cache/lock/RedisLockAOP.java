//package com.mobvoi.ticwatch.framework.cache.lock;
//
//import com.mobvoi.ticwatch.framework.core.utils.MD5EncryptUtil;
//import java.io.UnsupportedEncodingException;
//import java.security.NoSuchAlgorithmException;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//@Aspect
//public class RedisLockAOP {
//
//  private static final Logger log = LoggerFactory.getLogger(RedisLockAOP.class);
//
//  @Autowired
//  private RedisCacheService redisCacheService;
//
//  /**
//   * 指定切点
//   * 匹配 com.example.demo.controller包及其子包下的所有类的所有方法
//   */
//  @Pointcut("@annotation(redisLock)")
//  public void aopRedisLock(RedisLock redisLock) {
//    System.out.println("------------------------------------- redis lock aop init");
//  }
//
//  /**
//   * 前置通知，方法调用前被调用
//   */
//  @Before("aopRedisLock(redisLock)")
//  public void doBefore(JoinPoint joinPoint, RedisLock redisLock) {
////        System.out.println("我是前置通知!!!");
////        //获取目标方法的参数信息
////        Object[] obj = joinPoint.getArgs();
////        Signature signature = joinPoint.getSignature();
////        //代理的是哪一个方法
////        System.out.println("方法："+signature.getName());
////        //AOP代理类的名字
////        System.out.println("方法所在包:"+signature.getDeclaringTypeName());
////        //AOP代理类的类（class）信息
////        signature.getDeclaringType();
////        MethodSignature methodSignature = (MethodSignature) signature;
////        String[] strings = methodSignature.getParameterNames();
////        System.out.println("参数名："+ Arrays.toString(strings));
////        System.out.println("参数值ARGS : " + Arrays.toString(joinPoint.getArgs()));
////        String key = redisLock.prefix() + "";
////        threadLocal.set(key);
////        String expired = redisLock.timeOutMilliSec() + "";
////        redisCacheService.lock(key,expired);
//  }
////    /**
////     * 处理完请求返回内容
////     * @param ret
////     * @throws Throwable
////     */
////    @AfterReturning(returning = "ret", pointcut = "aopRedisLock(redisLock)")
////    public void doAfterReturning(Object ret,RedisLock redisLock) throws Throwable {
////        // 处理完请求，返回内容
////        System.out.println("方法的返回值 : " + ret);
////    }
//
////    /**
////     * 后置异常通知
////     * @param jp
////     */
////    @AfterThrowing("aopRedisLock(redisLock)")
////    public void throwss(JoinPoint jp,RedisLock redisLock){
////    }
//
////    /**
////     * 后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
////     * @param jp
////     */
////    @After("aopRedisLock(redisLock)")
////    public void after(JoinPoint jp,RedisLock redisLock){
////        String key = threadLocal.get();
////        redisCacheService.unlock(key,redisLock.timeOutMilliSec()+"");
////    }
//
//
//  /**
//   * 环绕通知,环绕增强，相当于MethodInterceptor
//   */
//  @Around("aopRedisLock(redisLock)")
//  public Object arround(ProceedingJoinPoint pjp, RedisLock redisLock) throws Throwable {
//
//    Object[] args = pjp.getArgs();
//    String key = redisLock.prefix() + buildKeyFromParams(args);
//    //过期时长
//    long lockTime = redisLock.timeOutMilliSec();
//    //当前时间
//    long currentTime = System.currentTimeMillis();
//    //加锁时间
//    long lockExpireTime = lockTime + currentTime;
//
//    log.debug("【redis lock】create the redis lock,key:{},time:{}", key, lockExpireTime);
//    boolean locked = redisCacheService.lock(key, lockExpireTime);
//    log.debug("【redis lock】redis get lock : {} => {}", locked, params(args));
//
//    Object result = null;
//    try {
//      if (locked) {
//        log.debug("【redis lock】the redis lock is locked, process will go on ");
//        result = pjp.proceed();
//      } else {
//        log.debug("【redis lock】redis lock failed, request ignore: {}", params(args));
////                throw new RedisLockException(redisLock.prefix());
//      }
//    } catch (Exception ex) {
//      log.error("【redis lock】exception", ex);
//      throw ex;
//    } finally {
//      if (locked) {
//        log.debug("【redis lock】delete the redis lock,key:{},unlockExpireTime:{}", key,
//            lockExpireTime);
//        redisCacheService.unlock(key, lockExpireTime);
//      }
//    }
//    return result;
//  }
//
//  private String params(Object[] args) {
//    StringBuffer sb = new StringBuffer("params : ");
//    for (Object obj : args) {
//      sb.append("[").append(obj).append("]");
//    }
//    return sb.toString();
//  }
//
//  /**
//   * 获取所有参数构建key
//   */
//  private String buildKeyFromParams(Object[] args) {
//    StringBuffer sb = new StringBuffer();
//    for (Object obj : args) {
//      sb.append(obj.toString());
//    }
//    try {
//      String md5 = MD5EncryptUtil.getMessageDigest(sb.toString());
//      return md5;
//    } catch (NoSuchAlgorithmException e) {
//      e.printStackTrace();
//    } catch (UnsupportedEncodingException e) {
//      e.printStackTrace();
//    }
//    return "";
//  }
//
//}
