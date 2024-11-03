/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.rabc;

import cn.hutool.core.lang.tree.Tree;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.support.excel.valid.ExcelDataImporter;
import com.cowave.sys.admin.domain.rabc.SysUser;
import com.cowave.sys.admin.domain.rabc.dto.SysUserInfoDto;
import com.cowave.sys.admin.domain.rabc.dto.SysUserListDto;
import com.cowave.sys.admin.domain.rabc.dto.SysUserNameDto;
import com.cowave.sys.admin.domain.rabc.request.*;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysUserService extends ExcelDataImporter<SysUser> {

    /**
     * 列表
     */
    Response.Page<SysUserListDto> list(SysUserQuery query);

    /**
     * 详情
     */
    SysUserInfoDto info(Integer userId);

    /**
     * 新增
     */
    void create(SysUserCreate user);

    /**
     * 修改
     */
    void edit(SysUserCreate user);

    /**
     * 删除
     */
    void delete(List<Integer> userIds);

    /**
     * 修改角色
     */
    void changeRoles(SysUserRoleUpdate roleUpdate);

    /**
     * 修改状态
     */
    void changeStatus(SysUserStatusUpdate statusUpdate);

    /**
     * 修改密码
     */
    void changePasswd(SysUserPasswdUpdate passwdUpdate);

    /**
     * 修改只读
     */
    void changeReadonly(SysUserReadUpdate readUpdate);

    /**
     * 导出用户列表
     */
    List<SysUser> listForExport(SysUserExport userExport);

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
     * 候选人列表：上级用户
     */
    List<SysUserNameDto> leaders(Integer userId);
}
