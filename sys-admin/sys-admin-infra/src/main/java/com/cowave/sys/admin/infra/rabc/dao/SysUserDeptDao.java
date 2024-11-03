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
import com.cowave.sys.admin.infra.rabc.dao.mapper.SysUserDeptMapper;
import com.cowave.sys.admin.domain.rabc.SysUserDept;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class SysUserDeptDao extends ServiceImpl<SysUserDeptMapper, SysUserDept> {

    /**
     * 从部门中删除用户
     */
    public void deleteByUserId(Integer userId){
        lambdaUpdate().eq(SysUserDept::getUserId, userId).remove();
    }

    /**
     * 删除用户部门
     */
    public void deleteByDeptIds(List<Integer> deptIds){
        lambdaUpdate().in(SysUserDept::getDeptId, deptIds).remove();
    }
}
