/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.base.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.client.http.asserts.Asserts;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.helper.socketio.SocketIoHelper;
import com.cowave.sys.admin.base.infra.dao.mapper.dto.SysNoticeDtoMapper;
import com.cowave.sys.admin.rabc.infra.dao.mapper.dto.SysUserDtoMapper;
import com.cowave.sys.admin.base.domain.dto.SysAttachDto;
import com.cowave.sys.admin.base.domain.dto.SysNoticeDto;
import com.cowave.sys.admin.base.domain.dto.SysNoticeReadDto;
import com.cowave.sys.admin.base.service.SysAttachService;
import com.cowave.sys.admin.base.service.SysNoticeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class SysNoticeServiceImpl implements SysNoticeService {

    private final SysAttachService sysAttachService;

    private final SysNoticeDtoMapper sysNoticeMapper;

    private final SysUserDtoMapper sysUserMapper;

    private final SocketIoHelper socketIoHelper;

    @Override
    public Page<SysNoticeDto> list(SysNoticeDto sysNotice) {
        if(Access.userIsAdmin()){
            return sysNoticeMapper.list(Access.page(), sysNotice);
        }else{
            return sysNoticeMapper.listOfUser(Access.page(), Access.userId(), sysNotice);
        }
    }

    @Override
    public SysNoticeDto info(Long noticeId) {
        return sysNoticeMapper.info(noticeId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(SysNoticeDto sysNotice) throws Exception {
        sysNotice.setNoticeStatus(SysNoticeDto.STATUS_DRAFT);
        sysNoticeMapper.insert(sysNotice);
        filterAttaches(sysNotice);
    }

    @Override
    public void edit(SysNoticeDto sysNotice) throws Exception {
        Asserts.notNull(sysNotice.getNoticeId(), "{notice.notnull.id}");
        SysNoticeDto notice = sysNoticeMapper.info(sysNotice.getNoticeId());
        Asserts.notNull(notice, "{notice.notexist}", sysNotice.getNoticeId());
        Asserts.equals(notice.getNoticeStatus(), SysNoticeDto.STATUS_DRAFT, "{notice.only.edit.unpublish}");
        if(!Access.userIsAdmin()){
            Asserts.equals(notice.getCreateUser(), Access.userId(v -> ((Number)v).longValue()), "{notice.only.edit.self}");
        }
        sysNoticeMapper.update(sysNotice);
        filterAttaches(sysNotice);
    }

    private void filterAttaches(SysNoticeDto sysNotice) throws Exception {
        String content = sysNotice.getContent();
        List<SysAttachDto> attachs = sysNotice.getAttachs();
        for(SysAttachDto attach : attachs){
            if(content.contains(attach.getAttachPath())){
                sysAttachService.updateMasterById(sysNotice.getNoticeId(), attach.getAttachId());
            }else{
                sysAttachService.delete(attach);
            }
        }
    }

    @Override
    public SysAttachDto imageUpload(MultipartFile file, SysAttachDto image) throws Exception {
        image.setAttachGroup("sys-notice");
        image.setAttachType("images");
        SysAttachDto attach = sysAttachService.upload(file, image, true);
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
        SysNoticeDto notice = sysNoticeMapper.info(noticeId);
        if(notice == null){
            return;
        }
        if(!Access.userIsAdmin()){
            Asserts.equals(notice.getCreateUser(), Access.userId(v -> ((Number)v).longValue()), "{notice.only.delete.self}");
        }
        if(SysNoticeDto.STATUS_DRAFT == notice.getNoticeStatus()){ // 删除草稿
            sysNoticeMapper.delete(noticeId);
            for(SysAttachDto attach : notice.getAttachs()){
                sysAttachService.delete(attach);
            }
        }else if(SysNoticeDto.STATUS_PUBLISH == notice.getNoticeStatus()){ // 撤回已发布
            sysNoticeMapper.updateStatus(noticeId, SysNoticeDto.STATUS_RECALL);
            sysNoticeMapper.msgRecall(noticeId);
        }else if(SysNoticeDto.STATUS_RECALL == notice.getNoticeStatus()) { // 删除已撤回
            sysNoticeMapper.delete(noticeId);
            for(SysAttachDto attach : notice.getAttachs()){
                sysAttachService.delete(attach);
            }
            sysNoticeMapper.msgClear(noticeId);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void publish(Long noticeId) {
        SysNoticeDto notice = sysNoticeMapper.info(noticeId);
        Asserts.notNull(notice, "{notice.notexist}", noticeId);
        if(!Access.userIsAdmin()){
            Asserts.equals(notice.getCreateUser(), Access.userId(v -> ((Number)v).longValue()), "{notice.only.publish.self}");
        }
        if(SysNoticeDto.STATUS_DRAFT != notice.getNoticeStatus()){
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
        sysNoticeMapper.updatePublishTotal(noticeId, SysNoticeDto.STATUS_PUBLISH, new Date());
        // 推送
        List<Long> userIds = sysNoticeMapper.publishUserIds(noticeId);
        socketIoHelper.sendGroup("notice_new", notice.getNoticeTitle(), userIds);
    }

    @Override
    public Page<SysNoticeReadDto> readList(Long noticeId) {
        return sysNoticeMapper.readList(Access.page(), noticeId);
    }

    @Override
    public Page<SysNoticeDto> msgList() {
        return sysNoticeMapper.msgList(Access.page(), Access.userId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void msgRead(Long noticeId) {
        if(sysNoticeMapper.msgRead(noticeId, SysNoticeReadDto.STATUS_PUBLISH_READ, Access.userId(), Access.accessTime()) > 0){
            sysNoticeMapper.updateReadStat(noticeId);
        }
    }

    @Override
    public void msgBack(Long noticeId, String readBack) {
        sysNoticeMapper.msgBack(noticeId, Access.userId(), readBack);
    }

    @Override
    public void msgDelete(Long noticeId) {
        sysNoticeMapper.msgDelete(Access.userId(), noticeId);
    }

    /**
     * 未读统计
     */
    @Override
    public int countUserUnRead(Integer userId) {
        return sysNoticeMapper.countUserUnRead(userId);
    }

    @Override
    public void sendNotice(SysNoticeDto notice, Long readUserId) {
        sysNoticeMapper.insert(notice);
        sysNoticeMapper.insertReadOfUser(notice.getNoticeId(), List.of(readUserId));
    }

    public void sendFlowNotice(String processName, String taskName, Integer startUser, Long assigneeUser){
        String startUserName = sysUserMapper.queryNameById(startUser);
        SysNoticeDto notice = defaultNotice();
        notice.setNoticeTitle(startUserName + "的" + processName + "[" + taskName + "]");
        notice.setContent("<p>催办提醒: </p><p>" + startUserName + "的" + processName + "[" + taskName + "]</p>");
        sysNoticeMapper.createNotice(notice);
        sysNoticeMapper.insertReadOfUser(notice.getNoticeId(), List.of(assigneeUser));
        socketIoHelper.sendSingle("notice_todo", "催办提醒: " + startUserName + "的" + processName + "[" + taskName + "]", assigneeUser);
    }

    private SysNoticeDto defaultNotice(){
        SysNoticeDto notice = new SysNoticeDto();
        notice.setNoticeStatus(SysNoticeDto.STATUS_PUBLISH);
        notice.setCreateUser(Access.userId());
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
