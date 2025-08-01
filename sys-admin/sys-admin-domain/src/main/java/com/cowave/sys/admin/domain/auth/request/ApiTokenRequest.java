/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.domain.auth.request;

import com.cowave.sys.admin.domain.auth.ApiToken;
import com.cowave.sys.admin.domain.rabc.dto.SysMenuScope;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class ApiTokenRequest extends ApiToken {

    /**
	 * 菜单id列表
	 */
	private List<SysMenuScope> menuScopes = new ArrayList<>();
}
