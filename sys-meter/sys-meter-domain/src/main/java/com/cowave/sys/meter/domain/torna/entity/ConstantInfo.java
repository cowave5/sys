package com.cowave.sys.meter.domain.torna.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 表名：error_code_info
 * 备注：错误码
 *
 * @author tanghc
 */
@Data
public class ConstantInfo {

    /**  数据库字段：id */
    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * project.id
     */
    private Long projectId;


    /**
     * module.id
     */
    private Long moduleId;


    /**
     * doc_info.id
     */
    private Long docId;


    /**
     * 错误码内容
     */
    private String content;


    private LocalDateTime gmtCreate;


    private LocalDateTime gmtModified;



}
