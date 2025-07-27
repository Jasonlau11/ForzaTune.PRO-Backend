package com.forzatune.backend.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户注册请求DTO
 */
@Data
public class RegisterRequest {

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 50, message = "密码长度必须在6-50个字符之间")
    private String pass; // 前端传递的字段名为pass

    @NotBlank(message = "Xbox ID不能为空")
    @Size(min = 3, max = 50, message = "Xbox ID长度必须在3-50个字符之间")
    private String xboxId; // Xbox Live ID

} 