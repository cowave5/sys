package com.cowave.sys.admin.domain.rabc.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author shanhuiming
 */
@Data
public class MenuReadUpdate {

    /**
     * 菜单id
     */
    @NotNull(message = "{admin.menu.id.notnull}")
    private Integer menuId;

    /**
     * 只读状态 TODO
     */
    @NotNull(message = "{post.notnull.readOnly}")
    private Integer readOnly;
}
