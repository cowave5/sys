package com.cowave.sys.admin.domain.rabc.dto;

import com.cowave.sys.admin.domain.rabc.SysMenu;
import com.cowave.sys.admin.domain.rabc.SysScope;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author shanhuiming
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysMenuTree extends SysMenu {

    private List<SysScope> scopes;
}
