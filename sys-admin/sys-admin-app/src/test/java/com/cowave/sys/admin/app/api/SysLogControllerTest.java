/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.app.api;

import com.cowave.commons.framework.access.filter.AccessFilter;
import com.cowave.commons.framework.access.security.BearerTokenFilter;
import com.cowave.sys.admin.app.SpringTest;
import com.cowave.sys.admin.app.base.SysOperationController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @author shanhuiming
 */
public class SysLogControllerTest  extends SpringTest {

    @Autowired
    private SysOperationController sysOperationController;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(sysOperationController)
                .addFilter(new AccessFilter(transactionIdSetter, accessIdGenerator, accessProperties, objectMapper))
                .addFilter(new BearerTokenFilter(true, bearerTokenService, null, null))
                .setControllerAdvice(accessAdvice).build();
    }

    /**
     * 列表
     */
    @Test
    public void list() throws Exception {
        mockPost("/api/v1/log/list", "{\"page\":1,\"pageSize\":1}");
    }

    /**
     * 详情
     */
    @Test
    public void info() throws Exception {
        mockGet("/api/v1/log/info/1");
    }

    /**
     * 删除
     */
    @Test
    public void delete() throws Exception {
        mockGet("/api/v1/log/delete?id=1");
    }

    /**
     * 清空
     */
    @Test
    public void clean() throws Exception {
        mockGet("/api/v1/log/clean");
    }

    /**
     * 导出
     */
    @Test
    public void export() throws Exception {
        mockExport("/api/v1/log/export", "{\"page\":1,\"pageSize\":1}", "target/log.xlsx");
    }

    /**
     * 用户日志信息查询
     */
    @Test
    public void userLogQuery() throws Exception {
        mockPost("/api/v1/log/user/query", "{\"roleIds\":[1],\"parentIds\":[2],\"deptPostIds\":[\"2-1\"]}");
    }

    /**
     * 部门日志信息查询
     */
    @Test
    public void deptLogQuery() throws Exception {
        mockPost("/api/v1/log/dept/query", "{\"parentIds\":[2]}");
    }

    /**
     * 岗位日志信息查询
     */
    @Test
    public void postLogQuery() throws Exception {
        mockPost("/api/v1/log/post/query", "{\"parentIds\":[2]}");
    }
}
