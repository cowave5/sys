/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.service;

import cn.hutool.core.lang.tree.Tree;
import com.cowave.commons.framework.support.excel.valid.ExcelDataImporter;
import com.cowave.commons.response.Response;
import com.cowave.sys.admin.core.entity.SysUser;
import com.cowave.sys.admin.core.entity.dto.SysUserDto;
import com.cowave.sys.admin.core.entity.dto.SysUserInfoDto;
import com.cowave.sys.admin.core.entity.dto.SysUserListDto;
import com.cowave.sys.admin.core.entity.request.*;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysUserService extends ExcelDataImporter<SysUser> {

    /**
     * 列表
     */
    Response.Page<SysUserListDto> list(UserQuery query);

    /**
     * 详情
     */
    SysUserInfoDto info(Long userId);

    /**
     * 新增
     */
    void create(UserCreate user);

    /**
     * 修改
     */
    SysUserInfoDto edit(UserCreate user);

    /**
     * 删除
     */
    List<SysUserInfoDto> delete(List<Long> userIds);

    /**
     * 修改角色
     */
    void changeRoles(UserRoleUpdate roleUpdate);

    /**
     * 修改状态
     */
    void changeStatus(UserStatusUpdate statusUpdate);

    /**
     * 修改密码
     */
    void changePasswd(UserPasswdUpdate passwdUpdate);

    /**
     * 修改只读
     */
    void changeReadonly(UserReadUpdate readUpdate);

    /**
     * 导出用户列表
     */
    List<SysUser> getExportUsers(UserExport userExport);

    /**
     * 刷新缓存
     */
    void refreshUserTree();

    /**
     * 用户树
     */
    Tree<String> getUserTree();

    /**
     * 部门用户树
     */
    Tree<String> getDeptUserTree();

    /**
     * 上级用户列表
     */
    List<SysUserDto> leaders(Long userId);
}
