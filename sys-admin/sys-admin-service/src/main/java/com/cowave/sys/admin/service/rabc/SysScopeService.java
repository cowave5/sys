package com.cowave.sys.admin.service.rabc;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.domain.rabc.SysScope;
import com.cowave.sys.admin.domain.rabc.request.ScopeInfoUpdate;
import com.cowave.sys.admin.domain.rabc.request.ScopeNameUpdate;
import com.cowave.sys.admin.domain.rabc.request.ScopeQuery;
import com.cowave.sys.admin.domain.rabc.request.ScopeStatusUpdate;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysScopeService {

    /**
     * 列表
     */
    Page<SysScope> page(String tenantId, ScopeQuery query);

    /**
     * 详情
     */
    SysScope info(String tenantId, Integer scopeId);

    /**
     * 新增
     */
    void create(String tenantId, SysScope sysScope);

    /**
     * 删除
     */
    void delete(String tenantId, List<Integer> scopeIds);

    /**
     * 修改
     */
    void edit(String tenantId, ScopeNameUpdate nameUpdate);

    /**
     * 修改状态
     */
    void switchStatus(String tenantId, ScopeStatusUpdate statusUpdate);

    /**
     * 修改权限
     */
    void editContent(String tenantId, ScopeInfoUpdate infoUpdate);
}
