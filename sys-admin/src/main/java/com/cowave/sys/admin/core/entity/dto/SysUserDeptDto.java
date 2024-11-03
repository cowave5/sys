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

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 用户部门
 *
 * @author shanhuiming
 *
 */
@Data
public class SysUserDeptDto {

	/**
	 * 用户id
	 */
	@NotNull(message = "{user.notnull.id}")
	private Integer userId;

	/**
	 * 用户编码
	 */
	private String userCode;

	/**
	 * 用户名称
	 */
	private String userName;

	/**
	 * 部门id
	 */
	@NotNull(message = "{dept.notnull.id}")
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
	@NotNull(message = "{post.notnull.id}")
	private Integer postId;

	/**
	 * 岗位编码
	 */
	private String postCode;

	/**
	 * 岗位名称
	 */
	private String postName;

	/**
	 * 是否用户默认单位
	 */
	private Integer isDefault = 0;

	/**
	 * 是否单位负责人
	 */
	private Integer isLeader = 0;

	public static SysUserDeptDto parseDeptPost(String deptPostId, Integer userId){
		String[] arr = deptPostId.split("-");
		SysUserDeptDto userDept = new SysUserDeptDto();
		userDept.setUserId(userId);
		userDept.setDeptId(Integer.parseInt(arr[0]));
		userDept.setPostId(Integer.parseInt(arr[1]));
		return userDept;
	}
}
