package com.cowave.sys.admin.domain.rabc.dto;

import com.cowave.sys.admin.domain.rabc.SysRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleInfoDto extends SysRole {

    /**
	 * 角色菜单
	 */
	private List<Long> menuIds = new ArrayList<>();
}
