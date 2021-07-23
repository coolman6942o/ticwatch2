package com.mobvoi.ticwatch.appvpa.service.account;

import com.mobvoi.ticwatch.framework.domain.entitys.account.TbAccountEntity;

/**
 * @Project : ticwatch_backend
 * @Package Name : com.mobvoi.ticwatch.appvpa.service.account
 * @Author : xiekun
 * @Desc :
 * @Create Date : 2021年07月23日 16:25
 * ----------------- ----------------- -----------------
 */
public interface SAccountService {

  TbAccountEntity getOne(String accountId);

}
