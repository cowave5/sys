package com.cowave.sys.admin.domain.rabc;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 租户
 */
@Data
public class SysTenant {

    /**
     * 租户id
     */
    @TableId
    private String tenantId;

    /**
     * 租户名称
     */
    private String tenantName;

    /**
     * 租户联系人
     */
    private String tenantUser;

    /**
     * 租户地址
     */
    private String tenantAddr;

    /**
     * 租户电话
     */
    private String tenantPhone;

    /**
     * 租户邮箱
     */
    private String tenantEmail;

    /**
     * 用户序号
     */
    private Integer userIndex;

    /**
     * 用户统计
     */
    private Integer userCount;

    /**
     * 用户上限
     */
    private Integer userLimit;

    /**
     * 租户状态
     */
    private Integer status;

    /**
     * 租户到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;

    /**
     * 备注
     */
    @ExcelProperty("备注")
    private String remark;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
