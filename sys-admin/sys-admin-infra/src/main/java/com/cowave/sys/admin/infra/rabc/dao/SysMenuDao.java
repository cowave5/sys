/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.rabc.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.domain.rabc.SysMenu;
import com.cowave.sys.admin.infra.rabc.dao.mapper.SysMenuMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class SysMenuDao extends ServiceImpl<SysMenuMapper, SysMenu> {

    public List<SysMenu> queryList(Integer visible, Integer menuStatus, String menuName) {
        return lambdaQuery()
                .eq(visible != null, SysMenu::getIsVisible, visible)
                .eq(menuStatus != null, SysMenu::getMenuStatus, menuStatus)
                .like(StringUtils.isNotBlank(menuName), SysMenu::getMenuName, menuName)
                .orderByAsc(SysMenu::getParentId)
                .orderByAsc(SysMenu::getMenuOrder)
                .list();
    }

    /**
     * 修改菜单信息
     */
    public void updateMenu(SysMenu sysMenu) {
        lambdaUpdate().eq(SysMenu::getMenuId, sysMenu.getMenuId())
                .set(SysMenu::getUpdateUser, Access.userId())
                .set(SysMenu::getUpdateDept, Access.deptId())
                .set(SysMenu::getUpdateTime, new Date())
                .set(SysMenu::getParentId, sysMenu.getParentId())
                .set(SysMenu::getMenuName, sysMenu.getMenuName())
                .set(SysMenu::getMenuOrder, sysMenu.getMenuOrder())
                .set(SysMenu::getMenuPermit, sysMenu.getMenuPermit())
                .set(SysMenu::getMenuPath, sysMenu.getMenuPath())
                .set(SysMenu::getMenuParam, sysMenu.getMenuParam())
                .set(SysMenu::getMenuType, sysMenu.getMenuType())
                .set(SysMenu::getMenuIcon, sysMenu.getMenuIcon())
                .set(SysMenu::getMenuStatus, sysMenu.getMenuStatus())
                .set(SysMenu::getComponent, sysMenu.getComponent())
                .set(SysMenu::getIsFrame, sysMenu.getIsFrame())
                .set(SysMenu::getIsCache, sysMenu.getIsCache())
                .set(SysMenu::getIsVisible, sysMenu.getIsVisible())
                .set(SysMenu::getIsProtected, sysMenu.getIsProtected())
                .set(SysMenu::getRemark, sysMenu.getRemark())
                .update();
    }

    /**
     * 修改菜单只读
     */
    public void updateReadonly(Integer menuId, Integer readonly){
        lambdaUpdate().eq(SysMenu::getMenuId, menuId)
                .set(SysMenu::getUpdateUser, Access.userId())
                .set(SysMenu::getUpdateDept, Access.deptId())
                .set(SysMenu::getUpdateTime, new Date())
                .set(SysMenu::getReadOnly, readonly)
                .update();
    }
}
