package com.cowave.sys.admin.domain.base.dto;

import com.cowave.sys.admin.domain.base.SysNoticeUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NoticeUserDto extends SysNoticeUser {

    /**
     * 用户名称
     */
    private String userName;
}
