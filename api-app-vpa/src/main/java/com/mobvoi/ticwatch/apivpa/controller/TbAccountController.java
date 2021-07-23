package com.mobvoi.ticwatch.apivpa.controller;


import com.mobvoi.ticwatch.appvpa.service.account.SAccountService;
import com.mobvoi.ticwatch.framework.core.responses.RestResponse;
import com.mobvoi.ticwatch.framework.domain.entitys.account.TbAccountEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xiekun
 * @since 2021-07-23
 */
@RestController
@RequestMapping("/account")
public class TbAccountController {

  @Autowired
  private SAccountService sAccountService;

  @ApiOperation("查询列表1")
  @GetMapping("/{wwid}")
  public RestResponse<TbAccountEntity> getOne(
      @ApiParam(value = "根据账号查询")
      @PathVariable(value = "wwid") String wwid) {
    TbAccountEntity one = sAccountService.getOne(wwid);
    return RestResponse.success(one);
  }

}
