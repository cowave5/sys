package com.cowave.sys.admin.domain.base.enums;

import com.cowave.commons.tools.EnumVal;
import lombok.RequiredArgsConstructor;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
public enum NoticeReadStatus implements EnumVal<Integer> {

    /**
     * 消息未读
     */
    UNREAD_PUBLISH(0),

    /**
     * 消息未读删除
     */
    UNREAD_DELETE(1),

    /**
     * 消息未读撤回
     */
    UNREAD_RECALL(2),

    /**
     * 消息已读
     */
    READ_PUBLISH(10),

    /**
     * 消息已读删除
     */
    READ_DELETE(11),

    /**
     * 消息已读撤回
     */
    READ_RECALL(22);

    private final Integer val;

    @Override
    public Integer val() {
        return val;
    }
}
