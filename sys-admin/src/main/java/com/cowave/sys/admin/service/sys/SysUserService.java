/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.sys;

import java.util.List;

import com.cowave.commons.framework.support.excel.valid.ExcelDataImporter;
import com.cowave.sys.model.admin.SysUser;

/**
 *
 * @author shanhuiming
 *
 */
public interface SysUserService extends ExcelDataImporter<SysUser> {

    /**
     * 列表
     */
    List<SysUser> list(SysUser sysUser);

    /**
     * 计数
     */
    int count(SysUser sysUser);

    /**
     * 详情
     */
    SysUser info(Long userId);

    /**
     * 新增
     */
    void add(SysUser sysUser);

    /**
     * 编辑
     */
    SysUser edit(SysUser sysUser);

    /**
     * 删除
     */
    List<SysUser> delete(Long[] userIds);

    /**
     * 修改状态
     */
    void changeStatus(SysUser sysUser);

    /**
     * 修改密码
     */
    void changePasswd(SysUser sysUser);

    /**
     * 修改角色
     */
    void changeRoles(SysUser sysUser);

    /**
     * 修改只读
     */
    void changeReadonly(SysUser sysUser);

    /**
     * 候选人: 用户Leaders
     */
    List<SysUser> leaders(Long userId);
}
