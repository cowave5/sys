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

import com.cowave.sys.admin.core.entity.dto.SysMenuDto;
import com.cowave.sys.admin.core.entity.dto.SysRoleDto;
import com.cowave.sys.admin.core.entity.vo.RoleAuthed;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单
 *
 * @author shanhuiming
 */
@Mapper
public interface SysMenuDtoMapper {

    /**
     * 所有菜单
     */
    List<SysMenuDto> list(@Param("visible") Integer visible,
                          @Param("menuName") String menuName, @Param("menuStatus") Integer menuStatus);

    /**
     * 用户菜单
     */
    List<SysMenuDto> userMenus(@Param("userId") Long userId, @Param("visible") Integer visible,
                               @Param("menuName") String menuName, @Param("menuStatus") Integer menuStatus);

    /**
     * OAuth菜单
     */
    List<SysMenuDto> oauthMenus(Long userId);

    /**
     * 详情
     */
    SysMenuDto info(Long menuId);

    /**
     * 新增
     */
    void insert(SysMenuDto sysMenu);

    /**
     * 更新
     */
    void update(SysMenuDto sysMenu);

    /**
     * 删除角色菜单
     */
    void deleteMenus(Long menuId);

    /**
     * 删除菜单
     */
    void deletes(Long menuId);

    /**
     * 已授权角色
     */
    List<SysRoleDto> roleAuthed(RoleAuthed roleAuthed);

    /**
     * 未授权角色
     */
    List<SysRoleDto> roleUnAuthed(RoleAuthed roleAuthed);

    /**
     * 授权角色
     */
    void grant(RoleAuthed roleAuthed);

    /**
     * 取消授权
     */
    void cancel(RoleAuthed roleAuthed);

    /**
     * 修改只读
     */
    void changeReadonly(SysMenuDto sysMenu);

    /**
     * 用户权限key
     */
    List<String> getPermitKeysByUserId(Long userId);
}
