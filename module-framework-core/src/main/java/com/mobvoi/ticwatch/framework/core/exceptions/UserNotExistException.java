package com.mobvoi.ticwatch.framework.core.exceptions;

import com.mobvoi.ticwatch.framework.core.responses.ResultCode;

/**
 * @Project : elearningweb
 * @Package Name : com.mobvoi.elearning.console.exception
 * @Author : xiekun
 * @Create Date : 2018年06月08日 下午4:23
 * @ModificationHistory Who   When     What
 * ------------    --------------    ---------------------------------
 */
public class UserNotExistException extends RuntimeException{

    private Integer code;

    public UserNotExistException(ResultCode resultCode) {
        super(resultCode.get().getMsg());

        this.code = resultCode.get().getCode();
    }

    public UserNotExistException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
