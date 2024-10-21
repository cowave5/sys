/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.api.service.impl;

import java.util.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.tools.Asserts;
import com.cowave.sys.admin.api.service.SysPostService;
import com.cowave.sys.model.admin.SysUser;
import org.springframework.stereotype.Service;

import com.cowave.sys.admin.core.mapper.SysPostMapper;
import com.cowave.sys.model.admin.SysPost;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class SysPostServiceImpl implements SysPostService {

    private final SysPostMapper sysPostMapper;

    @Override
    public Page<SysPost> list(SysPost sysPost) {
        return sysPostMapper.list(Access.page(), sysPost);
    }

    @Override
    public SysPost info(Long postId) {
        return sysPostMapper.info(postId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(SysPost sysPost) {
        // 新增岗位
        sysPostMapper.insert(sysPost);
        // 上级岗位
        inputPostParents(sysPost, false);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysPost edit(SysPost sysPost) {
        Asserts.notNull(sysPost.getPostId(), "{post.notnull.id}");
        SysPost prePost = sysPostMapper.info(sysPost.getPostId());
        Asserts.notNull(prePost, "{post.notexist}", sysPost.getPostId());
        Asserts.notEquals(1, prePost.getReadOnly(), "{post.forbid.edit.readonly}", prePost.getPostName());
        // 更新岗位
        sysPostMapper.update(sysPost);
        // 上级岗位
        inputPostParents(sysPost, true);
        return prePost;
    }

    private void inputPostParents(SysPost sysPost, boolean overwrite){
        if(overwrite){
            sysPostMapper.deletePostParents(sysPost.getPostId());
        }
        Long parentId = sysPost.getParentId();
        if(parentId != null){
            if(overwrite){
                List<Long> childIds = sysPostMapper.childIds(sysPost.getPostId());
                childIds.add(sysPost.getPostId());
                Asserts.isFalse(childIds.contains(parentId), "{post.tree.cycle}");
            }
            sysPostMapper.insertPostParent(sysPost.getPostId(), parentId);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<SysPost> delete(Long[] postId) {
        Asserts.equals(0, sysPostMapper.countUserByIdArray(postId), "{post.forbid.delete.hasuser}");
        Asserts.equals(0, sysPostMapper.countReadOnlyByIdArray(postId), "{post.forbid.delete.readonly}");
        List<SysPost> list = sysPostMapper.queryByIdArray(postId);
        // 删除岗位
        sysPostMapper.delete(postId);
        // 上级岗位关系
        sysPostMapper.deletePostParentsByIdArray(postId);
        // 下级岗位关系
        sysPostMapper.deletePostChildsByIdArray(postId);
        return list;
    }

    @Override
    public void changeReadonly(SysPost sysPost) {
        sysPostMapper.changeReadonly(sysPost);
    }

    @Override
    public List<SysUser> getUsersByCode(String postCode) {
        return sysPostMapper.getUsersByCode(postCode);
    }
}
