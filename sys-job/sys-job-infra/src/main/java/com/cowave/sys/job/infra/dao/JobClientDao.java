package com.cowave.sys.job.infra.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.job.domain.JobClient;
import com.cowave.sys.job.infra.dao.mapper.JobClientMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author xuxueli/shanhuiming
 */
@Repository
public class JobClientDao extends ServiceImpl<JobClientMapper, JobClient> {

    public Integer registry(JobClient jobClient) {
        JobClient exist = lambdaQuery()
                .eq(JobClient::getClientName, jobClient.getClientName())
                .eq(JobClient::getClientAddress, jobClient.getClientAddress())
                .one();
        if (exist == null) {
            this.save(jobClient);
            return jobClient.getId();
        } else {
            exist.setUpdateTime(new Date());
            this.updateById(exist);
            return exist.getId();
        }
    }

    public Integer unRegistry(JobClient jobClient) {
        JobClient exist = lambdaQuery()
                .eq(JobClient::getClientName, jobClient.getClientName())
                .eq(JobClient::getClientAddress, jobClient.getClientAddress())
                .one();
        if(exist != null){
            removeById(exist);
            return exist.getId();
        }
        return null;
    }
}
