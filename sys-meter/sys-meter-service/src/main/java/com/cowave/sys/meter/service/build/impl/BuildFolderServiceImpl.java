package com.cowave.sys.meter.service.build.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.cowave.sys.meter.domain.build.BuildFolder;
import com.cowave.sys.meter.domain.build.request.FolderDrag;
import com.cowave.sys.meter.domain.build.request.FolderRename;
import com.cowave.sys.meter.domain.build.request.VisibilityUpdate;
import com.cowave.sys.meter.infra.build.BuildFolderDao;
import com.cowave.sys.meter.infra.build.mapper.dto.BuildDtoMapper;
import com.cowave.sys.meter.service.build.BuildFolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cowave.sys.meter.domain.constants.Visibility.PRIVATE;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class BuildFolderServiceImpl implements BuildFolderService {
    public static final TreeNodeConfig DIAGRAM_CONFIG = new TreeNodeConfig()
            .setIdKey("id").setParentIdKey("pid").setNameKey("label").setChildrenKey("children");
    private final BuildFolderDao buildFolderDao;
    private final BuildDtoMapper buildDtoMapper;

    @Override
    public List<Tree<Integer>> tree() {
        List<BuildFolder> folderList =  buildFolderDao.list();
        return TreeUtil.build(folderList, 0, DIAGRAM_CONFIG, (f, node) -> {
            node.setId(f.getFolderId());
            node.setParentId(f.getParentId());
            node.setName(f.getFolderName());
            node.setWeight(f.getFolderOrder());
            node.put("visibility", f.getVisibility().getVal());
        });
    }

    @Override
    public void drag(FolderDrag folderDrag) {
        List<BuildFolder> folderList = folderDrag.getFolderList();
        buildFolderDao.updateBatchById(folderList);
    }

    @Override
    public void create(BuildFolder buildFolder) {
        Integer parentId = buildFolder.getParentId();
        Integer nextOrder = buildDtoMapper.nextIndexInFolder(parentId);
        buildFolder.setFolderOrder(nextOrder);
        buildFolderDao.save(buildFolder);
        // private则同步上级成员
    }

    @Override
    public void delete(Integer folderId) {
        List<Integer> folderIdList = buildDtoMapper.folderChildrenIds(folderId);
        buildFolderDao.removeBatchByIds(folderIdList);
    }

    @Override
    public void rename(FolderRename folderRename) {
        buildFolderDao.updateNameById(folderRename.getFolderId(), folderRename.getFolderName());
    }

    @Override
    public void updateVisibility(VisibilityUpdate visibilityUpdate) {
        BuildFolder buildFolder = buildFolderDao.getById(visibilityUpdate.getFolderId());
        if(buildFolder.getVisibility() == visibilityUpdate.getVisibility()){
            return;
        }

        // private -> public
        if(PRIVATE == buildFolder.getVisibility()){
            // 删除目录成员

        }else{
            // public -> private
            // 目录成员设置自己

            // 下级设置private，成员设置自己
        }
        buildFolderDao.updateVisibilityById(visibilityUpdate.getFolderId(), visibilityUpdate.getVisibility());
    }
}
