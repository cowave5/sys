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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.model.admin.SysNotice;
import com.cowave.sys.model.admin.SysNoticeRead;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface SysNoticeMapper {

    /**
     * 列表
     */
    Page<SysNotice> list(Page<SysNotice> page, @Param("notice") SysNotice sysNotice);

    /**
     * 用户创建的
     */
    Page<SysNotice> listOfUser(Page<SysNotice> page, @Param("userId") Long userId, @Param("notice") SysNotice sysNotice);

    /**
     * 详情
     */
    SysNotice info(Long noticeId);

    /**
     * 新增
     */
    void insert(SysNotice sysNotice);

    /**
     * 创建Notice
     */
    void createNotice(SysNotice sysNotice);

    /**
     * 更新
     */
    void update(SysNotice sysNotice);

    /**
     * 删除
     */
    void delete(Long noticeId);

    /**
     * 修改状态
     */
    void updateStatus(@Param("noticeId") Long noticeId, @Param("noticeStatus") Integer noticeStatus);

    /**
     * 消息撤回
     */
    void msgRecall(@Param("noticeId") Long noticeId);

    /**
     * 消息删除
     */
    void msgClear(@Param("noticeId") Long noticeId);

    /**
     * 所有用户
     */
    void insertReadOfAll(Long noticeId);

    /**
     * 单位用户
     */
    void insertReadOfDept(@Param("noticeId") Long noticeId, @Param("list") List<Long> list);

    /**
     * 角色用户
     */
    void insertReadOfRole(@Param("noticeId") Long noticeId, @Param("list") List<Long> list);

    /**
     * 指定用户
     */
    void insertReadOfUser(@Param("noticeId") Long noticeId, @Param("list") List<Long> list);

    /**
     * 更新待读总数
     */
    void updatePublishTotal(@Param("noticeId") Long noticeId,
                            @Param("noticeStatus") Integer noticeStatus, @Param("publishTime") Date publishTime);

    /**
     * 已读列表
     */
    Page<SysNoticeRead> readList(Page<SysNoticeRead> page, @Param("noticeId") Long noticeId);

    /**
     * 消息列表
     */
    Page<SysNotice> msgList(Page<SysNotice> page, @Param("userId") Long userId);

    /**
     * 阅读消息
     */
    int msgRead(@Param("noticeId") Long noticeId, @Param("noticeStatus") Integer noticeStatus,
                @Param("userId") Long userId, @Param("readTime") Date readTime);

    /**
     * 反馈消息
     */
    void msgBack(@Param("noticeId") Long noticeId, @Param("userId") Long userId, @Param("readBack") String readBack);

    /**
     * 更新已读统计
     */
    void updateReadStat(Long noticeId);

    /**
     * 删除消息
     */
    void msgDelete(@Param("userId") Long userId, @Param("noticeId") Long noticeId);

    /**
     * 未读统计
     */
    int countUserUnRead(Long userId);

    /**
     * 消息目标用户
     */
    List<Long> publishUserIds(Long noticeId);
}
