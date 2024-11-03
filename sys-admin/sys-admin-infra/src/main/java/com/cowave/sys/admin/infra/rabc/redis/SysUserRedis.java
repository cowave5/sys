package com.cowave.sys.admin.infra.rabc.redis;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.sys.admin.domain.base.vo.TreeNode;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysUserDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author shanhuiming
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SysUserRedis implements ApplicationRunner {
    private static final String KEY_USER = "sys:tree:user";

    private final TreeNodeConfig treeConfig = new TreeNodeConfig()
            .setIdKey("id").setParentIdKey("pid").setNameKey("label").setChildrenKey("children");
    private final RedisHelper redisHelper;
    private final SysUserDtoMapper sysUserMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("init cache of user tree ...");
        refreshUserTree();
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

    public Tree<String> getUserTree() {
        return redisHelper.getValue(KEY_USER);
    }
}
