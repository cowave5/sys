/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.meter.mapper;

import com.cowave.sys.meter.entity.DbTableIndex;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *
 * @author shanhuiming
 */
@Mapper
public interface IndexMapper {

    /**
     * 新增
     */
    void insert(DbTableIndex index);

    /**
     * 列表
     */
    List<DbTableIndex> list(Long tableId);

    /**
     * 删除库索引信息
     */
    void deleteDbIndex(Long tableId);
}
