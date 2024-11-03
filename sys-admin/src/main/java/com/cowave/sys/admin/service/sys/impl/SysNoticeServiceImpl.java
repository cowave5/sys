/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.sys.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.helper.socketio.SocketIoHelper;
import com.cowave.commons.response.exception.Asserts;
import com.cowave.sys.admin.service.sys.SysAttachService;
import com.cowave.sys.admin.service.sys.SysNoticeService;
import com.cowave.sys.admin.core.mapper.SysNoticeMapper;
import com.cowave.sys.admin.core.mapper.SysUserMapper;
import com.cowave.sys.model.admin.SysAttach;
import com.cowave.sys.model.admin.SysNotice;
import com.cowave.sys.model.admin.SysNoticeRead;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class SysNoticeServiceImpl implements SysNoticeService {

    private final SysAttachService sysAttachService;

    private final SysNoticeMapper sysNoticeMapper;

    private final SysUserMapper sysUserMapper;

    private final SocketIoHelper socketIoHelper;

    @Override
    public Page<SysNotice> list(SysNotice sysNotice) {
        if(Access.userIsAdmin()){
            return sysNoticeMapper.list(Access.page(), sysNotice);
        }else{
            return sysNoticeMapper.listOfUser(Access.page(), Access.userId(v -> ((Number)v).longValue()), sysNotice);
        }
    }

    @Override
    public SysNotice info(Long noticeId) {
        return sysNoticeMapper.info(noticeId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(SysNotice sysNotice) throws Exception {
        sysNotice.setNoticeStatus(SysNotice.STATUS_DRAFT);
        sysNoticeMapper.insert(sysNotice);
        filterAttaches(sysNotice);
    }

    @Override
    public void edit(SysNotice sysNotice) throws Exception {
        Asserts.notNull(sysNotice.getNoticeId(), "{notice.notnull.id}");
        SysNotice notice = sysNoticeMapper.info(sysNotice.getNoticeId());
        Asserts.notNull(notice, "{notice.notexist}", sysNotice.getNoticeId());
        Asserts.equals(notice.getNoticeStatus(), SysNotice.STATUS_DRAFT, "{notice.only.edit.unpublish}");
        if(!Access.userIsAdmin()){
            Asserts.equals(notice.getCreateUser(), Access.userId(v -> ((Number)v).longValue()), "{notice.only.edit.self}");
        }
        sysNoticeMapper.update(sysNotice);
        filterAttaches(sysNotice);
    }

    private void filterAttaches(SysNotice sysNotice) throws Exception {
        String content = sysNotice.getContent();
        List<SysAttach> attachs = sysNotice.getAttachs();
        for(SysAttach attach : attachs){
            if(content.contains(attach.getAttachPath())){
                sysAttachService.updateMasterById(sysNotice.getNoticeId(), attach.getAttachId());
            }else{
                sysAttachService.delete(attach);
            }
        }
    }

    @Override
    public SysAttach imageUpload(MultipartFile file, SysAttach image) throws Exception {
        image.setAttachGroup("sys-notice");
        image.setAttachType("images");
        SysAttach attach = sysAttachService.upload(file, image, true);
        attach.setViewUrl(sysAttachService.preview(attach));
        return attach;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long[] noticeIds) throws Exception {
        for(Long noticeId : noticeIds){
            delete(noticeId);
        }
    }

    private void delete(Long noticeId) throws Exception {
        SysNotice notice = sysNoticeMapper.info(noticeId);
        if(notice == null){
            return;
        }
        if(!Access.userIsAdmin()){
            Asserts.equals(notice.getCreateUser(), Access.userId(v -> ((Number)v).longValue()), "{notice.only.delete.self}");
        }
        if(SysNotice.STATUS_DRAFT == notice.getNoticeStatus()){ // 删除草稿
            sysNoticeMapper.delete(noticeId);
            for(SysAttach attach : notice.getAttachs()){
                sysAttachService.delete(attach);
            }
        }else if(SysNotice.STATUS_PUBLISH == notice.getNoticeStatus()){ // 撤回已发布
            sysNoticeMapper.updateStatus(noticeId, SysNotice.STATUS_RECALL);
            sysNoticeMapper.msgRecall(noticeId);
        }else if(SysNotice.STATUS_RECALL == notice.getNoticeStatus()) { // 删除已撤回
            sysNoticeMapper.delete(noticeId);
            for(SysAttach attach : notice.getAttachs()){
                sysAttachService.delete(attach);
            }
            sysNoticeMapper.msgClear(noticeId);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void publish(Long noticeId) {
        SysNotice notice = sysNoticeMapper.info(noticeId);
        Asserts.notNull(notice, "{notice.notexist}", noticeId);
        if(!Access.userIsAdmin()){
            Asserts.equals(notice.getCreateUser(), Access.userId(v -> ((Number)v).longValue()), "{notice.only.publish.self}");
        }
        if(SysNotice.STATUS_DRAFT != notice.getNoticeStatus()){
            return;
        }

        // 转换read信息
        if(Objects.equals(notice.getGoalsAll(), 1)) {
            sysNoticeMapper.insertReadOfAll(noticeId);
        }else {
            List<Long> depts = notice.getGoalsDept();
            if(CollectionUtils.isNotEmpty(depts)) {
                sysNoticeMapper.insertReadOfDept(noticeId, depts);
            }
            List<Long> roles = notice.getGoalsRole();
            if(CollectionUtils.isNotEmpty(roles)) {
                sysNoticeMapper.insertReadOfRole(noticeId, roles);
            }
            List<Long> users = notice.getGoalsUser();
            if(CollectionUtils.isNotEmpty(users)) {
                sysNoticeMapper.insertReadOfUser(noticeId, users);
            }
        }
        sysNoticeMapper.updatePublishTotal(noticeId, SysNotice.STATUS_PUBLISH, new Date());
        // 推送
        List<Long> userIds = sysNoticeMapper.publishUserIds(noticeId);
        socketIoHelper.sendGroup("notice_new", notice.getNoticeTitle(), userIds);
    }

    @Override
    public Page<SysNoticeRead> readList(Long noticeId) {
        return sysNoticeMapper.readList(Access.page(), noticeId);
    }

    @Override
    public Page<SysNotice> msgList() {
        return sysNoticeMapper.msgList(Access.page(), Access.userId(v -> ((Number)v).longValue()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void msgRead(Long noticeId) {
        if(sysNoticeMapper.msgRead(noticeId, SysNoticeRead.STATUS_PUBLISH_READ, Access.userId(v -> ((Number)v).longValue()), Access.accessTime()) > 0){
            sysNoticeMapper.updateReadStat(noticeId);
        }
    }

    @Override
    public void msgBack(Long noticeId, String readBack) {
        sysNoticeMapper.msgBack(noticeId, Access.userId(v -> ((Number)v).longValue()), readBack);
    }

    @Override
    public void msgDelete(Long noticeId) {
        sysNoticeMapper.msgDelete(Access.userId(v -> ((Number)v).longValue()), noticeId);
    }

    /**
     * 未读统计
     */
    @Override
    public int countUserUnRead(Long userId) {
        return sysNoticeMapper.countUserUnRead(userId);
    }

    @Override
    public void sendNotice(SysNotice notice, Long readUserId) {
        sysNoticeMapper.insert(notice);
        sysNoticeMapper.insertReadOfUser(notice.getNoticeId(), List.of(readUserId));
    }

    public void sendFlowNotice(String processName, String taskName, Long startUser, Long assigneeUser){
        String startUserName = sysUserMapper.queryNameById(startUser);
        SysNotice notice = defaultNotice();
        notice.setNoticeTitle(startUserName + "的" + processName + "[" + taskName + "]");
        notice.setContent("<p>催办提醒: </p><p>" + startUserName + "的" + processName + "[" + taskName + "]</p>");
        sysNoticeMapper.createNotice(notice);
        sysNoticeMapper.insertReadOfUser(notice.getNoticeId(), List.of(assigneeUser));
        socketIoHelper.sendSingle("notice_todo", "催办提醒: " + startUserName + "的" + processName + "[" + taskName + "]", assigneeUser);
    }

    private SysNotice defaultNotice(){
        SysNotice notice = new SysNotice();
        notice.setNoticeStatus(SysNotice.STATUS_PUBLISH);
        notice.setCreateUser(Access.userId(v -> ((Number)v).longValue()));
        notice.setNoticeType(3);
        notice.setNoticeLevel(0);
        notice.setIsSystem(0);
        notice.setStatTotal(1);
        notice.setStatRead(0);
        notice.setCreateTime(new Date());
        notice.setPublishTime(new Date());
        return notice;
    }
}
