package com.cowave.sys.admin.core.entity.dto;

import com.cowave.sys.admin.core.entity.SysDept;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDeptListDto extends SysDept {

    /**
     * 上级部门id
     */
    private String parentId;

    /**
     * 部门负责人列表
     */
    private List<SysUserNameDto> leaderList;
}
