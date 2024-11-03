package com.cowave.sys.admin.rabc.infra.redis;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.sys.admin.base.domain.vo.TreeChildren;
import com.cowave.sys.admin.base.domain.vo.TreeNode;
import com.cowave.sys.admin.rabc.infra.dao.mapper.dto.SysDeptDtoMapper;
import com.cowave.sys.admin.rabc.infra.dao.mapper.dto.SysPostDtoMapper;
import com.cowave.sys.admin.rabc.infra.dao.mapper.dto.SysUserDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author shanhuiming
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SysDeptRedis implements ApplicationRunner {
    private static final String KEY_DEPT = "sys:tree:dept";
    private static final String KEY_DEPT_USER = "sys:tree:dept-user";
    private static final String KEY_DEPT_POST = "sys:tree:dept-post";

    private final TreeNodeConfig treeConfig = new TreeNodeConfig()
            .setIdKey("id").setParentIdKey("pid").setNameKey("label").setChildrenKey("children");
    private final RedisHelper redisHelper;
    private final SysDeptDtoMapper sysDeptMapper;
    private final SysUserDtoMapper sysUserMapper;
    private final SysPostDtoMapper sysPostMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("init cache of dept tree ...");
        refreshDeptTree();
        log.info("init cache of dept-user tree ...");
        refreshDeptUserTree();
        log.info("init cache of dept-post tree ...");
        refreshDeptPostTree();
    }

    public void refreshDeptTree() {
        List<TreeNode> list = sysDeptMapper.listTree();
        List<Tree<String>> deptTree = TreeUtil.build(list, "0", treeConfig, (d, node) -> {
            node.setId(String.valueOf(d.getId()));
            node.setParentId(String.valueOf(d.getPid()));
            node.setName(d.getLabel());
        });
        redisHelper.putValue(KEY_DEPT, deptTree.get(0));
    }

    public void refreshDeptUserTree() {
        // 部门树
        Tree<String> tree = redisHelper.getValue(KEY_DEPT);
        // 部门用户
        List<TreeChildren> deptUserList = sysUserMapper.deptUserOptions();
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
        redisHelper.putValue(KEY_DEPT_USER, tree);
    }

    // id = deptId-postId, label = postName
    public void refreshDeptPostTree() {
        List<TreeChildren> list = sysPostMapper.deptPostOptions();
        Map<String, List<Tree<String>>> map = new HashMap<>();
        for (TreeChildren option : list) {
            map.put(option.getPid(), option.getChildren());
        }

        // tree.id = deptId, label = deptName
        Tree<String> tree = redisHelper.getValue(KEY_DEPT);
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
        redisHelper.putValue(KEY_DEPT_POST, tree);
    }

    public List<Tree<String>> getDeptTree(String deptId) {
        Tree<String> root = redisHelper.getValue(KEY_DEPT);
        // 如果没有传deptId，或者传的根部门id，则返回整棵树
        if (deptId == null || deptId.equals(root.getId())) {
            return List.of(root);
        }

        // 空树
        if (CollectionUtils.isEmpty(root.getChildren())) {
            return List.of(new Tree<>());
        }

        // 广度优先，如果节点id与deptId一样则返回
        Deque<Tree<String>> queue = new LinkedList<>(root.getChildren());
        while (!queue.isEmpty()) {
            root = queue.pop();
            if (Objects.equals(deptId, root.getId())) {
                return List.of(root);
            }
            if (CollectionUtils.isNotEmpty(root.getChildren())) {
                queue.addAll(root.getChildren());
            }
        }
        return List.of(new Tree<>());
    }

    public Tree<String> getDeptUserTree() {
        return redisHelper.getValue(KEY_DEPT_USER);
    }

    public Tree<String> getDeptPostTree() {
        return redisHelper.getValue(KEY_DEPT_POST);
    }
}
