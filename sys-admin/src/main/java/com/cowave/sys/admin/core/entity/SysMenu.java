/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author shanhuiming
 */
@Data
public class SysMenu {

    /**
     * 菜单id
     */
    @TableId(type = IdType.AUTO)
    private Long menuId;

    /**
     * 上级id
     */
    private Long parentId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单排序
     */
    private Integer menuOrder;

    /**
     * 菜单权限符
     */
    private String menuPermit;

    /**
     * 路由地址
     */
    private String menuPath;

    /**
     * 路由参数
     */
    private String menuParam;

    /**
     * 菜单类型
     */
    private String menuType;

    /**
     * 菜单图标
     */
    private String menuIcon;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 菜单状态
     */
    private Integer menuStatus;

    /**
     * 是否内部链接
     */
    private Integer isFrame;

    /**
     * 是否缓存
     */
    private Integer isCache;

    /**
     * 是否显示
     */
    private Integer isVisible;

    /**
     * 是否受保护
     */
    private Integer isProtected;

    /**
     * 是否只读
     */
    private Integer readOnly;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建部门
     */
    private Long createDept;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    private Long updateUser;

    /**
     * 更新部门
     */
    private Long updateDept;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
