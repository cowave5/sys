/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.flow.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.domain.flow.Meeting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author shanhuiming
 */
@Mapper
public interface FlowMeetingMapper {

    /**
     * 序列id
     */
    long nextId();

    /**
     * 列表
     */
    Page<Meeting> list(Page<Meeting> page, @Param("meeting") Meeting meeting);

    /**
     * 详情
     */
    Meeting info(Long id);

    /**
     * 新增
     */
    void insert(Meeting meeting);

    /**
     * 修改
     */
    void update(Meeting meeting);

    /**
     * 删除
     */
    int delete(Long[] ids);

    /**
     * 修改流程状态
     */
    void changeProcessStatus(@Param("id") Long id, @Param("processStatus") Integer processStatus);
}
