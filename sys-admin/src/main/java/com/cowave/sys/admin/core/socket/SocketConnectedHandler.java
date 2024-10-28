/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.socket;

import com.cowave.commons.framework.helper.socketio.ConnectedHandler;
import com.cowave.commons.framework.helper.socketio.SocketIoHelper;
import com.cowave.sys.admin.core.mapper.SysNoticeMapper;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Component
public class SocketConnectedHandler implements ConnectedHandler {

    private final SysNoticeMapper sysNoticeMapper;

    @Override
    public void onConnected(Long userId, SocketIoHelper socketIoHelper) {
        int unread = sysNoticeMapper.countUserUnRead(userId);
        socketIoHelper.sendSingle("notice_unread", unread, userId);
    }
}
