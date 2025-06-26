package com.cowave.sys.admin.domain.rabc.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shanhuiming
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserDiagramNode extends DiagramNode {

    /**
     * 用户职级
     */
    private String userRank;

    public static UserDiagramNode newRootNode(String nodeName){
        UserDiagramNode rootNode = new UserDiagramNode();
		rootNode.setPid(-1);
		rootNode.setId(0);
		rootNode.setLabel(nodeName);
        return rootNode;
    }
}
