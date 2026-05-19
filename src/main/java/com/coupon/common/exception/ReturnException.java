package com.coupon.common.exception;

import com.coupon.common.enums.CodeEnum;

public class ReturnException extends RuntimeException {
    private CodeEnum code;
    private String msg;

    public ReturnException(String msg) {
        super(msg);
        this.msg = msg;
        this.code = CodeEnum.ServerError;
    }

    public ReturnException(CodeEnum code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public CodeEnum getCode() {
        return code;
    }

    public void setCode(CodeEnum code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
