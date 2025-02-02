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
import com.alibaba.fastjson.annotation.JSONField;
import com.cowave.commons.framework.access.security.AccessInfo;
import com.cowave.commons.framework.support.excel.SexConverter;
import com.cowave.commons.framework.support.excel.StatusConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * 用户
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
public class SysUserDto extends AccessInfo {

	/**
	 * 部门id
	 */
	private Integer deptId;

	/**
	 * 部门编码
	 */
	private String deptCode;

	/**
	 * 部门名称
	 */
	private String deptName;

	/**
	 * 岗位id
	 */
	private Integer postId;

	/**
	 * 岗位编码
	 */
	private Long postCode;

	/**
	 * 岗位名称
	 */
	private String postName;

	/**
	 * 角色id
	 */
	private Integer roleId;

	/**
	 * 角色编码
	 */
	private Long roleCode;

	/**
	 * 角色名称
	 */
	private String roleName;

	/**
	 * 上级用户id
	 */
	private Long parentId;

	/**
	 * 上级用户编码
	 */
	private Long parentCode;

	/**
	 * 上级用户名称
	 */
	private String parentName;

	/**
	 * 用户id
	 */
	private Integer userId;

	/**
	 * 用户类型 0:默认用户
	 */
	private int userType;

	/**
	 * 用户编码
	 */
	@ColumnWidth(50)
	@ExcelProperty("用户编码")
	private String userCode;

	/**
	 * 用户名称
	 */
	@ExcelProperty("用户名称")
	@NotBlank(message = "{user.notnull.name}")
	private String userName;

	/**
	 * 用户账号
	 */
	@ExcelProperty("用户账号")
	@NotBlank(message = "{user.notnull.account}")
	private String userAccount;

	/**
	 * 用户密码
	 */
	@JSONField(serialize = false)
	private String userPasswd;

	/**
	 * 用户性别
	 */
	@ExcelProperty(value = "用户性别", converter = SexConverter.class)
	private Integer userSex;

	/**
	 * 用户电话
	 */
	@ExcelProperty("用户电话")
	@Pattern(regexp = "^[1][3,4,5,7,8][0-9]{9}$", message = "user.invalid.phone")
	private String userPhone;

	/**
     * 用户邮箱
     */
    @ExcelProperty("用户邮箱")
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "user.invalid.email")
    private String userEmail;

	/**
	 * 用户职级
	 */
	private String rank;

	/**
	 * 是否只读
	 */
	private Integer readOnly;

	/**
	 * 用户状态
	 */
	@ExcelProperty(value = "用户状态", converter = StatusConverter.class)
	private Integer userStatus;

	/**
	 * 登录ip
	 */
	private String loginIp;

	/**
	 * 登录时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date loginTime;

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

	/**
	 * 当前页索引
	 */
	private Integer page;

	/**
	 * 页记录数
	 */
	private Integer pageSize;

	/**
	 * 偏移索引
	 */
	public Integer getOffset(){
		if(page == null || pageSize == null){
			return null;
		}
		return this.page > 0 ? (this.page - 1) * this.pageSize : 0;
	}

	/**
	 * 用户部门
	 */
	private List<SysUserDeptDto> deptList;

	/**
	 * 用户角色
	 */
	private List<SysUserRoleDto> roleList;

	/**
	 * 上级用户
	 */
	private List<SysUserParentDto> parentList;

	/**
	 * 上级用户id列表
	 */
	private List<Long> parentIds;

	/**
	 * 角色id列表
	 */
	private List<Long> roleIds;

	/**
	 * 部门-岗位id列表
	 */
	private List<String> deptPostIds;

	// 登录对于单位是获取的单值，这里尝试获取一个default单位，没有则随机取第一个
	public Integer getDeptId() {
		if(deptId != null){
			return deptId;
		}
		if(CollectionUtils.isEmpty(deptList)){
			return null;
		}
		for(SysUserDeptDto dept : deptList){
			if(dept.getIsDefault() == 1){
				return dept.getDeptId();
			}
		}
		return deptList.get(0).getDeptId();
	}

	public String getDeptName() {
		if(deptName != null){
			return deptName;
		}
		if(CollectionUtils.isEmpty(deptList)){
			return null;
		}
		for(SysUserDeptDto dept : deptList){
			if(dept.getIsDefault() == 1){
				return dept.getDeptName();
			}
		}
		return deptList.get(0).getDeptName();
	}
}
