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

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cowave.sys.model.admin.SysAttach;

/**
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface SysAttachMapper {

    /**
     * 新增
     */
    void insert(SysAttach sysAttach);

    /**
     * 删除
     */
    void delete(Long attachId);

    /**
     * 更新宿主id
     */
    void updateMasterById(@Param("masterId") Long masterId, @Param("attachId") Long attachId);

    /**
     * 宿主最新附件
     */
    SysAttach latestOfMaster(@Param("masterId") Long masterId, @Param("attachGroup") String attachGroup);

    /**
     * 列表
     */
    List<SysAttach> list(@Param("masterId") Long masterId, @Param("attachGroup") String attachGroup, @Param("attachType") String attachType);

    /**
     * 详情
     */
    SysAttach info(Long attachId);

    /**
     * 更新宿主
     */
    void masterUpdate(@Param("list") List<Long> attachIds, @Param("masterId") Long masterId);

    /**
     * 清除附件
     */
    void masterClear(@Param("masterId") Long masterId, @Param("attachGroup") String attachGroup, @Param("attachType") String attachType);

    /**
     * 删除宿主
     */
    void masterDelete(@Param("masterId") Long masterId, @Param("attachGroup") String attachGroup, @Param("attachType") String attachType);
}
