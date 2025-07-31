package com.cowave.sys.meter.app.build;

import cn.hutool.core.lang.tree.Tree;
import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.meter.domain.build.BuildFolder;
import com.cowave.sys.meter.domain.build.request.FolderDrag;
import com.cowave.sys.meter.domain.build.request.FolderRename;
import com.cowave.sys.meter.domain.build.request.VisibilityUpdate;
import com.cowave.sys.meter.service.build.BuildFolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 构建目录
 *
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/build/folder")
public class BuildFolderController {

    private final BuildFolderService buildFolderService;

    /**
     * 目录结构
     */
    @GetMapping
    public Response<List<Tree<Integer>>> tree() {
        return Response.success(buildFolderService.tree());
    }

    /**
     * 目录拖拽
     */
    @PatchMapping("/drag")
    public Response<Void> drag(@Validated @RequestBody FolderDrag folderDrag) throws Exception {
        return Response.success(() -> buildFolderService.drag(folderDrag));
    }

    /**
     * 新增
     */
    @PostMapping
    public Response<Void> create(@Validated @RequestBody BuildFolder buildFolder) throws Exception {
        return Response.success(() -> buildFolderService.create(buildFolder));
    }

    /**
     * 删除
     */
    @DeleteMapping("/{folderId}")
    public Response<Void> delete(@PathVariable Integer folderId) throws Exception {
        return Response.success(() -> buildFolderService.delete(folderId));
    }

    /**
     * 修改目录名称
     */
    @PatchMapping("/name")
    public Response<Void> rename(@Validated @RequestBody FolderRename folderRename) throws Exception {
        return Response.success(() -> buildFolderService.rename(folderRename));
    }

    /**
     * 修改访问限制
     */
    @PatchMapping("/visibility")
    public Response<Void> updateVisibility(@Validated @RequestBody VisibilityUpdate visibilityUpdate) throws Exception {
        return Response.success(() -> buildFolderService.updateVisibility(visibilityUpdate));
    }

    /**
     * 设置目录成员
     */
    @PatchMapping("/members")
    public Response<Void> updateMembers(@Validated @RequestBody VisibilityUpdate visibilityUpdate) throws Exception {
        return Response.success(() -> buildFolderService.updateVisibility(visibilityUpdate));
    }
}
