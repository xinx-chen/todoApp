package com.example.todolistmybatis.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result<T> {
    private Integer code;    // 业务状态码
    private String message;  // 提示信息
    private T data;         // 响应数据

    /**
     * 成功响应(带数据)
     */
    public static <E> Result<E> success(E data) {
        return new Result<>(200, "请求成功", data);
    }

    /**
     * 成功响应(带自定义消息和数据)
     */
    public static <E> Result<E> success(String message, E data) {
        return new Result<>(200, message, data);
    }

    /**
     * 成功响应(带自定义消息，不带数据)
     */
    public static <E> Result<E> success(String message) {
        return success(message,null);
    }
    /**
     * 失败响应(带自定义消息)
     */
    public static <E> Result<E> fail(String message) {
        return new Result<>(500, message, null);
}
        /**
     * 失败响应(带状态码和自定义消息)
     */
    public static <E> Result<E> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}
