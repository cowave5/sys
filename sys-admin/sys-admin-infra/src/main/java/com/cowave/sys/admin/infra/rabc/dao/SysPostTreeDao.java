/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.rabc.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.admin.domain.rabc.SysPostTree;
import com.cowave.sys.admin.infra.rabc.dao.mapper.SysPostTreeMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class SysPostTreeDao extends ServiceImpl<SysPostTreeMapper, SysPostTree> {

    /**
     * 删除上级岗位关系
     */
    public void deleteParentById(Integer postId){
        lambdaUpdate().eq(SysPostTree::getPostId, postId).remove();
    }

    /**
     * 删除上级岗位关系
     */
    public void deleteParentByIds(List<Integer> postIds){
        lambdaUpdate().in(SysPostTree::getPostId, postIds).remove();
    }

    /**
     * 删除下级岗位关系
     */
    public void deleteChildByIds(List<Integer> postIds){
        lambdaUpdate().in(SysPostTree::getParentId, postIds).remove();
    }
}
