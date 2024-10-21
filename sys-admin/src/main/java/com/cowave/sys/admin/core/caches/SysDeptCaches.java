/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.caches;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.sys.admin.core.entity.TreeNode;
import com.cowave.sys.admin.core.mapper.SysDeptMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author shanhuiming
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysDeptCaches implements ApplicationRunner {

    public static final String KEY_DEPT = "sys:tree:dept";

    private final TreeNodeConfig treeConfig = new TreeNodeConfig()
            .setIdKey("id"). setParentIdKey("pid").setNameKey("label").setChildrenKey("children").setDeep(10);

    private final RedisHelper redisHelper;

    private final SysDeptMapper sysDeptMapper;

    private final SysPostCaches sysPostCaches;

    private final SysUserCaches sysUserCaches;

    @Override
    public void run(ApplicationArguments args) {
        refreshTree();
        sysPostCaches.refreshDeptPostTree();
        sysUserCaches.refreshDeptUserTree();
    }

    public void refreshTree() {
        log.info("refresh cache of dept tree ...");
        List<TreeNode> list = sysDeptMapper.listTree();
        List<Tree<String>> deptTree = TreeUtil.build(list, "0", treeConfig, (d, node) -> {
            node.setId(String.valueOf(d.getId()));
            node.setParentId(String.valueOf(d.getPid()));
            node.setName(d.getLabel());
        });
        redisHelper.putValue(KEY_DEPT, deptTree.get(0));
    }

    public List<Tree<String>> tree(String deptId) {
        Tree<String> root = redisHelper.getValue(KEY_DEPT);
        // 如果没有传deptId，或者传的根部门id，则返回整棵树
        if(deptId == null || deptId.equals(root.getId())) {
            return List.of(root);
        }

        // 空树
        if(CollectionUtils.isEmpty(root.getChildren())) {
            return List.of(new Tree<>());
        }

        // 广度优先，如果节点id与deptId一样则返回
        Deque<Tree<String>> queue = new LinkedList<>(root.getChildren());
        while (!queue.isEmpty()) {
            root = queue.pop();
            if(Objects.equals(deptId, root.getId())) {
                return List.of(root);
            }
            if(CollectionUtils.isNotEmpty(root.getChildren())) {
                queue.addAll(root.getChildren());
            }
        }
        return List.of(new Tree<>());
    }
}
