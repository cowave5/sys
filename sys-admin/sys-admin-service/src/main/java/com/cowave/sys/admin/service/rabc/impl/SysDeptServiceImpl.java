/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.rabc.impl;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.operation.OperationContext;
import com.cowave.sys.admin.domain.base.vo.TreeChildren;
import com.cowave.sys.admin.domain.rabc.SysDept;
import com.cowave.sys.admin.domain.rabc.SysDeptPost;
import com.cowave.sys.admin.domain.rabc.SysUserDept;
import com.cowave.sys.admin.domain.rabc.dto.*;
import com.cowave.sys.admin.domain.rabc.request.*;
import com.cowave.sys.admin.infra.rabc.dao.SysDeptDao;
import com.cowave.sys.admin.infra.rabc.dao.SysDeptPostDao;
import com.cowave.sys.admin.infra.rabc.dao.SysDeptTreeDao;
import com.cowave.sys.admin.infra.rabc.dao.SysUserDeptDao;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysDeptDtoMapper;
import com.cowave.sys.admin.infra.rabc.redis.SysDeptRedis;
import com.cowave.sys.admin.service.rabc.SysDeptService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.cowave.commons.client.http.constants.HttpCode.*;
import static com.cowave.sys.admin.domain.AdminRedisKeys.*;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysDeptServiceImpl implements SysDeptService {
    private final SysDeptRedis sysDeptRedis;
    private final SysDeptDao sysDeptDao;
    private final SysUserDeptDao sysUserDeptDao;
    private final SysDeptTreeDao sysDeptTreeDao;
    private final SysDeptPostDao sysDeptPostDao;
    private final SysDeptDtoMapper sysDeptDtoMapper;

    @Override
    public List<DeptListDto> list(String tenantId, DeptQuery query) {
        return sysDeptDtoMapper.list(tenantId, query);
    }

    @Override
    public DeptInfoDto info(Integer deptId) {
        return sysDeptDtoMapper.info(deptId);
    }

    @Override
    public void create(DeptCreate deptCreate) {
        // 新增部门
        sysDeptDao.save(deptCreate);
        // 上级部门
        sysDeptTreeDao.saveBatch(deptCreate.getDeptParents());
    }

    @Override
    public void delete(List<Integer> deptIds) {
        HttpAsserts.isTrue(sysDeptTreeDao.countChildByIds(deptIds) == 0, BAD_REQUEST, "{admin.dept.child.forbid.delete}");
        // 操作日志
        List<SysDept> list = sysDeptDao.listByIds(deptIds);
        OperationContext.prepareContent(list);
        // 删除部门
        sysDeptDao.removeByIds(deptIds);
        // 上级部门关系
        sysDeptTreeDao.deleteParentsByDeptIds(deptIds);
        // 部门用户关系
        sysUserDeptDao.clearByDeptIds(deptIds);
    }

    @Override
    public void edit(DeptCreate deptCreate) {
        Integer deptId = deptCreate.getDeptId();
        HttpAsserts.notNull(deptId, BAD_REQUEST, "{admin.dept.id.notnull}");

        // 校验&操作日志
        DeptInfoDto preDept = sysDeptDtoMapper.info(deptId);
        HttpAsserts.notNull(preDept, NOT_FOUND, "{admin.dept.not.exist}");
        OperationContext.prepareContent(preDept);

        // 更新部门
        sysDeptDao.updateDept(deptCreate);

        // 上级部门
        sysDeptTreeDao.deleteParentsByDeptId(deptId);
        List<Integer> parentIds = deptCreate.getParentIds();
        if (CollectionUtils.isNotEmpty(parentIds)) {
            // 检测循环，children与parents不能有交集
            List<Integer> childIds = sysDeptDtoMapper.childIds(deptId);
            childIds.add(deptId);
            HttpAsserts.isTrue(Collections.disjoint(childIds, parentIds), BAD_REQUEST, "{admin.user.tree.cycle}");
            sysDeptTreeDao.saveBatch(deptCreate.getDeptParents());
        }
    }

    @Override
    public List<SysDept> listForExport() {
        return sysDeptDao.list();
    }

    @Override
    public List<Tree<String>> getDiagram(String tenantId, String deptId) {
        Tree<String> tree = sysDeptRedis.getDeptDiagram(tenantId);
        // 如果没有传deptId，或者传的根部门id，则返回整棵树
        if (deptId == null || deptId.equals(tree.getId())) {
            return List.of(tree);
        }
        // 空树
        if (CollectionUtils.isEmpty(tree.getChildren())) {
            return List.of(new Tree<>());
        }
        // 广度优先，如果节点id与deptId一样则返回
        Deque<Tree<String>> queue = new LinkedList<>(tree.getChildren());
        while (!queue.isEmpty()) {
            tree = queue.pop();
            if (Objects.equals(deptId, tree.getId())) {
                return List.of(tree);
            }
            if (CollectionUtils.isNotEmpty(tree.getChildren())) {
                queue.addAll(tree.getChildren());
            }
        }
        return List.of(new Tree<>());
    }

    @Cacheable(value = DEPT_POST_DIAGRAM, key = "#tenantId")
    @Override
    public Tree<String> getPostDiagram(String tenantId) {
        // 部门树
        Tree<String> tree = sysDeptRedis.getDeptDiagram(tenantId);
        // 部门岗位
        List<TreeChildren> list = sysDeptDtoMapper.deptPostOptions(); // TODO
        Map<String, List<Tree<String>>> map = new HashMap<>();
        for (TreeChildren option : list) {
            map.put(option.getPid(), option.getChildren());
        }

        // tree.id = deptId, label = deptName
        Tree<String> root = tree;
        Deque<Tree<String>> queue = new LinkedList<>(List.of(root));
        while (!queue.isEmpty()) {
            root = queue.pop();
            List<Tree<String>> children = root.getChildren();
            if (children != null) {
                queue.addAll(root.getChildren());
                children.addAll(map.get(root.getId()));
            } else {
                List<Tree<String>> childs = map.get(root.getId());
                if (CollectionUtils.isEmpty(childs)) {
                    root.setChildren(null);
                } else {
                    root.setChildren(map.get(root.getId()));
                }
            }
        }
        return tree;
    }

    @Cacheable(value = DEPT_USER_DIAGRAM, key = "#tenantId")
    @Override
    public Tree<String> getUserDiagram(String tenantId) {
        // 部门树
        Tree<String> tree = sysDeptRedis.getDeptDiagram(tenantId);
        // 部门用户
        List<TreeChildren> deptUserList = sysDeptDtoMapper.deptUserOptions(); // TODO
        // Map<deptId, List<userId>>
        Map<String, List<Tree<String>>> deptUserMap = new HashMap<>();
        for (TreeChildren deptUser : deptUserList) {
            deptUserMap.put(deptUser.getPid(), deptUser.getChildren());
        }

        Tree<String> root = tree;
        Deque<Tree<String>> queue = new LinkedList<>(List.of(root));
        while (!queue.isEmpty()) {
            root = queue.pop();
            List<Tree<String>> children = root.getChildren();
            List<Tree<String>> users = deptUserMap.get(root.getId());
            if (children != null) {
                queue.addAll(root.getChildren());
                if (users != null) {
                    children.addAll(users);
                }
            } else {
                root.setChildren(users);
            }
            root.put("isDept", true);
            root.setId(root.getId() + "d"); // 避免dept与user的id相同导致选择问题
        }
        return tree;
    }

    @Override
    public void addPosts(List<SysDeptPost> list) {
        if(list.isEmpty()){
            return;
        }
        sysDeptDtoMapper.insertDeptPosts(list);
        List<Integer> deptIdList = sysDeptDtoMapper.deptWithMultiDefaultPost();
        HttpAsserts.isEmpty(deptIdList, BAD_REQUEST, "{admin.dept.post.default.max}");
    }

    @Override
    public void removePosts(Integer deptId, List<Integer> postIds) {
        sysDeptPostDao.removePostOfDept(deptId, postIds);
    }

    @Override
    public Page<DeptPostDto> getConfiguredPosts(DeptPostQuery query) {
        return sysDeptDtoMapper.getConfiguredPosts(Access.page(), query);
    }

    @Override
    public Page<DeptPostDto> getUnConfiguredPosts(DeptPostQuery query) {
        return sysDeptDtoMapper.getUnConfiguredPosts(Access.page(), query);
    }

    @Override
    public void addMembers(List<SysUserDept> list) {
        sysDeptDtoMapper.insertDeptUsers(list);
    }

    @Override
    public void removeMembers(Integer deptId, List<Integer> userIds) {
         sysUserDeptDao.removeUserOfDept(deptId, userIds);
    }

    @Override
    public Page<DeptUserDto> getJoinedMembers(DeptUserQuery query) {
        return sysDeptDtoMapper.getJoinedMembers(Access.page(), query);
    }

    @Override
    public Page<DeptUserDto> getUnJoinedMembers(DeptUserQuery query) {
        return sysDeptDtoMapper.getUnJoinedMembers(Access.page(), query);
    }

    @Override
    public List<UserNameDto> getCandidatesByCode(String deptCode) {
        return sysDeptDtoMapper.getCandidatesByCode(deptCode);
    }

    @Override
    public List<String> getNamesById(List<Integer> deptIds) {
        return sysDeptDao.getNamesById(deptIds);
    }
}
