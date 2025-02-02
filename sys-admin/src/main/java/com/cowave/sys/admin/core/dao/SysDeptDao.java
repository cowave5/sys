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

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.core.dao.mapper.SysDeptMapper;
import com.cowave.sys.admin.core.entity.SysDept;
import com.cowave.sys.admin.core.entity.request.DeptCreate;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author shanhuiming
 */
@Repository
public class SysDeptDao extends ServiceImpl<SysDeptMapper, SysDept> {

    public void updateDept(DeptCreate dept){
        LambdaUpdateWrapper<SysDept> updateWrapper = prepareUpdateWrapper(dept.getDeptId());
        updateWrapper.set(SysDept::getDeptName, dept.getDeptName());
        updateWrapper.set(SysDept::getDeptShort, dept.getDeptShort());
        updateWrapper.set(SysDept::getDeptAddr, dept.getDeptAddr());
        updateWrapper.set(SysDept::getDeptPhone, dept.getDeptPhone());
        updateWrapper.set(SysDept::getRemark, dept.getRemark());
        update(updateWrapper);
    }

    private LambdaUpdateWrapper<SysDept> prepareUpdateWrapper(Integer deptId){
        LambdaUpdateWrapper<SysDept> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysDept::getDeptId, deptId);
        updateWrapper.set(SysDept::getUpdateUser, Access.userId());
        updateWrapper.set(SysDept::getUpdateDept, Access.deptId());
        updateWrapper.set(SysDept::getUpdateTime, new Date());
        return updateWrapper;
    }
}
