package com.cowave.sys.job.client.handler;

import com.cowave.sys.job.client.JobContent;
import com.cowave.sys.job.client.util.ScriptUtil;
import com.cowave.sys.job.domain.constant.GlueTypeEnum;
import com.cowave.sys.job.domain.constant.TriggerStatusEnum;
import lombok.Getter;

import java.io.File;
import java.util.Date;

/**
 * @author xuxueli/shanhuiming
 */
public class ScriptJobHandler extends JobHandler {

    private final int jobId;

    @Getter
    private final long glueUpdatetime;

    private final String gluesource;

    private final GlueTypeEnum glueType;

    public ScriptJobHandler(int jobId, long glueUpdatetime, String gluesource, GlueTypeEnum glueType){
        this.jobId = jobId;
        this.glueUpdatetime = glueUpdatetime;
        this.gluesource = gluesource;
        this.glueType = glueType;
        File glueSrcPath = new File("log/script");
        if (glueSrcPath.exists()) {
            File[] glueSrcFileList = glueSrcPath.listFiles();
            if (glueSrcFileList!=null && glueSrcFileList.length>0) {
                for (File glueSrcFileItem : glueSrcFileList) {
                    if (glueSrcFileItem.getName().startsWith(jobId +"_")) {
                        glueSrcFileItem.delete();
                    }
                }
            }
        }

    }

    @Override
    public void execute() throws Exception {
        if (!glueType.isScript()) {
            JobContent.get().setHandleStatus(TriggerStatusEnum.EXEC_FAIL.getStatus());
            JobContent.get().setHandleMsg("glueType["+ glueType +"] invalid");
            return;
        }

        long startTime = System.currentTimeMillis();
        JobContent.get().setHandleTime(new Date());
        String cmd = glueType.getCmd();
        String scriptFileName = "log/script"
                .concat(File.separator)
                .concat(String.valueOf(jobId))
                .concat("_")
                .concat(String.valueOf(glueUpdatetime))
                .concat(glueType.getSuffix());
        File scriptFile = new File(scriptFileName);
        if (!scriptFile.exists()) {
            ScriptUtil.markScriptFile(scriptFileName, gluesource);
        }

        // script params：0=param、1=分片序号、2=分片总数
        String[] scriptParams = new String[3];
        scriptParams[0] = JobContent.get().getJobParam();
        scriptParams[1] = String.valueOf(JobContent.get().getShardIndex());
        scriptParams[2] = String.valueOf(JobContent.get().getShardTotal());

        int exitValue = ScriptUtil.execToFile(cmd, scriptFileName, scriptParams);
        JobContent.get().setHandleCost(System.currentTimeMillis() - startTime);
        if (exitValue == 0) {
            JobContent.get().setHandleStatus(TriggerStatusEnum.EXEC_SUCCESS.getStatus());
        } else {
            JobContent.get().setHandleStatus(TriggerStatusEnum.EXEC_FAIL.getStatus());
        }
    }
}
