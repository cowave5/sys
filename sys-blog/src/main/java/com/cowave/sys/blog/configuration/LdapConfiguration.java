/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.blog.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 *
 * @author shanhuiming
 *
 */
@ConfigurationProperties(prefix = "ldap")
@Configuration
@Data
public class LdapConfiguration {

    /**
     * 用户名
     */
    private String ldapUser = "zhangyuliang@cowave.com";

    /**
     * 用户密码
     */
    private String ldapPasswd = "Cowave@123";

    /**
     * 基本DN
     */
    private String baseDn = "OU=Cowavers,DC=cowave,DC=com";

    /**
     * 用户DN
     */
    private String userDn = "";

    /**
     * Ldap地址
     */
    private String ldapUrl = "ldap://10.64.3.1:389";

    /**
     * 是否以匿名身份只读
     */
    private int readonly;

    /**
     * 环境属性
     */
    private Map<String, Object> environment;

    public String[] determineUrls() {
        return new String[]{this.ldapUrl};
    }

    public boolean anonymousReadOnly(){
        return readonly == 1;
    }
}
