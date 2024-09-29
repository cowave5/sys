/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.rabc.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.domain.rabc.SysUser;
import com.cowave.sys.admin.domain.rabc.request.UserCreate;
import com.cowave.sys.admin.domain.rabc.request.UserExportQuery;
import com.cowave.sys.admin.infra.rabc.dao.mapper.SysUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class SysUserDao extends ServiceImpl<SysUserMapper, SysUser> {

    /**
     * 获取导出用户
     */
    public List<SysUser> listForExport(UserExportQuery query) {
        return lambdaQuery()
                .eq(StringUtils.isNotBlank(query.getRank()), SysUser::getRank, query.getRank())
                .like(StringUtils.isNotBlank(query.getUserName()), SysUser::getUserName, query.getUserName())
                .like(StringUtils.isNotBlank(query.getUserPhone()), SysUser::getUserPhone, query.getUserPhone())
                .list();
    }

    /**
     * 查询姓名
     */
    public String queryNameById(Integer userId) {
        return lambdaQuery().select(SysUser::getUserName)
                .eq(SysUser::getUserId, userId)
                .one().getUserName();
    }

    /**
     * 账号查询用户
     */
    public SysUser getByUserAccount(String userAccount){
        return lambdaQuery().eq(SysUser::getUserAccount, userAccount).one();
    }

    /**
     * 账号是否已存在
     */
    public boolean isAccountExist(String userAccount){
        return lambdaQuery().eq(SysUser::getUserAccount, userAccount).count() > 0;
    }

    /**
     * 账号是否已存在，排除自己
     */
    public boolean isAccountExist(String userAccount, Integer userId){
        return lambdaQuery().eq(SysUser::getUserAccount, userAccount).ne(SysUser::getUserId, userId).count() > 0;
    }

    /**
     * 修改用户信息
     */
    public void updateUser(UserCreate user){
        lambdaUpdate().eq(SysUser::getUserId, user.getUserId())
                .set(SysUser::getUpdateUser, Access.userId())
                .set(SysUser::getUpdateDept, Access.deptId())
                .set(SysUser::getUpdateTime, new Date())
                .set(SysUser::getUserName, user.getUserName())
                .set(SysUser::getUserAccount, user.getUserAccount())
                .set(SysUser::getUserSex, user.getUserSex())
                .set(SysUser::getUserPhone, user.getUserPhone())
                .set(SysUser::getUserEmail, user.getUserEmail())
                .set(SysUser::getRank, user.getRank())
                .set(SysUser::getRemark, user.getRemark())
                .update();
    }

    /**
     * 修改用户状态
     */
    public void updateUserStatus(Integer userId, Integer status){
        lambdaUpdate().eq(SysUser::getUserId, userId)
                .set(SysUser::getUpdateUser, Access.userId())
                .set(SysUser::getUpdateDept, Access.deptId())
                .set(SysUser::getUpdateTime, new Date())
                .set(SysUser::getUserStatus, status)
                .update();
    }

    /**
     * 修改用户密码
     */
    public void updateUserPasswd(Integer userId, String passwd){
        lambdaUpdate().eq(SysUser::getUserId, userId)
                .set(SysUser::getUpdateUser, Access.userId())
                .set(SysUser::getUpdateDept, Access.deptId())
                .set(SysUser::getUpdateTime, new Date())
                .set(SysUser::getUserPasswd, passwd)
                .update();
    }

    /**
     * 修改用户只读
     */
    public void updateUserReadonly(Integer userId, Integer readonly){
        lambdaUpdate().eq(SysUser::getUserId, userId)
                .set(SysUser::getUpdateUser, Access.userId())
                .set(SysUser::getUpdateDept, Access.deptId())
                .set(SysUser::getUpdateTime, new Date())
                .set(SysUser::getReadOnly, readonly)
                .update();
    }
}
