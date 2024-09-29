package com.cowave.sys.admin.domain.rabc.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author shanhuiming
 */
@Data
public class DeptPostQuery {

    /**
     * 部门id
     */
    @NotNull(message = "{admin.dept.id.notnull}")
    private Integer deptId;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 岗位类型
     */
    private String postType;
}
