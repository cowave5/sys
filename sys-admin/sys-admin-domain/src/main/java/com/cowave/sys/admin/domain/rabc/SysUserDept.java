/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.domain.rabc;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author shanhuiming
 */
@NoArgsConstructor
@Data
public class SysUserDept {

    /**
	 * 用户id
	 */
	@NotNull(message = "{admin.user.id.null}")
	private Integer userId;

    /**
	 * 部门id
	 */
	@NotNull(message = "{admin.dept.id.null}")
	private Integer deptId;

    /**
	 * 岗位id
	 */
	private Integer postId;

    /**
	 * 用户默认单位
	 */
	private Integer isDefault = 0;

	/**
	 * 单位负责人
	 */
	private Integer isLeader = 0;

	public SysUserDept(Integer userId, Integer deptId, Integer postId){
		this.userId = userId;
		this.deptId = deptId;
		this.postId = postId;
	}
}
