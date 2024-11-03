package com.cowave.sys.model.admin.auth;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@Data
public class AuthProfile {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户角色
     */
    private List<String> roles = new ArrayList<>();

    /**
     * 用户权限
     */
    private List<String> permissions = new ArrayList<>();
}
