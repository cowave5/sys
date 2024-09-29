package com.cowave.sys.admin.infra.base.socketio;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.cowave.sys.admin.infra.base.dao.SysNoticeUserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.cowave.sys.admin.infra.base.socketio.SysSocketConfiguration.EVENT_SERVER_NOTICE_COUNT;

/**
 * @author shanhuiming
 */
@Component
@RequiredArgsConstructor
public class GetNoticeCountEvent implements DataListener<String> {

    private final SysNoticeUserDao sysNoticeUserDao;

    @Override
    public void onData(SocketIOClient client, String userCode, AckRequest ackSender) {
        Long noticeCount = sysNoticeUserDao.countUnReadByUserId(userCode);
        client.sendEvent(EVENT_SERVER_NOTICE_COUNT, noticeCount);
    }
}
