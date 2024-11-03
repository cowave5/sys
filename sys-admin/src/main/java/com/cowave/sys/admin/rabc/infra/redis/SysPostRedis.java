package com.cowave.sys.admin.rabc.infra.redis;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.cowave.commons.framework.helper.redis.RedisHelper;
import com.cowave.sys.admin.base.domain.vo.TreeNode;
import com.cowave.sys.admin.rabc.infra.dao.mapper.dto.SysPostDtoMapper;
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
public class SysPostRedis implements ApplicationRunner {
    private static final String KEY_POST = "sys:tree:post";

    private final TreeNodeConfig treeConfig = new TreeNodeConfig()
            .setIdKey("id").setParentIdKey("pid").setNameKey("label").setChildrenKey("children");
    private final RedisHelper redisHelper;
    private final SysPostDtoMapper sysPostMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("init cache of post tree ...");
        refreshPostTree();
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

    public Tree<String> getPostTree() {
        return redisHelper.getValue(KEY_POST);
    }
}
