/*
 * Copyright (c) 2017～2024 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.core.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author shanhuiming
 */
@ExcelIgnoreUnannotated
@HeadRowHeight(20)
@ColumnWidth(20)
@HeadFontStyle(fontHeightInPoints = 10)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@Data
public class SysDept {

    /**
     * 部门id
     */
    @TableId(type = IdType.AUTO)
    private Integer deptId;

    /**
     * 部门编码
     */
    @ColumnWidth(50)
	@ExcelProperty("部门编码")
    private String deptCode;

    /**
     * 部门名称
     */
	@ColumnWidth(50)
	@ExcelProperty("部门名称")
    @NotBlank(message = "{dept.notnull.name}")
    private String deptName;

    /**
     * 部门简称
     */
    @ColumnWidth(30)
	@ExcelProperty("部门简称")
    private String deptShort;

    /**
     * 部门地址
     */
    @ColumnWidth(50)
	@ExcelProperty("部门地址")
    private String deptAddr;

    /**
     * 部门电话
     */
    @ExcelProperty("部门电话")
    private String deptPhone;

    /**
     * 是否只读
     */
    private Integer readOnly;

    /**
     * 备注
     */
    @ExcelProperty("备注")
    private String remark;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建部门
     */
    private Integer createDept;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    private Integer updateUser;

    /**
     * 更新部门
     */
    private Integer updateDept;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}