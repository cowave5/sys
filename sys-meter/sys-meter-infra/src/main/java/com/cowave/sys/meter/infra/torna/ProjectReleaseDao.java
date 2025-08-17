package com.cowave.sys.meter.infra.torna;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.ProjectRelease;
import com.cowave.sys.meter.infra.torna.mapper.ProjectReleaseMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectReleaseDao extends ServiceImpl<ProjectReleaseMapper, ProjectRelease> {

}
