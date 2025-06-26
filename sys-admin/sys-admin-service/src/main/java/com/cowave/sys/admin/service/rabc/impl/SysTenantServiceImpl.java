package com.cowave.sys.admin.service.rabc.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.domain.rabc.SysTenant;
import com.cowave.sys.admin.domain.rabc.request.TenantCreate;
import com.cowave.sys.admin.domain.rabc.request.TenantQuery;
import com.cowave.sys.admin.infra.base.dao.SysAttachDao;
import com.cowave.sys.admin.infra.rabc.dao.SysTenantDao;
import com.cowave.sys.admin.service.rabc.SysTenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysTenantServiceImpl implements SysTenantService {

    private final SysTenantDao sysTenantDao;
    private final SysAttachDao sysAttachDao;

    @Override
    public Page<SysTenant> page(TenantQuery query) {
        return sysTenantDao.list(query);
    }

    @Override
    public SysTenant info(String tenantId) {
        return sysTenantDao.getById(tenantId);
    }

    @Override
    public void creat(TenantCreate tenantCreate) {
        sysTenantDao.save(tenantCreate);
        sysAttachDao.updateOwnerById(tenantCreate.getTenantId(), tenantCreate.getAttachId());
    }
}
