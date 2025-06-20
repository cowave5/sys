package com.cowave.sys.job.domain.constant;

import lombok.Getter;

/**
 * @author xuxueli/shanhuiming
 */
@Getter
public enum GlueTypeEnum {

    /**
     * BEAN
     */
    BEAN("BEAN", false, null, null),

    /**
     * GLUE_GROOVY
     */
    GLUE_GROOVY("GLUE(Java)", false, null, null),

    /**
     * GLUE_SHELL
     */
    GLUE_SHELL("GLUE(Shell)", true, "bash", ".sh"),

    /**
     * GLUE_PYTHON
     */
    GLUE_PYTHON("GLUE(Python)", true, "python", ".py"),

    /**
     * GLUE_PHP
     */
    GLUE_PHP("GLUE(PHP)", true, "php", ".php"),

    /**
     * GLUE_NODEJS
     */
    GLUE_NODEJS("GLUE(Nodejs)", true, "node", ".js"),

    /**
     * GLUE_POWERSHELL
     */
    GLUE_POWERSHELL("GLUE(PowerShell)", true, "powershell", ".ps1");

    private final String desc;
    private final boolean isScript;
    private final String cmd;
    private final String suffix;

    GlueTypeEnum(String desc, boolean isScript, String cmd, String suffix) {
        this.desc = desc;
        this.isScript = isScript;
        this.cmd = cmd;
        this.suffix = suffix;
    }

    public boolean isScript() {
        return isScript;
    }

    public static GlueTypeEnum match(String name){
        for (GlueTypeEnum item: GlueTypeEnum.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return null;
    }
}
