/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.code.mapper;

import com.cowave.commons.framework.access.security.AccessInfo;
import com.cowave.sys.code.entity.ModelInfo;
import com.cowave.sys.code.entity.SelectOption;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author shanhuiming
 */
@Mapper
public interface ModelMapper {

    /**
     * 模型选项
     */
    List<SelectOption> options();

    /**
     * 列表
     */
    List<ModelInfo> list(ModelInfo modelInfo);

    /**
     * 详情
     */
    ModelInfo info(Long id);

    /**
     * 新增
     */
    void insert(ModelInfo modelInfo);

    /**
     * 编辑
     */
    void update(ModelInfo modelInfo);

    /**
     * 删除
     */
    void delete(Integer[] array);

    /**
     * 是否导出Excel切换
     */
    void switchExcel(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessInfo user);

    /**
     * 是否继承Access切换
     */
    void switchAccess(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessInfo user);

    /**
     * 是否记录日志切换
     */
    void switchLog(@Param("id") Long id, @Param("flag") Integer flag, @Param("user") AccessInfo user);
}