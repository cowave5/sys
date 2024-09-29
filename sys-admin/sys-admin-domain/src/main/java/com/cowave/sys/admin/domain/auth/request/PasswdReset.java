package com.cowave.sys.admin.domain.auth.request;

import lombok.Data;

/**
 * @author shanhuiming
 */
@Data
public class PasswdReset {

    /**
     * 旧密码
     */
    private String oldPasswd;

    /**
     * 新密码
     */
    private String newPasswd;
}
