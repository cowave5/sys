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

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.model.admin.SysAttach;
import com.cowave.sys.model.admin.SysNotice;
import com.cowave.sys.model.admin.SysNoticeRead;
import com.cowave.sys.model.admin.SysUser;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author shanhuiming
 *
 */
public interface SysNoticeService {

    /**
     * 列表
     */
    Page<SysNotice> list(SysNotice sysNotice);

    /**
     * 详情
     */
    SysNotice info(Long noticeId);

    /**
     * 新增
     */
    void add(SysNotice sysNotice) throws Exception;

    /**
     * 修改
     */
    void edit(SysNotice sysNotice) throws Exception;

    /**
     * 上传图片
     */
    SysAttach imageUpload(MultipartFile file, SysAttach image) throws Exception;

    /**
     * 删除
     */
    void delete(Long[] noticeId) throws Exception;

    /**
     * 发布
     */
    void publish(Long noticeId);

    /**
     * 已读列表
     */
    Page<SysNoticeRead> readList(Long noticeId);

    /**
     * 消息列表
     */
    Page<SysNotice> msgList();

    /**
     * 阅读消息
     */
    void msgRead(Long noticeId);

    /**
     * 反馈消息
     */
    void msgBack(Long noticeId, String readBack);

    /**
     * 删除消息
     */
    void msgDelete(Long noticeId);

    /**
     * 发送通知
     */
    void sendNotice(SysNotice notice, Long readUserId);

    /**
     * 流程通知
     */
    void sendFlowNotice(String processName, String taskName, Long startUser, Long assigneeUser);
}
