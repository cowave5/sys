package com.cowave.sys.admin.domain.base.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author shanhuiming
 */
@Data
public class OperationQuery {

    /**
     * 操作模块
     */
    private String opModule;

    /**
     * 操作类型
     */
    private String opType;

    /**
     * 操作人
     */
    private String opUser;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
