package com.cowave.sys.admin.service.rabc.impl;

import com.cowave.sys.admin.domain.rabc.SysTenant;
import com.cowave.sys.admin.infra.rabc.dao.SysTenantDao;
import com.cowave.sys.admin.service.rabc.SysTenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysTenantServiceImpl implements SysTenantService {

    private final SysTenantDao sysTenantDao;

    @Override
    public List<SysTenant> list() {
        return sysTenantDao.list();
    }
}
