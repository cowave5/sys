package com.cowave.sys.admin.infra.base.socketio;

import com.cowave.commons.framework.helper.socketio.SocketIoHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Configuration
public class SysSocketConfiguration {
    public static final String SPACE_NOTICE = "/notice";
    public static final String EVENT_CLIENT_NOTICE_COUNT = "getNoticeCount";

    public static final String EVENT_SERVER_NOTICE_COUNT = "noticeCount";
    public static final String EVENT_SERVER_NOTICE_NEW = "newNotice";

    private final SocketIoHelper socketIoHelper;
    private final GetNoticeCountEvent getNoticeCountEvent;

    @PostConstruct
    public void init(){
        socketIoHelper.registerConnectListener(SPACE_NOTICE);
        socketIoHelper.registerDisconnectListener(SPACE_NOTICE);
        socketIoHelper.registerDataListener(SPACE_NOTICE, EVENT_CLIENT_NOTICE_COUNT, String.class, getNoticeCountEvent);
    }
}
