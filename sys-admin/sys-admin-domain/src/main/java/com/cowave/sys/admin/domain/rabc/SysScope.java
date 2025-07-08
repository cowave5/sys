package com.cowave.sys.admin.domain.rabc;

import com.alibaba.excel.annotation.ExcelProperty;
import com.cowave.commons.framework.access.security.AccessInfoSetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @author shanhuiming
 */
@Data
public class SysScope implements AccessInfoSetter {

    /**
     * 权限id
     */
    private Integer scopeId;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 权限模块
     */
    private String scopeModule;

    /**
     * 权限名称
     */
    private String scopeName;

    /**
     * 权限状态
     */
    private Integer scopeStatus;

    /**
     * 权限内容
     */
    private Map<String, Object> content;

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
