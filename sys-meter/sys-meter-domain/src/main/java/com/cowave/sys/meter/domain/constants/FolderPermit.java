package com.cowave.sys.meter.domain.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.cowave.commons.tools.EnumVal;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author shanhuiming
 */
@Getter
@RequiredArgsConstructor
public enum FolderPermit implements EnumVal<Integer> {

    /**
     * 参与者
     */
    PARTICIPANT(0),

    /**
     * 维护者
     */
    MAINTAINER(1),

    /**
     * 拥有者
     */
    OWNER(2);

    @EnumValue
    @JsonValue
    private final Integer val;
}
