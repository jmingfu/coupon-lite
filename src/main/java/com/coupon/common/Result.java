package com.coupon.common;

import com.coupon.common.enums.CodeEnum;
import lombok.Data;

@Data
public class Result<T> {
    private CodeEnum code;
    private T data;
    private String msg;

    public CodeEnum getCode() {
        return code;
    }

    public void setCode(CodeEnum code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setCode(CodeEnum.Success);
        r.setData(data);
        r.setMsg(CodeEnum.Success.getStatus());
        return r;
    }

    public static <T> Result<T> fail(CodeEnum status, String msg) {
        Result<T> r = new Result<>();
        r.setCode(status);
        r.setMsg(msg);
        return r;
    }
}
