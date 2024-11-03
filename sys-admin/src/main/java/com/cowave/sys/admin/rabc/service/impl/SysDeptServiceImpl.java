/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.rabc.service.impl;

import cn.hutool.core.lang.tree.Tree;
import com.cowave.commons.client.http.asserts.Asserts;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.OperationContext;
import com.cowave.sys.admin.rabc.infra.dao.SysDeptDao;
import com.cowave.sys.admin.rabc.infra.dao.SysDeptTreeDao;
import com.cowave.sys.admin.rabc.infra.dao.SysUserDao;
import com.cowave.sys.admin.rabc.infra.dao.SysUserDeptDao;
import com.cowave.sys.admin.rabc.infra.dao.mapper.dto.SysDeptDtoMapper;
import com.cowave.sys.admin.rabc.domain.SysDept;
import com.cowave.sys.admin.rabc.domain.request.DeptCreate;
import com.cowave.sys.admin.rabc.domain.request.DeptQuery;
import com.cowave.sys.admin.rabc.domain.request.DeptReadUpdate;
import com.cowave.sys.admin.rabc.infra.redis.SysDeptRedis;
import com.cowave.sys.admin.rabc.service.SysDeptService;
import com.cowave.sys.admin.rabc.domain.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Exception.class)
public class SysDeptServiceImpl implements SysDeptService {
    private final SysDeptRedis sysDeptRedis;
    private final SysUserDao sysUserDao;
    private final SysDeptDao sysDeptDao;
    private final SysUserDeptDao sysUserDeptDao;
    private final SysDeptTreeDao sysDeptTreeDao;
    private final SysDeptDtoMapper sysDeptMapper;

    @Override
    public List<SysDeptListDto> list(DeptQuery deptQuery) {
        return sysDeptMapper.list(deptQuery);
    }

    @Override
    public List<SysDept> listForExport() {
        return sysDeptDao.list();
    }

    @Override
    public SysDeptInfoDto info(Integer deptId) {
        return sysDeptMapper.info(deptId);
    }

    @Override
    public void create(DeptCreate deptCreate) {
        // 新增部门
        deptCreate.setCreateInfo(Access.accessInfo());
        sysDeptDao.save(deptCreate);
        // 上级部门
        sysDeptTreeDao.saveBatch(deptCreate.getDeptParents());
    }

    @Override
    public void edit(DeptCreate deptCreate) {
        Integer deptId = deptCreate.getDeptId();
        Asserts.notNull(deptId, "{dept.notnull.id}");
        // 校验
        SysDeptInfoDto preDept = sysDeptMapper.info(deptId);
        Asserts.notNull(preDept, "{dept.notexist}");
        Asserts.isTrue(preDept.getReadOnly() == 0, "{dept.forbid.edit.readonly}");
        OperationContext.prepareContent(preDept);
        // 更新部门
        sysDeptDao.updateDept(deptCreate);
        // 上级部门
        sysDeptTreeDao.deleteParentsByDeptId(deptId);
        List<Integer> parentIds = deptCreate.getParentIds();
        if (CollectionUtils.isNotEmpty(parentIds)) {
            // 检测循环，children与parents不能有交集
            List<Integer> childIds = sysDeptMapper.childIds(deptId);
            childIds.add(deptId);
            Asserts.isTrue(Collections.disjoint(childIds, parentIds), "{user.tree.cycle}");
            sysDeptTreeDao.saveBatch(deptCreate.getDeptParents());
        }
    }

    @Override
    public void delete(List<Integer> deptIds) {
        Asserts.isTrue(sysDeptDao.countReadOnlyByIds(deptIds) == 0, "{dept.forbid.delete.readonly}");
        Asserts.isTrue(sysDeptTreeDao.countChildByIds(deptIds) == 0, "{dept.forbid.delete.haschild}");
        // 操作日志
        List<SysDept> list = sysDeptDao.listByIds(deptIds);
        OperationContext.prepareContent(list);
        // 删除部门
        sysDeptDao.removeByIds(deptIds);
        // 上级部门关系
        sysDeptTreeDao.deleteParentsByDeptIds(deptIds);
        // 部门用户关系
        sysUserDeptDao.deleteByDeptIds(deptIds);
    }

    @Override
    public Response.Page<SysDeptUserDto> members(Integer deptId) {
        return new Response.Page<>(sysDeptMapper.members(
                deptId, Access.pageSize(), Access.pageOffset()), sysUserDao.countUser());
    }

    @Override
    public void changeReadonly(DeptReadUpdate readUpdate) {
        sysDeptDao.updateDeptReadonly(readUpdate.getDeptId(), readUpdate.getReadOnly());
    }

    @Override
    public List<SysDeptPostDto> getPostsById(Integer deptId) {
        return sysDeptMapper.getPostsById(deptId);
    }

    @Override
    public void setPosts(List<SysDeptPostDto> list) {
        int defaultCount = 0;
        for (SysDeptPostDto post : list) {
            if (post.getIsDefault() == 1) {
                defaultCount++;
            }
        }
        Asserts.isTrue(defaultCount <= 1, "{dept.post.default.max}");
        sysDeptMapper.deleteDeptPosts(list.get(0).getDeptId());
        sysDeptMapper.insertDeptPosts(list);
    }

    @Override
    public List<SysUserDeptDto> getUsersById(Integer deptId) {
        return sysDeptMapper.getUsersById(deptId);
    }

    @Override
    public void setUsers(List<SysUserDeptDto> list) {
        sysDeptMapper.deleteDeptUsers(list.get(0).getDeptId());
        sysDeptMapper.insertDeptUsers(list);
    }

    @Override
    public List<SysUserDto> getUsersByCode(String deptCode) {
        return sysDeptMapper.getUsersByCode(deptCode);
    }

    @Override
    public void refreshDeptTree() {
        sysDeptRedis.refreshDeptTree();
        sysDeptRedis.refreshDeptUserTree();
        sysDeptRedis.refreshDeptPostTree();
    }

    @Override
    public List<Tree<String>> getDeptTree(String deptId) {
        return sysDeptRedis.getDeptTree(deptId);
    }
}
