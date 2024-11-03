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
public class SysDeptInfoDto extends SysDept {

    /**
	 * 上级部门Id列表
	 */
	private List<Long> parentIds;

	/**
	 * 部门负责人Id列表
	 */
	private List<Long> leaderIds;
}
