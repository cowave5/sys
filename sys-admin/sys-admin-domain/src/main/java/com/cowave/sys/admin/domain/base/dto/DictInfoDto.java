package com.cowave.sys.admin.domain.base.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.cowave.commons.framework.access.security.AccessInfoSetter;
import com.cowave.commons.framework.helper.redis.dict.Dict;
import com.cowave.sys.admin.domain.base.SysDict;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DictInfoDto extends SysDict implements Dict, AccessInfoSetter {

    /**
     * 分组编码
     */
	@ExcelProperty("分组码")
    private String groupCode;

	/**
	 * 分组名称
	 */
    @ExcelProperty("分组名称")
	private String groupName;

    /**
     * 类型编码
     */
	@ExcelProperty("类型码")
    private String typeCode;

	/**
	 * 类型名称
	 */
    @ExcelProperty("类型名称")
	private String typeName;
}
