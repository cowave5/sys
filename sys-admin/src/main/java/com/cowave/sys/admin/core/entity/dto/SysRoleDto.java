/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.entity.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.cowave.commons.framework.access.security.AccessInfo;
import com.cowave.commons.framework.access.security.Permission;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 角色
 *
 * @author shanhuiming
 */
@ExcelIgnoreUnannotated
@HeadRowHeight(20)
@ColumnWidth(20)
@HeadFontStyle(fontHeightInPoints = 10)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@Data
@EqualsAndHashCode(callSuper = false)
public class SysRoleDto extends AccessInfo {

	/**
	 * 角色id
	 */
	private Long roleId;

	/**
	 * 角色编码
	 */
	@NotBlank(message = "{role.notnull.code}")
	@ExcelProperty(value = "角色编码")
	private String roleCode;

	/**
	 * 角色名称
	 */
	@NotBlank(message = "{role.notnull.name}")
	@ExcelProperty(value = "角色名称")
	private String roleName;

	/**
	 * 角色类型
	 */
	@ExcelProperty(value = "角色类型")
	private String roleType;

	/**
	 * 是否只读
	 */
	private Integer readOnly;

	/**
	 * 创建人
	 */
	private String createBy;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 更新人
	 */
	private String updateBy;

	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	/**
	 * 备注
	 */
	@ExcelProperty("备注")
	private String remark;

	/**
	 * 角色菜单
	 */
	private List<Long> menuIds = new ArrayList<>();

	public boolean isAdmin(){
		return Permission.ROLE_ADMIN.equals(roleCode);
	}
}
