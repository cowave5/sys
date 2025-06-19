package com.cowave.sys.job.infra.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.job.domain.JobTrigger;
import com.cowave.sys.job.infra.dao.mapper.JobTriggerMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xuxueli/shanhuiming
 */
@Repository
public class JobTriggerDao extends ServiceImpl<JobTriggerMapper, JobTrigger> {

    public List<JobTrigger> preListInSeconds(long ms, int limit){
        return lambdaQuery()
                .eq(JobTrigger::getStatus, 1)
                .le(JobTrigger::getNextTime, ms)
                .last("limit " + limit)
                .list();
    }

    public void updateTime(JobTrigger jobTrigger){
        lambdaUpdate()
                .eq(JobTrigger::getId, jobTrigger.getId())
                .eq(JobTrigger::getStatus, 1)
                .set(JobTrigger::getLastTime, jobTrigger.getLastTime())
                .set(JobTrigger::getNextTime, jobTrigger.getNextTime())
                .update();
    }
}
