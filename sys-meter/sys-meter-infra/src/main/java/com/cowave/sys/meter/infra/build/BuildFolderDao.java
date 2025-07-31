package com.cowave.sys.meter.infra.build;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.build.BuildFolder;
import com.cowave.sys.meter.domain.constants.Visibility;
import com.cowave.sys.meter.infra.build.mapper.BuildFolderMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class BuildFolderDao extends ServiceImpl<BuildFolderMapper, BuildFolder> {

    /**
     * 列表
     */
    public List<BuildFolder> list() {
        return lambdaQuery()
                .orderByAsc(BuildFolder::getParentId)
                .orderByAsc(BuildFolder::getFolderOrder)
                .list();
    }

    /**
     * 修改名称
     */
    public void updateNameById(Integer folderId, String folderName) {
        lambdaUpdate()
                .eq(BuildFolder::getFolderId, folderId)
                .set(BuildFolder::getFolderName, folderName)
                .update();
    }

    /**
     * 修改访问限制
     */
    public void updateVisibilityById(Integer folderId, Visibility visibility) {
        lambdaUpdate()
                .eq(BuildFolder::getFolderId, folderId)
                .set(BuildFolder::getVisibility, visibility)
                .update();
    }
}
