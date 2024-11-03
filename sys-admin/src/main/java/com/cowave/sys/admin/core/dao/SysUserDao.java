/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.core.dao.mapper.SysUserMapper;
import com.cowave.sys.admin.core.entity.SysUser;
import com.cowave.sys.admin.core.entity.request.UserCreate;
import com.cowave.sys.admin.core.entity.request.UserExport;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class SysUserDao extends ServiceImpl<SysUserMapper, SysUser> {

    public List<SysUser> getExportUsers(UserExport userExport){
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(userExport.getUserName())){
            queryWrapper.like(SysUser::getUserName, userExport.getUserName());
        }
        if(StringUtils.isNotBlank(userExport.getUserPhone())){
            queryWrapper.like(SysUser::getUserPhone, userExport.getUserPhone());
        }
        if(userExport.getRank() != null){
            queryWrapper.eq(SysUser::getRank, userExport.getRank());
        }
        return list(queryWrapper);
    }

    public long countUser(){
        return lambdaQuery().gt(SysUser::getUserId, 1).count();
    }

    public SysUser getByUserAccount(String userAccount){
        return lambdaQuery().eq(SysUser::getUserAccount, userAccount).one();
    }

    public boolean isAccountExist(String userAccount){
        return lambdaQuery().eq(SysUser::getUserAccount, userAccount).count() > 0;
    }

    public boolean isAccountExist(String userAccount, Integer userId){
        return lambdaQuery().eq(SysUser::getUserAccount, userAccount).ne(SysUser::getUserId, userId).count() > 0;
    }

    public void updateUser(UserCreate user){
        LambdaUpdateWrapper<SysUser> updateWrapper = prepareUpdateWrapper(user.getUserId());
        updateWrapper.set(SysUser::getUserName, user.getUserName());
        updateWrapper.set(SysUser::getUserAccount, user.getUserAccount());
        updateWrapper.set(SysUser::getUserSex, user.getUserSex());
        updateWrapper.set(SysUser::getUserPhone, user.getUserPhone());
        updateWrapper.set(SysUser::getUserEmail, user.getUserEmail());
        updateWrapper.set(SysUser::getRank, user.getRank());
        updateWrapper.set(SysUser::getRemark, user.getRemark());
        update(updateWrapper);
    }

    public void updateUserStatus(Integer userId, Integer status){
        LambdaUpdateWrapper<SysUser> updateWrapper = prepareUpdateWrapper(userId);
        updateWrapper.set(SysUser::getUserStatus, status);
        update(updateWrapper);
    }

    public void updateUserPasswd(Integer userId, String passwd){
        LambdaUpdateWrapper<SysUser> updateWrapper = prepareUpdateWrapper(userId);
        updateWrapper.set(SysUser::getUserPasswd, passwd);
        update(updateWrapper);
    }

    public void updateUserReadonly(Integer userId, Integer readonly){
        LambdaUpdateWrapper<SysUser> updateWrapper = prepareUpdateWrapper(userId);
        updateWrapper.set(SysUser::getReadOnly, readonly);
        update(updateWrapper);
    }

    private LambdaUpdateWrapper<SysUser> prepareUpdateWrapper(Integer userId){
        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysUser::getUserId, userId);
        updateWrapper.set(SysUser::getUpdateUser, Access.userId());
        updateWrapper.set(SysUser::getUpdateDept, Access.deptId());
        updateWrapper.set(SysUser::getUpdateTime, new Date());
        return updateWrapper;
    }
}
