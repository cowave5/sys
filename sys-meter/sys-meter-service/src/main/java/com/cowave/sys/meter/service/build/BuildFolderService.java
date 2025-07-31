package com.cowave.sys.meter.service.build;

import cn.hutool.core.lang.tree.Tree;
import com.cowave.sys.meter.domain.build.BuildFolder;
import com.cowave.sys.meter.domain.build.request.FolderDrag;
import com.cowave.sys.meter.domain.build.request.FolderRename;
import com.cowave.sys.meter.domain.build.request.VisibilityUpdate;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface BuildFolderService {

    /**
     * 目录结构
     */
    List<Tree<Integer>> tree();

    /**
     * 目录拖拽
     */
    void drag(FolderDrag folderDrag);

    /**
     * 新增
     */
    void create(BuildFolder buildFolder);

    /**
     * 删除
     */
    void delete(@PathVariable Integer folderId);

    /**
     * 修改目录名称
     */
    void rename(FolderRename folderRename);

    /**
     * 修改目录访问限制
     */
    void updateVisibility(VisibilityUpdate visibilityUpdate);
}
