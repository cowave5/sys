/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.domain.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author shanhuiming
 */
@Data
public class RegisterUser {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户账号
     */
    @NotBlank(message = "{user.notnull.account}")
    private String userAccount;

    /**
     * 用户邮箱
     */
    @NotBlank(message = "{user.notnull.email}")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "user.invalid.email")
    private String userEmail;

    /**
     * 用户密码
     */
    private String userPasswd;

    /**
     * 用户编码
     */
    private String userCode;

    /**
     * 验证码
     */
    private String captcha;
}
