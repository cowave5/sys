package com.cowave.sys.admin.domain.rabc.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author shanhuiming
 */
@Data
public class DeptReadUpdate {

    /**
     * 用户id
     */
    @NotNull(message = "{admin.dept.id.notnull}")
    private Integer deptId;

    /**
     * 用户名称
     */
    @NotBlank(message = "{admin.dept.name.notnull}")
    private String deptName;

    /**
     * 只读状态
     */
    @NotNull(message = "{dept.notnull.readOnly}")
    private Integer readOnly;
}
