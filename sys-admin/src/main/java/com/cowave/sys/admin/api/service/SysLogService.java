/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.api.entity.LogQuery;
import com.cowave.sys.model.admin.SysLog;

/**
 *
 * @author shanhuiming
 *
 */
public interface SysLogService {

	/**
	 * 新增
	 */
	void add(SysLog sysLog);

	/**
	 * 列表
	 */
	Page<SysLog> list(SysLog sysLog);

	/**
	 * 详情
	 */
	SysLog info(Long id);

	/**
	 * 删除
	 */
	void delete(Long[] operId);

	/**
	 * 清空
	 */
	void clean();

	/**
	 * 用户日志信息查询
	 */
	LogQuery userLogQuery(LogQuery logQuery);

	/**
	 * 部门日志信息查询
	 */
	LogQuery deptLogQuery(LogQuery logQuery);

	/**
	 * 岗位日志信息查询
	 */
	LogQuery postLogQuery(LogQuery logQuery);
}
