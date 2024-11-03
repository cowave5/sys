package com.cowave.sys.admin.rabc.domain.request;

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
    @NotNull(message = "{dept.notnull.id}")
    private Integer deptId;

    /**
     * 用户名称
     */
    @NotBlank(message = "{dept.notnull.name}")
    private String deptName;

    /**
     * 只读状态
     */
    @NotNull(message = "{dept.notnull.readOnly}")
    private Integer readOnly;
}
