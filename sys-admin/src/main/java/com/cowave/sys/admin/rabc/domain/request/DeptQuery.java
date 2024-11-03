package com.cowave.sys.admin.rabc.domain.request;

import lombok.Data;

/**
 * @author shanhuiming
 */
@Data
public class DeptQuery {

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 部门电话
     */
    private String deptPhone;
}
