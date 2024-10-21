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
import com.cowave.sys.admin.core.entity.TreeChildren;
import com.cowave.sys.admin.core.entity.TreeNode;
import com.cowave.sys.admin.core.mapper.SysPostMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
public class SysPostCaches implements ApplicationRunner {

    private static final String KEY_POST = "sys:tree:post";

    private static final String KEY_DEPT_POST = "sys:tree:dept-post";

    private final TreeNodeConfig treeConfig = new TreeNodeConfig()
            .setIdKey("id"). setParentIdKey("pid").setNameKey("label").setChildrenKey("children");

    private final RedisHelper redisHelper;

    private final SysPostMapper sysPostMapper;

    @Override
    public void run(ApplicationArguments args) {
        refreshTree();
    }

    public Tree<String> tree() {
        return redisHelper.getValue(KEY_POST);
    }

    public Tree<String> deptPostTree() {
        return redisHelper.getValue(KEY_DEPT_POST);
    }

    // id = postId
    public void refreshTree() {
        log.info("refresh cache of post tree ...");
        List<TreeNode> list = sysPostMapper.listTree();
        List<Tree<String>> deptTree = TreeUtil.build(list, "0", treeConfig, (p, node) -> {
            node.setId(p.getId());
            node.setParentId(p.getPid());
            node.setName(p.getLabel());
        });
        redisHelper.putValue(KEY_POST, deptTree.get(0));
    }

    // id = deptId-postId, label = postName
    public void refreshDeptPostTree(){
        log.info("refresh cache of dept-post tree ...");
        List<TreeChildren> list = sysPostMapper.deptPostOptions();
        Map<String, List<Tree<String>>> map = new HashMap<>();
        for(TreeChildren option : list){
            map.put(option.getPid(), option.getChildren());
        }

        // tree.id = deptId, label = deptName
        Tree<String> tree = redisHelper.getValue(SysDeptCaches.KEY_DEPT);
        Tree<String> root = tree;
        Deque<Tree<String>> queue = new LinkedList<>(List.of(root));
        while (!queue.isEmpty()) {
            root = queue.pop();
            List<Tree<String>> children = root.getChildren();
            if(children != null) {
                queue.addAll(root.getChildren());
                children.addAll(map.get(root.getId()));
            }else{
                List<Tree<String>> childs = map.get(root.getId());
                if(CollectionUtils.isEmpty(childs)){
                    root.setChildren(null);
                }else{
                    root.setChildren(map.get(root.getId()));
                }
            }
        }
        redisHelper.putValue(KEY_DEPT_POST, tree);
    }
}
