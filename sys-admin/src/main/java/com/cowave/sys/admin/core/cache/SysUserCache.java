/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.cache;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.sys.admin.core.entity.TreeChildren;
import com.cowave.sys.admin.core.entity.TreeNode;
import com.cowave.sys.admin.core.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 *
 * @author shanhuiming
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysUserCache implements ApplicationRunner {

    private static final String KEY_USER = "sys:tree:user";

    private static final String KEY_DEPT_USER = "sys:tree:dept-user";

    private final TreeNodeConfig treeConfig = new TreeNodeConfig()
            .setIdKey("id"). setParentIdKey("pid").setNameKey("label").setChildrenKey("children");

    private final RedisHelper redisHelper;

    private final SysUserMapper sysUserMapper;

    @Override
    public void run(ApplicationArguments args) {
        refreshTree();
    }

    public Tree<String> tree() {
        return redisHelper.getValue(KEY_USER);
    }

    public Tree<String> deptUserTree() {
        return redisHelper.getValue(KEY_DEPT_USER);
    }

    public void refreshTree() {
        log.info("refresh cache of user tree ...");
        List<TreeNode> list = sysUserMapper.listTree();
        List<Tree<String>> deptTree = TreeUtil.build(list, "0", treeConfig, (u, node) -> {
            node.setId(u.getId());
            node.setParentId(u.getPid());
            node.setName(u.getLabel());
            node.put("content", u.getContent());
        });
        redisHelper.putValue(KEY_USER, deptTree.get(0));
    }

    public void refreshDeptUserTree() {
        log.info("refresh cache of dept-user tree ...");
        Tree<String> tree = redisHelper.getValue(SysDeptCache.KEY_DEPT); // 部门树
        List<TreeChildren> deptUserList = sysUserMapper.deptUserOptions(); // 部门用户
        Map<String, List<Tree<String>>> deptUserMap = new HashMap<>(); // Map<deptId, List<userId>>
        for(TreeChildren deptUser : deptUserList){
            deptUserMap.put(deptUser.getPid(), deptUser.getChildren());
        }

        Tree<String> root = tree;
        Deque<Tree<String>> queue = new LinkedList<>(List.of(root));
        while (!queue.isEmpty()) {
            root = queue.pop();
            List<Tree<String>> children = root.getChildren();
            List<Tree<String>> users = deptUserMap.get(root.getId());
            if(children != null) {
                queue.addAll(root.getChildren());
                if(users != null) {
                    children.addAll(users);
                }
            }else{
                root.setChildren(users);
            }
            root.put("isDept", true);
            root.setId(root.getId() + "d"); // 避免dept与user的id相同导致选择问题
        }
        redisHelper.putValue(KEY_DEPT_USER, tree);
    }
}
