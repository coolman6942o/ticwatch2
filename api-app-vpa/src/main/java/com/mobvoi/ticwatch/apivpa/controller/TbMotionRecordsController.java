package com.mobvoi.ticwatch.apivpa.controller;


import com.mobvoi.ticwatch.appvpa.service.ticwatch.SMotionRecordsService;
import com.mobvoi.ticwatch.framework.core.responses.RestResponse;
import com.mobvoi.ticwatch.framework.domain.entitys.ticwatch.TbMotionRecordsEntity;
import com.mobvoi.ticwatch.framework.domain.influxdb.motion.MotionPoint;
import com.mobvoi.ticwatch.framework.domain.vo.MotionPointVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xiekun
 * @since 2021-07-22
 */

@Api(value = "motion-records-API", description = "这是用户接口详细信息的描述")
@RestController
@RequestMapping("/motion_records")
public class TbMotionRecordsController {

  @Autowired
  private SMotionRecordsService sMotionRecordsService;

  @ApiOperation("查询列表1")
  @GetMapping("/{accountId}")
  public RestResponse<List<TbMotionRecordsEntity>> getOne(
      @ApiParam(value = "根据账号查询")
      @PathVariable(value = "accountId") String accountId) {
    List<TbMotionRecordsEntity> list = sMotionRecordsService.list(accountId);
    return RestResponse.success(list);
  }

  @ApiOperation("查询列表2")
  @GetMapping("/mapper/{accountId}")
  public RestResponse<List<TbMotionRecordsEntity>> getlist(
      @ApiParam(value = "根据账号查询")
      @PathVariable(value = "accountId") String accountId) {
    List<TbMotionRecordsEntity> list = sMotionRecordsService.getList(accountId);
    return RestResponse.success(list);
  }


  @ApiOperation("influxdb保存列表")
  @PostMapping("/influxdb/save")
  public RestResponse saveMotionRecords(@RequestBody List<MotionPointVO> volist) {
    sMotionRecordsService.save(volist);
    return RestResponse.success();
  }

  @ApiOperation("influxdb查询列表")
  @GetMapping("/influxdb/querylist")
  public RestResponse queryMotionRecords() {
    List<MotionPoint> resList = sMotionRecordsService.queryList();
    return RestResponse.success(resList);
  }


}
