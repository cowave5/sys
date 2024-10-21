/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.mapper;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.model.admin.SysLog;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

/**
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface SysLogMapper {

	/**
	 * 列表
	 */
	Page<SysLog> list(Page<SysLog> page, @Param("log") SysLog sysLog);

	/**
	 * 新增
	 */
	void insert(SysLog sysLog);

	/**
	 * 详情
	 */
	SysLog info(long id);

	/**
	 * 删除
	 */
	void delete(Long[] array);

	/**
	 * 清空
	 */
	void clean();

	/**
	 * 查询角色名称
	 */
	List<String> queryNameByRoleIds(List<Long> list);

	/**
	 * 查询用户名称
	 */
	List<String> queryNameByUserIds(List<Long> list);

	/**
	 * 查询部门名称
	 */
	List<String> queryNameByDeptIds(List<Long> list);

	/**
	 * 查询岗位名称
	 */
	String queryNameByPostId(Long postId);

	/**
	 * 查询部门岗位名称
	 */
	String queryNameOfDeptPost(@Param("deptId") Long deptId, @Param("postId") Long postId);
}
