/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.cowave.sys.admin.core.entity.dto.SysAttachDto;

/**
 * @author shanhuiming
 */
public interface SysAttachService {

    /**
     * 上传
     */
    SysAttachDto upload(MultipartFile file, SysAttachDto sysAttach, boolean isPublic) throws Exception;

    /**
     * 预览
     */
    String preview(Long attachId) throws Exception;

    /**
     * 预览
     */
    String preview(SysAttachDto sysAttach) throws Exception;

    /**
     * 删除
     */
    void delete(Long attachId) throws Exception;

    /**
     * 删除
     */
    void delete(SysAttachDto sysAttach) throws Exception;

    /**
     * 保留最近几个
     */
    void masterReserve(Long masterId, String attachGroup, String attachType, int reserve) throws Exception;

    /**
     * 更新宿主id
     */
    void updateMasterById(Long masterId, Long attachId);

    /**
     * 宿主最新附件
     */
    SysAttachDto latestOfMaster(Long masterId, String attachGroup) throws Exception;

    /**
     * 下载
     */
    void download(HttpServletResponse response, Long attachId) throws Exception;

    /**
     * 列表
     */
    List<SysAttachDto> list(Long masterId, String attachGroup, String attachType);

    /**
     * 更新宿主
     */
    void masterUpdate(List<Long> attachIds, Long masterId);

    /**
     * 删除宿主
     */
    void masterDelete(Long masterId, String attachGroup, String attachType) throws Exception;
}
