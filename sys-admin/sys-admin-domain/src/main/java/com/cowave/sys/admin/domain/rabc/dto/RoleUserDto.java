package com.cowave.sys.admin.domain.rabc.dto;

import lombok.Data;

/**
 * @author shanhuiming
 */
@Data
public class RoleUserDto {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户电话
     */
    private String userPhone;

    /**
	 * 用户职级
	 */
	private String userRank;

    /**
     * 用户部门
     */
    private String deptName;

    /**
     * 用户岗位
     */
    private String postName;
}
