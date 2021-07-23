package com.mobvoi.ticwatch.appvpa.service.account.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mobvoi.ticwatch.appvpa.service.account.SAccountService;
import com.mobvoi.ticwatch.framework.domain.entitys.account.TbAccountEntity;
import com.mobvoi.ticwatch.base.modules.mysql.account.service.TbAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Project : ticwatch_backend
 * @Package Name : com.mobvoi.ticwatch.appvpa.service.account.impl
 * @Author : xiekun
 * @Desc :
 * @Create Date : 2021年07月23日 16:25
 * ----------------- ----------------- -----------------
 */

@Service
public class SAccountServiceImpl implements SAccountService {

  @Autowired
  private TbAccountService tbAccountService;


  @Override
  public TbAccountEntity getOne(String wwid) {
    QueryWrapper<TbAccountEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("wwid",wwid);
    return tbAccountService.getOne(queryWrapper);
  }
}
