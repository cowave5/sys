package com.cowave.sys.admin.domain.base.enums;

import com.cowave.commons.tools.EnumVal;
import lombok.RequiredArgsConstructor;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
public enum NoticeStatus implements EnumVal<Integer> {

    /**
     * 草稿
     */
    DRAFT(0),

    /**
     * 已发布
     */
    PUBLISH(1),

    /**
     * 已撤回
     */
    RECALL(2);

    private final Integer val;

    @Override
    public Integer val() {
        return val;
    }
}
