package com.cowave.sys.admin.domain.base;

import com.cowave.commons.framework.access.operation.OperationInfo;
import com.cowave.commons.framework.access.security.AccessInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
public class SysOperation {

    public static final String INDEX_NAME = "sys_operation_log";

    /**
     * 日志id
     */
    private Long id;

    /**
     * 操作模块
     */
    private String opModule;

    /**
     * 操作类型
     */
    private String opType;

    /**
     * 操作动作
     */
    private String opAction;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date opTime;

    /**
     * 操作状态
     */
    private Integer opStatus;

    /**
     * 访问ip
     */
    private String ip;

    /**
     * 访问url
     */
    private String url;

    /**
     * 请求内容
     */
    private Object request;

    /**
     * 操作描述
     */
    private String opDesc;

    /**
     * 请求内容
     */
    private Object opContent;

    /**
     * 响应内容
     */
    private Object response;

    /**
     * 访问对照
     */
    private AccessInfo access;

    public SysOperation(OperationInfo opInfo) {
        this.opModule = opInfo.getOpModule();
        this.opType = opInfo.getOpType();
        this.opAction = opInfo.getOpAction();
        this.opDesc = opInfo.getDesc();
        this.opContent = opInfo.getOpContent();
        this.opStatus = opInfo.isSuccess() ? 1 : 0;
    }
}
