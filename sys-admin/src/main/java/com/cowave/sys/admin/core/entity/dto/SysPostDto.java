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
import com.cowave.commons.framework.support.excel.StatusConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 岗位
 *
 * @author shanhuiming
 */
@ExcelIgnoreUnannotated
@HeadRowHeight(20)
@ColumnWidth(20)
@HeadFontStyle(fontHeightInPoints = 10)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@Data
@EqualsAndHashCode(callSuper=false)
public class SysPostDto extends AccessInfo {

	/**
	 * 部门id
	 */
	private Integer deptId;

	/**
	 * 部门名称
	 */
	private String deptName;

	/**
	 * 上级岗位id
	 */
	private Integer parentId;

	/**
	 * 岗位id
	 */
	private Integer postId;

	/**
	 * 岗位编码
	 */
	@ColumnWidth(50)
	@ExcelProperty(value = "岗位编码")
	private String postCode;

	/**
	 * 岗位类型
	 */
	@ExcelProperty(value = "岗位类型")
	private String postType;

	/**
	 * 岗位名称
	 */
	@NotBlank(message = "{post.notnull.name}")
	@ExcelProperty(value = "岗位名称")
	private String postName;

	/**
	 * 岗位级别
	 */
	private Integer postLevel;

	/**
	 * 岗位状态
	 */
	@ExcelProperty(value = "岗位状态", converter = StatusConverter.class)
	private Integer postStatus;

	/**
	 * 是否只读
	 */
	private Integer readOnly;

	/**
	 * 创建人
	 */
	private Integer createUser;

	/**
	 * 创建部门
	 */
	private Integer createDept;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 更新人
	 */
	private Integer updateUser;

	/**
	 * 更新部门
	 */
	private Integer updateDept;

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

}