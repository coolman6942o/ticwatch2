package com.mobvoi.ticwatch.module.base.modules.mysql.account.service.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobvoi.ticwatch.framework.domain.entitys.account.TbAccountEntity;
import com.mobvoi.ticwatch.module.base.modules.mysql.account.mapper.TbAccountMapper;
import com.mobvoi.ticwatch.module.base.modules.mysql.account.service.TbAccountService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xiekun
 * @since 2021-07-23
 */
@Service
@DS("ticaccount")
public class TbAccountServiceImpl extends ServiceImpl<TbAccountMapper, TbAccountEntity> implements
    TbAccountService {

}
