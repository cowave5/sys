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
import com.cowave.sys.admin.api.entity.flow.Purchase;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
 public interface FlowPurchaseService {

    /**
     * 列表
     */
    Page<Purchase> list(Purchase purchase);

    /**
     * 详情
     */
     Purchase info(Long id);

    /**
     * 新增
     */
     void add(Purchase purchase);

    /**
     * 修改
     */
     void edit(Purchase purchase);

    /**
     * 删除
     */
     void delete(Long[] ids);
}
