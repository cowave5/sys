/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.domain.base.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 告警类型
 *
 * @author shanhuiming
 */
@Data
public class SysAlarmTypeDto {

	/**
	 * 主键id
	 */
	private Long id;

	/**
	 * 类型名称
	 */
	@NotBlank(message = "{admin.alarm.type.name.null}")
	private String typeName;

	/**
	 * 详情路径
	 */
	private String typeView;

	/**
	 * 描述
	 */
	private String description;
}
