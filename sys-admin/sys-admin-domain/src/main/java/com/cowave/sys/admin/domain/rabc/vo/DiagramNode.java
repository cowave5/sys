package com.cowave.sys.admin.domain.rabc.vo;

import cn.hutool.core.lang.tree.TreeNodeConfig;
import lombok.Data;

/**
 * @author shanhuiming
 */
@Data
public class DiagramNode {

    public static final TreeNodeConfig DIAGRAM_CONFIG = new TreeNodeConfig()
            .setIdKey("id").setParentIdKey("pid").setNameKey("label").setChildrenKey("children");

    /**
     * id
     */
    private Integer id;

    /**
     * 上级id
     */
    private Integer pid;

    /**
     * 节点名称
     */
    private String label;

    public static DiagramNode newRootNode(String nodeName){
        DiagramNode rootNode = new DiagramNode();
		rootNode.setPid(-1);
		rootNode.setId(0);
		rootNode.setLabel(nodeName);
        return rootNode;
    }
}
