package com.mobvoi.ticwatch.appvpa.service.ticwatch.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mobvoi.ticwatch.appvpa.service.ticwatch.SMotionRecordsService;
import com.mobvoi.ticwatch.framework.domain.entitys.ticwatch.TbMotionRecordsEntity;
import com.mobvoi.ticwatch.module.base.modules.mysql.ticwatch.mapper.TbMotionRecordsMapper;
import com.mobvoi.ticwatch.module.base.modules.mysql.ticwatch.service.TbMotionRecordsService;
import java.util.List;
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

  @Autowired
  private TbMotionRecordsService tbMotionRecordsService;

  @Autowired
  private TbMotionRecordsMapper tbMotionRecordsMapper;

  @Override
  public List<TbMotionRecordsEntity> list(String accountId) {
    QueryWrapper<TbMotionRecordsEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("account_id",accountId);
    return tbMotionRecordsService.list(queryWrapper);
  }

  @Override
  public List<TbMotionRecordsEntity> getList(String accountId) {
    LambdaQueryWrapper<TbMotionRecordsEntity> lqw = new LambdaQueryWrapper<>();
    lqw.eq(TbMotionRecordsEntity::getAccountId, accountId);
    List<TbMotionRecordsEntity> tbMotionRecordsEntities = tbMotionRecordsMapper.selectList(lqw);
    return tbMotionRecordsEntities;
  }
}
