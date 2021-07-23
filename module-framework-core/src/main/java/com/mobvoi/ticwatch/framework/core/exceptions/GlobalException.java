package com.mobvoi.ticwatch.framework.core.exceptions;

import com.mobvoi.ticwatch.framework.core.responses.ResultCode;
import lombok.Data;

/**
 * @Project : ticwatch_backend
 * @Package Name : com.mobvoi.ticwatch.framework.core.exceptions
 * @Author : xiekun
 * @Desc :
 * @Create Date : 2021年07月21日 17:01
 * ----------------- ----------------- -----------------
 */

@Data
public class GlobalException extends RuntimeException{

  private Integer code;
  private String msg;
  private Object data;

  public GlobalException(String message) {
    super(message);
  }

  public GlobalException(ResultCode resultCode) {
    super(resultCode.get().getMsg());
    this.code = resultCode.get().getCode();
  }

  public GlobalException(String message, Object data) {
    super(message);
    this.data = data;
  }

  public GlobalException(String message, String msg) {
    super(message);
    this.msg = msg;
  }

  public GlobalException(String message, String msg, Object data) {
    super(message);
    this.msg = msg;
    this.data = data;
  }

  public GlobalException(String message, Throwable cause) {
    super(message, cause);
  }

  public GlobalException(String message, Object data, Throwable cause) {
    super(message, cause);
    this.data = data;
  }

  public GlobalException(String message, String msg, Throwable cause) {
    super(message, cause);
    this.msg = msg;
  }

  public GlobalException(String message, String msg, Object data, Throwable cause) {
    super(message, cause);
    this.msg = msg;
    this.data = data;
  }

  public GlobalException(Integer code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }


}
