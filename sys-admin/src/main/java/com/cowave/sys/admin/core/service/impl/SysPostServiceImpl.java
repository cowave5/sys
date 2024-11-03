/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.service.impl;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.response.exception.Asserts;
import com.cowave.sys.admin.core.SysRedisHelper;
import com.cowave.sys.admin.core.service.SysPostService;
import com.cowave.sys.admin.core.dao.mapper.dto.SysPostDtoMapper;
import com.cowave.sys.admin.core.entity.dto.SysPostDto;
import com.cowave.sys.admin.core.entity.dto.SysUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class SysPostServiceImpl implements SysPostService {
    private final SysRedisHelper sysRedisHelper;
    private final SysPostDtoMapper sysPostMapper;

    @Override
    public Page<SysPostDto> list(SysPostDto sysPost) {
        return sysPostMapper.list(Access.page(), sysPost);
    }

    @Override
    public SysPostDto info(Integer postId) {
        return sysPostMapper.info(postId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(SysPostDto sysPost) {
        // 新增岗位
        sysPostMapper.insert(sysPost);
        // 上级岗位
        inputPostParents(sysPost, false);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysPostDto edit(SysPostDto sysPost) {
        Asserts.notNull(sysPost.getPostId(), "{post.notnull.id}");
        SysPostDto prePost = sysPostMapper.info(sysPost.getPostId());
        Asserts.notNull(prePost, "{post.notexist}", sysPost.getPostId());
        Asserts.notEquals(1, prePost.getReadOnly(), "{post.forbid.edit.readonly}", prePost.getPostName());
        // 更新岗位
        sysPostMapper.update(sysPost);
        // 上级岗位
        inputPostParents(sysPost, true);
        return prePost;
    }

    private void inputPostParents(SysPostDto sysPost, boolean overwrite){
        if(overwrite){
            sysPostMapper.deletePostParents(sysPost.getPostId());
        }
        Integer parentId = sysPost.getParentId();
        if(parentId != null){
            if(overwrite){
                List<Integer> childIds = sysPostMapper.childIds(sysPost.getPostId());
                childIds.add(sysPost.getPostId());
                Asserts.isFalse(childIds.contains(parentId), "{post.tree.cycle}");
            }
            sysPostMapper.insertPostParent(sysPost.getPostId(), parentId);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<SysPostDto> delete(Long[] postId) {
        Asserts.equals(0, sysPostMapper.countUserByIdArray(postId), "{post.forbid.delete.hasuser}");
        Asserts.equals(0, sysPostMapper.countReadOnlyByIdArray(postId), "{post.forbid.delete.readonly}");
        List<SysPostDto> list = sysPostMapper.queryByIdArray(postId);
        // 删除岗位
        sysPostMapper.delete(postId);
        // 上级岗位关系
        sysPostMapper.deletePostParentsByIdArray(postId);
        // 下级岗位关系
        sysPostMapper.deletePostChildsByIdArray(postId);
        return list;
    }

    @Override
    public void changeReadonly(SysPostDto sysPost) {
        sysPostMapper.changeReadonly(sysPost);
    }

    @Override
    public List<SysUserDto> getUsersByCode(String postCode) {
        return sysPostMapper.getUsersByCode(postCode);
    }

    @Override
    public void refreshPostTree() {
        sysRedisHelper.refreshDeptPostTree();
    }

    @Override
    public Tree<String> getPostTree() {
        return sysRedisHelper.getPostTree();
    }

    @Override
    public Tree<String> getDeptPostTree() {
        return sysRedisHelper.getDeptPostTree();
    }
}
