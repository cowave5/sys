/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.support.excel.SexConverter;
import com.cowave.commons.framework.support.excel.StatusConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @author shanhuiming
 */
@ExcelIgnoreUnannotated
@HeadRowHeight(20)
@ColumnWidth(20)
@HeadFontStyle(fontHeightInPoints = 10)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@Data
public class SysUser {

    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Integer userId;

    /**
     * 用户类型
     */
    private int userType;

    /**
     * 用户编码
     */
    @ColumnWidth(50)
    @ExcelProperty("用户编码")
    private String userCode;

    /**
     * 用户名称
     */
    @ExcelProperty("用户名称")
    @NotBlank(message = "{user.notnull.name}")
    private String userName;

    /**
     * 用户账号
     */
    @ExcelProperty("用户账号")
    @NotBlank(message = "{user.notnull.account}")
    private String userAccount;

    /**
	 * 用户密码
	 */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String userPasswd;

    /**
     * 用户性别
     */
    @ExcelProperty(value = "用户性别", converter = SexConverter.class)
    private Integer userSex;

    /**
     * 用户电话
     */
    @ExcelProperty("用户电话")
    @Pattern(regexp = "^[1][3,4,5,7,8][0-9]{9}$", message = "user.invalid.phone")
    private String userPhone;

    /**
     * 用户邮箱
     */
    @ExcelProperty("用户邮箱")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "user.invalid.email")
    private String userEmail;

    /**
     * 用户职级
     */
    private String rank;

    /**
     * 是否只读
     */
    private Integer readOnly;

    /**
     * 用户状态
     */
    @ExcelProperty(value = "用户状态", converter = StatusConverter.class)
    private Integer userStatus;

    /**
     * 备注
     */
    @ExcelProperty("备注")
    private String remark;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建部门
     */
    private Integer createDept;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    private Integer updateUser;

    /**
     * 更新部门
     */
    private Integer updateDept;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public static AccessUserDetails newUserDetails(SysUser sysUser, SysDept userDept) {
        AccessUserDetails userDetails = AccessUserDetails.newUserDetails();
        userDetails.setType(AccessUserDetails.TYPE_USER);
        userDetails.setUserId(sysUser.getUserId());
        userDetails.setUserCode(sysUser.getUserCode());
        userDetails.setUsername(sysUser.getUserAccount());
        userDetails.setUserNick(sysUser.getUserName());
        userDetails.setUserPasswd(sysUser.getUserPasswd());
        if (userDept != null) {
            userDetails.setDeptId(userDept.getDeptId());
            userDetails.setDeptCode(userDept.getDeptCode());
            userDetails.setDeptName(userDept.getDeptName());
        }
        return userDetails;
    }
}