/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.dao.mapper.dto;

import com.cowave.sys.admin.core.entity.SysDept;
import com.cowave.sys.admin.core.entity.dto.*;
import com.cowave.sys.admin.core.entity.request.DeptQuery;
import com.cowave.sys.admin.core.entity.vo.TreeNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shanhuiming
 */
@Mapper
public interface SysDeptDtoMapper {

    /**
     * 列表
     */
    List<SysDeptListDto> list(DeptQuery deptQuery);

    /**
     * 详情
     */
    SysDeptInfoDto info(Integer deptId);

    /**
     * 部门成员
     */
    List<SysDeptUserDto> members(@Param("deptId") Integer deptId, @Param("limit") int limit, @Param("offset") int offset);

    /**
     * 删除上级部门
     */
    void deleteDeptParents(Integer deptId);

    /**
     * 下级部门id列表
     */
    List<Integer> childIds(Integer deptId);

    /**
     * 树
     */
    List<TreeNode> listTree();

    /**
     * 插入上级部门
     */
    void insertDeptParents(@Param("deptId") Integer deptId, @Param("list") List<Long> list);

    /**
     * 下级部门计数
     */
    int countChildByIdArray(Long[] array);

    /**
     * 只读计数
     */
    int countReadOnlyByIdArray(Long[] array);

    /**
     * id列表查询
     */
    List<SysDeptDto> queryByIdArray(Long[] array);

    /**
     * 删除
     */
    int deleteByIdArray(Long[] array);

    /**
     * 修改只读
     */
    void changeReadonly(SysDeptDto sysDept);

    /**
     * 删除上级部门关系
     */
    void deleteDeptParentsByIdArray(Long[] array);

    /**
     * 删除部门用户关系
     */
    void deleteDeptUserByIdArray(Long[] array);

    /**
     * 获取部门岗位
     */
    List<SysDeptPostDto> getPostsById(Integer deptId);

    /**
     * 删除部门岗位
     */
    void deleteDeptPosts(Integer deptId);

    /**
     * 插入部门岗位
     */
    void insertDeptPosts(List<SysDeptPostDto> list);

    /**
     * 部门人员，id获取
     */
    List<SysUserDeptDto> getUsersById(Integer deptId);

    /**
     * 删除部门人员
     */
    void deleteDeptUsers(Integer deptId);

    /**
     * 插入部门人员
     */
    void insertDeptUsers(List<SysUserDeptDto> list);

    /**
     * 部门人员，code获取
     */
    List<SysUserDto> getUsersByCode(String deptCode);

    /**
     * 获取用户默认部门
     */
    SysDept getDefaultDeptOfUser(Integer userId);
}
