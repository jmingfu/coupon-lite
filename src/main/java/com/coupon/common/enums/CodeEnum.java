package com.coupon.common.enums;

public enum CodeEnum {
    Success(200, "成功"),
    BadRequest(400, "参数错误"),
    Unauthorized(401, "用户未登录"),
    Forbidden(403, "权限不足"),
    NotFound(404, "接口不存在"),
    ServerError(500, "服务器异常");

    private final Integer code;
    private final String status;

    CodeEnum(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }
}
