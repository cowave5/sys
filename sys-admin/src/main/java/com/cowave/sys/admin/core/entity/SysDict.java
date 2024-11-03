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
public class SysDict {

    /**
	 * id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 上级字典码
	 */
	private String parentCode;

    /**
     * 字典码
     */
    private String dictCode;

    /**
	 * 字典名称
	 */
	private String dictLabel;

	/**
	 * 英文名称
	 */
	private String dictEn;

    /**
     * 字典值
     */
    private Object dictValue;

	/**
	 * 值转换器
	 */
	private String valueParser;

    /**
	 * 转换参数
	 */
	private String valueParam;

    /**
	 * 字典排序
	 */
	private Integer dictOrder;

    /**
     * 是否默认值
     */
    private Integer isDefault = 0;

	/**
	 * css样式
	 */
	private String css;

    /**
     * 状态
     */
    private Integer status;

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
