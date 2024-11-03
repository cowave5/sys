/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.auth;

import org.springframework.security.core.AuthenticationException;

/**
 *
 * UsernameNotFoundException在里面会被catch掉，并重新抛出BadCredentialsException，所以这里自己加个异常
 *
 * @author shanhuiming
 *
 */
public class UserNotFoundException extends AuthenticationException {

    /**
     * Constructs a <code>UsernameNotFoundException</code> with the specified message.
     * @param msg the detail message.
     */
    public UserNotFoundException(String msg) {
        super(msg);
    }

    /**
     * Constructs a {@code UsernameNotFoundException} with the specified message and root
     * cause.
     * @param msg the detail message.
     * @param cause root cause
     */
    public UserNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
