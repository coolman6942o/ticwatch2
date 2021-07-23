package com.mobvoi.ticwatch.base.modules.mysql.ticwatch.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobvoi.ticwatch.base.modules.mysql.ticwatch.mapper.TbMotionRecordsMapper;
import com.mobvoi.ticwatch.base.modules.mysql.ticwatch.service.TbMotionRecordsService;
import com.mobvoi.ticwatch.framework.domain.entitys.ticwatch.TbMotionRecordsEntity;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xiekun
 * @since 2021-07-22
 */
@Service
@DS("ticwatch")
public class TbMotionRecordsServiceImpl extends
    ServiceImpl<TbMotionRecordsMapper, TbMotionRecordsEntity> implements TbMotionRecordsService {

}
