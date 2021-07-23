package com.mobvoi.ticwatch.appvpa.service.ticwatch.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mobvoi.ticwatch.appvpa.service.ticwatch.SMotionRecordsService;
import com.mobvoi.ticwatch.base.modules.mysql.ticwatch.mapper.TbMotionRecordsMapper;
import com.mobvoi.ticwatch.base.modules.mysql.ticwatch.service.TbMotionRecordsService;
import com.mobvoi.ticwatch.framework.cache.cache.IRedisCache;
import com.mobvoi.ticwatch.framework.core.utils.JSONUtils;
import com.mobvoi.ticwatch.framework.domain.entitys.ticwatch.TbMotionRecordsEntity;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Project : ticwatch_backend
 * @Package Name : com.mobvoi.ticwatch.appvpa.service.ticwatch
 * @Author : xiekun
 * @Desc :
 * @Create Date : 2021年07月22日 21:21
 * ----------------- ----------------- -----------------
 */
@Service
public class SMotionRecordsServiceImpl implements SMotionRecordsService {

  private static final Logger logger = LoggerFactory.getLogger(SMotionRecordsServiceImpl.class);

  @Autowired
  private TbMotionRecordsService tbMotionRecordsService;

  @Autowired
  private TbMotionRecordsMapper tbMotionRecordsMapper;

  @Autowired
  private IRedisCache redisUtils;


  @Override
  public List<TbMotionRecordsEntity> list(String accountId) {
    String key = "LIST_" + accountId;

    List<TbMotionRecordsEntity> result;
    if (!redisUtils.exists(key)) {
      logger.info("[redis] accountinfo not exists ,accountId={}", accountId);
      QueryWrapper<TbMotionRecordsEntity> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("account_id", accountId);
      result = tbMotionRecordsService.list(queryWrapper);
      redisUtils.set(key, result, 100L);
    } else {
      logger.info("[redis] accountinfo exists,accountId={}", accountId);
      Object o = redisUtils.get(key);
      result = JSONUtils.jsonToList(JSONUtils.objectToJson(o), TbMotionRecordsEntity.class);
    }
    return result;
  }

  @Override
  public List<TbMotionRecordsEntity> getList(String accountId) {
    LambdaQueryWrapper<TbMotionRecordsEntity> lqw = new LambdaQueryWrapper<>();
    lqw.eq(TbMotionRecordsEntity::getAccountId, accountId);
    List<TbMotionRecordsEntity> tbMotionRecordsEntities = tbMotionRecordsMapper.selectList(lqw);
    return tbMotionRecordsEntities;
  }
}
