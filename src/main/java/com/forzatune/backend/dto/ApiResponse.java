package com.forzatune.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 通用API响应体
 * @param <T> 响应数据的类型
 */
@Data
// 这个注解表示，如果字段值为null，则在生成的JSON中不包含该字段。这对于 error 字段很方便。
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
    private ApiError error;

    // 静态工厂方法，用于快速创建成功响应
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setData(data);
        response.setMessage("操作成功");
        return response;
    }

    // 静态工厂方法，用于快速创建失败响应
    public static <T> ApiResponse<T> failure(String errorMessage) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(errorMessage);
        response.setError(new ApiError("ERROR", errorMessage));
        return response;
    }

    /**
     * 内部类，用于表示错误信息
     */
    @Data
    public static class ApiError {
        private String code;
        private String message;
        private Object details;

        public ApiError(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public ApiError(String code, String message, Object details) {
            this.code = code;
            this.message = message;
            this.details = details;
        }
    }
}