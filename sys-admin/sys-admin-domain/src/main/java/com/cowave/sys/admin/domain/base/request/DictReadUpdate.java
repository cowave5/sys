package com.cowave.sys.admin.domain.base.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author shanhuiming
 */
@Data
public class DictReadUpdate {

    /**
     * 字典id
     */
    @NotNull(message = "{admin.dict.id.notnull}")
    private Integer dictId;

    /**
     * 只读状态
     */
    private Integer readOnly;
}
