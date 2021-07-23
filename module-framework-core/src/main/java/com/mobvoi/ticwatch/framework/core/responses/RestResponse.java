package com.mobvoi.ticwatch.framework.core.responses;

import com.mobvoi.ticwatch.framework.core.utils.JSONUtils;
import com.mobvoi.ticwatch.framework.core.utils.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 提供统一的REST相应对象封装
 * data 用于提供返回对象的封装
 */
@Data
public class RestResponse<T> {

  private static final Logger logger = LoggerFactory.getLogger(RestResponse.class);
  public static final int CODE_OK = 200;


  @ApiModelProperty(value = "是否请求成功", name = "success")
  private boolean success;

  @ApiModelProperty(value = "返回状态码,如果success = false ，必须提供错误代号", name = "code")
  private int code;

  @ApiModelProperty(value = "返回的详细说明,如果success = false,指出错误消息", name = "msg", example = "成功")
  private String msg;

  @ApiModelProperty(value = "如果success = true,可提供返回对象，也可以不提供", name = "data")
  private T data;

  /**
   * 提供成功的静态方法，方便使用
   */
  public static RestResponse success() {
    return success(null);
  }

  /**
   * 提供成功的静态方法，方便使用
   *
   * @param data 希望作为返回值输出的结果对象
   * @return 返回success为true的相应对象
   */
  public static RestResponse success(Object data) {
    RestResponse restResponse = new RestResponse();
    restResponse.setSuccess(true);
    restResponse.setCode(CODE_OK);
    restResponse.setData(data);
    restResponse.setMsg("success");

    if (logger.isDebugEnabled()) {
      logger.debug("输出响应：{}", StringUtil.trimLog(restResponse.toString()));
    }
    return restResponse;
  }


  /**
   * 提供失败对象的静态方法，方便输出
   */
  public static RestResponse failed() {
    RestResponse restResponse = new RestResponse();
    restResponse.setSuccess(false);
    restResponse.setCode(500);
    restResponse.setMsg("异常");
    return restResponse;
  }

  /**
   * 提供失败对象的静态方法，方便输出
   *
   * @param code 错误码
   * @param msg 错误消息
   */
  public static RestResponse failed(int code, String msg) {
    RestResponse restResponse = new RestResponse();
    restResponse.setSuccess(false);
    restResponse.setCode(code);
    restResponse.setMsg(msg);
    return restResponse;
  }

  /**
   * 提供失败对象的静态方法，方便输出
   */
  public static RestResponse failed(ResultCode resultCode, String data) {
    RestResponse restResponse = new RestResponse();
    restResponse.setSuccess(false);
    restResponse.setCode(resultCode.getCode());
    restResponse.setMsg(resultCode.getMsg());
    restResponse.setData(data);
    return restResponse;
  }

  /**
   * 提供失败对象的静态方法，方便输出
   *
   * @param resultCode 结果枚举对象
   */
  public static RestResponse failed(ResultCode resultCode) {
    return failed(resultCode.getCode(), resultCode.getMsg());
  }

  /**
   * 包装一个指定对象数据类型的响应对象
   */
  public <T> RestResponse<T> wrapIndicateTypeRestResponse(T data) {
    RestResponse<T> restResponse = new RestResponse<>();
    restResponse.setSuccess(this.isSuccess());
    restResponse.setMsg(this.getMsg());
    restResponse.setCode(this.getCode());
    restResponse.setData(data);
    return restResponse;
  }

  @Override
  public String toString() {
    return JSONUtils.objectToJson(this);
  }
}
