/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.auth;

import com.cowave.sys.admin.domain.auth.request.ApiTokenRequest;
import com.cowave.sys.admin.domain.auth.vo.ApiTokenVo;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface ApiTokenService {

    /**
     * 列表
     */
    List<ApiTokenVo> listApiToken();

    /**
     * 创建用户令牌
     */
    String creatApiToken(ApiTokenRequest request);

    /**
     * 删除用户令牌
     */
    void deleteApiToken(Integer tokenId);
}
