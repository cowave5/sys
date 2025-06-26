package com.cowave.sys.admin.service.rabc;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.domain.rabc.SysTenant;
import com.cowave.sys.admin.domain.rabc.request.TenantCreate;
import com.cowave.sys.admin.domain.rabc.request.TenantQuery;

/**
 * @author shanhuiming
 */
public interface SysTenantService {

    /**
     * 列表
     */
    Page<SysTenant> page(TenantQuery query);

    /**
     * 详情
     */
    SysTenant info(String tenantId);

    /**
     * 新增
     */
    void creat(TenantCreate tenantCreate);
}
