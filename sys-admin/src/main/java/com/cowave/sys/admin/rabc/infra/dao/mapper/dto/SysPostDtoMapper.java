/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.rabc.infra.dao.mapper.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.rabc.domain.dto.SysPostDto;
import com.cowave.sys.admin.rabc.domain.dto.SysUserDto;
import com.cowave.sys.admin.base.domain.vo.TreeChildren;
import com.cowave.sys.admin.base.domain.vo.TreeNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shanhuiming
 */
@Mapper
public interface SysPostDtoMapper {

    /**
     * 部门岗位选项
     */
    List<TreeChildren> deptPostOptions();

    /**
     * 列表
     */
    Page<SysPostDto> list(Page<SysPostDto> page, @Param("post") SysPostDto sysPost);

    /**
     * 详情
     */
    SysPostDto info(Integer postId);

    /**
     * 新增
     */
    void insert(SysPostDto sysPost);

    /**
     * 更新
     */
    int update(SysPostDto sysPost);

    /**
     * 用户计数
     */
    int countUserByIdArray(Long[] array);

    /**
     * 只读计数
     */
    int countReadOnlyByIdArray(Long[] array);

    /**
     * 删除
     */
    void delete(Long[] array);

    /**
     * 删除上级岗位关系
     */
    void deletePostParentsByIdArray(Long[] array);

    /**
     * 删除上级岗位关系
     */
    void deletePostParents(Integer postId);

    /**
     * 删除下级岗位关系
     */
    void deletePostChildsByIdArray(Long[] array);

    /**
     * 下级岗位id列表
     */
    List<Integer> childIds(Integer postId);

    /**
     * 插入上级岗位
     */
    void insertPostParent(@Param("postId") Integer postId, @Param("parentId") long parentId);

    /**
     * 岗位关系
     */
    List<TreeNode> listTree();

    /**
     * id列表查询
     */
    List<SysPostDto> queryByIdArray(Long[] array);

    /**
     * 修改只读
     */
    void changeReadonly(SysPostDto sysPost);

    /**
     * 岗位人员，code获取
     */
    List<SysUserDto> getUsersByCode(String postCode);
}
