package com.example.todolistmybatis.common;

public enum ErrorCode {
    /*
    Token校验部分
     */
    UNAUTHORIZED(401, "Token 过期或无效，请重新登录"),
    /*
    用户相关错误码
     */
    USER_EXIST(1001, "用户名已存在"),
    EMAIL_EXIST(1002, "邮箱已被注册"),
    USER_NAME_PWD_WRONG(1003, "登录失败，请检查用户名和密码"),
    SYSTEM_ERROR(500, "系统异常");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
