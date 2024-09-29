package com.cowave.sys.admin.domain.base.request;

import lombok.Data;

/**
 * @author shanhuiming
 */
@Data
public class DictQuery {

    /**
     * 字典码
     */
    private String dictCode;

    /**
	 * 字典名称
	 */
	private String dictName;
}
