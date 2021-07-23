package com.mobvoi.ticwatch.framework.core.exceptions;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mobvoi.ticwatch.framework.core.responses.RestResponse;
import com.mobvoi.ticwatch.framework.core.responses.ResultCode;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Project : ticwatch_backend
 * @Package Name : com.mobvoi.ticwatch.framework.core.exceptions
 * @Author : xiekun
 * @Desc : 全局异常捕捉
 * @Create Date : 2021年07月21日 17:00
 * ----------------- ----------------- -----------------
 */

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {


  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * 全局未知异常捕获
   *
   * @param request
   * @param e
   * @return
   */
  @ExceptionHandler(value = Exception.class)
  public RestResponse<?> handleUnknownException(HttpServletRequest request, Exception e) {
    logger.error("【handleUnknownException】:url:{}, {}", request.getRequestURL(), e);
    return RestResponse.failed(ResultCode.ERROR);
  }

  /**
   * 自定义全局异常消息提示
   * @param request
   * @param exception
   * @return
   */
  @ExceptionHandler(GlobalException.class)
  public RestResponse<?> handleGlobalException(HttpServletRequest request, GlobalException exception) {
    logger.error(String.format("【handleGlobalException】: Message: %s, InternalMessage:%s, Data:%s"
        , exception.getMessage(), exception.getMsg(), JSON.toJSON(exception.getData())), exception);
    Integer code = exception.getCode();
    ResultCode byCode = ResultCode.getByCode(code);
    return RestResponse.failed(byCode.getCode(),byCode.getMsg());
  }


  /**
   * 参数合法性校验
   * @param request
   * @param ex
   * @return
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public RestResponse<?> handleIllegalArgumentException(HttpServletRequest request, IllegalArgumentException ex) {
    logger.error("【handleIllegalArgumentException】",ex);
    return RestResponse.failed(ResultCode.BIND_ERROR,ex.getMessage());
  }


  /**
   * JSR-303参数错误全局异常捕捉
   * @param request
   * @param ex
   * @return
   */
  @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
  public RestResponse<?> handleBindException(HttpServletRequest request,Exception ex) {
    List<ObjectError> errors = Lists.newArrayList();
    if(ex instanceof BindException){
      BindException e = (BindException)ex;
      errors = e.getAllErrors();
    }else if(ex instanceof MethodArgumentNotValidException){
      MethodArgumentNotValidException e2 = (MethodArgumentNotValidException)ex;
      BindingResult bindingResult = e2.getBindingResult();
      errors = bindingResult.getAllErrors();
    }

    /*注意：此处的BindException 是 Spring 框架抛出的Validation异常*/
    //绑定错误返回很多错误，是一个错误列表，只需要第一个错误
    Object rejectedValue = null;
    String resMsg = null;
    StringBuffer requestURL = null;
    try {
      ObjectError error = errors.get(0);
      String msg = error.getDefaultMessage();
      String field = ((FieldError) error).getField();
      rejectedValue = ((FieldError) error).getRejectedValue();

      resMsg = field.concat(msg);
      requestURL = request.getRequestURL();
    } catch (Exception e) {
      logger.error("参数绑定校验异常",e);
    }

    logger.debug("\n\n【handleBindException】\nrequestURL-->{},\nresMsg-->{},\nrejectedValue-->{}\n\n",requestURL,resMsg,rejectedValue);
    return RestResponse.failed(ResultCode.BIND_ERROR,ex.getMessage());

  }


}
