/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.mapper;

import com.cowave.sys.admin.core.entity.UserProfile;
import com.cowave.sys.admin.core.entity.UserRegister;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface ProfileMapper {

    /**
     * 注册
     */
    void register(UserRegister userRegister);

    /**
     * 初始角色
     */
    void initRole(Long userId);

    /**
     * 历史通知
     */
    void initHistoryNotice(Long userId);

    /**
     * 统计通知
     */
    void updateNoticeStat();

    /**
     * 详情
     */
    UserProfile oauthInfo(Long userId);

    /**
     * 详情
     */
    UserProfile info(Long userId);

    /**
     * 修改
     */
    void edit(UserProfile userProfile);

    /**
     * 密码
     */
    String queryPasswd(Long userId);

    /**
     * 重置密码
     */
    void resetPasswd(@Param("userId") Long userId, @Param("passwd") String passwd);
}