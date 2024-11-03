package com.cowave.sys.admin.core;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.commons.framework.helper.redis.dict.DictValueParser;
import com.cowave.sys.admin.core.dao.mapper.dto.SysConfigDtoMapper;
import com.cowave.sys.admin.core.dao.mapper.dto.SysDeptDtoMapper;
import com.cowave.sys.admin.core.dao.mapper.dto.SysPostDtoMapper;
import com.cowave.sys.admin.core.dao.mapper.dto.SysUserDtoMapper;
import com.cowave.sys.admin.core.entity.dto.SysConfigDto;
import com.cowave.sys.admin.core.entity.vo.TreeChildren;
import com.cowave.sys.admin.core.entity.vo.TreeNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shanhuiming
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SysRedisHelper implements ApplicationRunner {
    public static final String KEY_CONFIG = "sys:config:";
    private static final String KEY_USER = "sys:tree:user";
    private static final String KEY_POST = "sys:tree:post";
    public static final String KEY_DEPT = "sys:tree:dept";
    private static final String KEY_DEPT_USER = "sys:tree:dept-user";
    private static final String KEY_DEPT_POST = "sys:tree:dept-post";

    private final TreeNodeConfig treeConfig = new TreeNodeConfig()
            .setIdKey("id").setParentIdKey("pid").setNameKey("label").setChildrenKey("children");
    private final Map<String, DictValueParser> parserMap = new ConcurrentHashMap<>();

    private final SysConfigDtoMapper sysConfigMapper;
    private final SysUserDtoMapper sysUserMapper;
    private final SysPostDtoMapper sysPostMapper;
    private final SysDeptDtoMapper sysDeptMapper;

    private final RedisHelper redisHelper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("init cache of config ...");
        refreshConfig();
        log.info("init cache of user tree ...");
        refreshUserTree();
        log.info("init cache of post tree ...");
        refreshPostTree();
        log.info("init cache of dept tree ...");
        refreshDeptTree();
        log.info("init cache of dept-user tree ...");
        refreshDeptUserTree();
        log.info("init cache of dept-post tree ...");
        refreshDeptPostTree();
    }

    public void refreshConfig() throws Exception {
        Collection<String> keys = redisHelper.keys(KEY_CONFIG + "*");
        redisHelper.delete(keys);
        List<SysConfigDto> list = sysConfigMapper.list(Access.page(Integer.MAX_VALUE), new SysConfigDto()).getRecords();
        for (SysConfigDto conf : list) {
            putConfig(conf);
        }
    }

    public void refreshUserTree() {
        List<TreeNode> list = sysUserMapper.listTree();
        List<Tree<String>> deptTree = TreeUtil.build(list, "0", treeConfig, (u, node) -> {
            node.setId(u.getId());
            node.setParentId(u.getPid());
            node.setName(u.getLabel());
            node.put("content", u.getContent());
        });
        redisHelper.putValue(KEY_USER, deptTree.get(0));
    }

    // id = postId
    public void refreshPostTree() {
        List<TreeNode> list = sysPostMapper.listTree();
        List<Tree<String>> deptTree = TreeUtil.build(list, "0", treeConfig, (p, node) -> {
            node.setId(p.getId());
            node.setParentId(p.getPid());
            node.setName(p.getLabel());
        });
        redisHelper.putValue(KEY_POST, deptTree.get(0));
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

    public void putConfig(SysConfigDto conf) throws Exception {
        String parserClazz = conf.getValueParser();
        if (StringUtils.isBlank(parserClazz)) {
            redisHelper.putValue(KEY_CONFIG + conf.getConfigKey(), conf.getConfigValue());
        } else {
            DictValueParser parser = parserMap.get(parserClazz);
            if (parser == null) {
                parser = (DictValueParser) Class.forName(parserClazz).getDeclaredConstructor().newInstance();
                parserMap.put(parserClazz, parser);
            }
            redisHelper.putValue(KEY_CONFIG + conf.getConfigKey(), parser.parse(conf.getConfigValue(), conf.getValueParam()));
        }
    }

    public <T> T getConfig(String configKey) {
        return redisHelper.getValue(KEY_CONFIG + configKey);
    }

    public void removeConfig(SysConfigDto conf) {
        redisHelper.delete(KEY_CONFIG + conf.getConfigKey());
    }

    public Tree<String> getUserTree() {
        return redisHelper.getValue(KEY_USER);
    }

    public Tree<String> getDeptUserTree() {
        return redisHelper.getValue(KEY_DEPT_USER);
    }

    public Tree<String> getPostTree() {
        return redisHelper.getValue(KEY_POST);
    }

    public Tree<String> getDeptPostTree() {
        return redisHelper.getValue(KEY_DEPT_POST);
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
}
