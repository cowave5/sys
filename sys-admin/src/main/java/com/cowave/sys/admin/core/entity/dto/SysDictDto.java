/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.entity.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.cowave.commons.framework.access.security.AccessInfo;
import com.cowave.commons.framework.helper.redis.dict.Dict;
import com.cowave.commons.framework.helper.redis.dict.DictValueParser;
import com.cowave.commons.framework.support.excel.StatusConverter;
import com.cowave.commons.framework.support.excel.StringConverter;
import com.cowave.commons.framework.support.excel.YesNoConverter;
import com.cowave.commons.response.exception.AssertsException;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 字典
 *
 * @author shanhuiming
 */
@ExcelIgnoreUnannotated
@HeadRowHeight(20)
@ColumnWidth(20)
@HeadFontStyle(fontHeightInPoints = 10)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@Data
@EqualsAndHashCode(callSuper=false)
public class SysDictDto extends AccessInfo implements Dict {

	/**
	 * id
	 */
	private Long id;

	/**
	 * 上级字典编码
	 */
	private String parentCode;

    /**
     * 分组编码
     */
	@ExcelProperty("分组编码")
    private String groupCode;

	/**
	 * 分组名称
	 */
    @ExcelProperty("分组名称")
	private String groupName;

	/**
	 * 分组英文名
	 */
	@ExcelProperty("分组英文名")
	private String groupEn;

    /**
     * 类型编码
     */
	@ExcelProperty("类型编码")
    private String typeCode;

	/**
	 * 类型名称
	 */
    @ExcelProperty("类型名称")
	private String typeName;

	/**
	 * 类型英文名
	 */
	@ExcelProperty("类型英文名")
	private String typeEn;

    /**
     * 字典码
     */
	@ExcelProperty("字典码")
    private String dictCode;

	/**
	 * 字典名称
	 */
	@ExcelProperty("字典名称")
	private String dictLabel;

	/**
	 * 英文名称
	 */
	@ExcelProperty("英文名称")
	private String dictEn;

    /**
     * 字典值
     */
	@ExcelProperty(value = "字典值", converter = StringConverter.class)
    private Object dictValue;

	/**
	 * 值转换器
	 */
	@ColumnWidth(60)
	@ExcelProperty(value = "值转换器")
	private String valueParser;

	/**
	 * 转换参数
	 */
	@ExcelProperty(value = "转换参数")
	private String valueParam;

	/**
	 * 字典排序
	 */
	@ExcelProperty("字典排序")
	private Integer dictOrder;

    /**
     * 是否默认值
     */
	@ExcelProperty(value = "是否默认值", converter = YesNoConverter.class)
    private Integer isDefault = 0;

    /**
     * 是否只读
     */
	@ExcelProperty(value = "是否只读", converter = YesNoConverter.class)
    private Integer readOnly;

    /**
     * 状态
     */
	@ExcelProperty(value = "字典状态", converter = StatusConverter.class)
    private Integer status;

	/**
	 * css样式
	 */
	private String css;

    /**
	 * 创建人
	 */
	private String createBy;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 更新人
	 */
	private String updateBy;

	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	/**
	 * 备注
	 */
	@ColumnWidth(80)
	@ExcelProperty("备注")
	private String remark;

	public DictValueParser validParser() {
		if (StringUtils.isBlank(valueParser)) {
			return null;
		}
		try {
			Class<?> parserClass = Class.forName(valueParser);
			if (!DictValueParser.class.isAssignableFrom(parserClass)) {
				throw new AssertsException("{config.invalid.parser}");
			}
			return (DictValueParser) parserClass.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new AssertsException("{config.notexist.parser}");
		}
	}
}
