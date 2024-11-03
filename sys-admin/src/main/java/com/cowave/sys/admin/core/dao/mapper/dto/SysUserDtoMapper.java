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

import com.cowave.sys.admin.core.entity.SysUser;
import com.cowave.sys.admin.core.entity.dto.SysUserInfoDto;
import com.cowave.sys.admin.core.entity.dto.SysUserListDto;
import com.cowave.sys.admin.core.entity.dto.SysUserNameDto;
import com.cowave.sys.admin.core.entity.request.UserQuery;
import com.cowave.sys.admin.core.entity.vo.TreeChildren;
import com.cowave.sys.admin.core.entity.vo.TreeNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户
 *
 * @author shanhuiming
 */
@Mapper
public interface SysUserDtoMapper {

    /**
     * 列表
     */
    List<SysUserListDto> list(UserQuery query);

    /**
     * 计数
     */
    int count(UserQuery query);

    /**
     * 单位用户列表
     */
    List<SysUserListDto> listOfDept(UserQuery query);

    /**
     * 单位用户计数
     */
    int countOfDept(UserQuery query);

    /**
     * 详情
     */
    SysUserInfoDto getById(Integer userId);

    /**
     * 下级用户id列表
     */
    List<Integer> childIds(Integer userId);

    /**
     * 用户权限符
     */
    List<String> permitKeys(Integer userId);

    /**
     * 导入用户
     */
    void batchInsert(@Param("list") List<SysUser> list, @Param("overwrite") boolean overwrite);

    /**
     * 部门用户树
     */
    List<TreeChildren> deptUserOptions();

    /**
     * 人员关系
     */
    List<TreeNode> listTree();

    /**
     * id查询姓名
     */
    String queryNameById(Integer userId);

    /**
     * 候选人列表：上级用户
     */
    List<SysUserNameDto> leaders(Integer userId);
}
