package com.cowave.sys.admin.infra.rabc.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.domain.rabc.SysScope;
import com.cowave.sys.admin.domain.rabc.request.ScopeQuery;
import com.cowave.sys.admin.infra.rabc.dao.mapper.SysScopeMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class SysScopeDao extends ServiceImpl<SysScopeMapper, SysScope> {

    /**
     * 分页查询
     */
    public Page<SysScope> page(String tenantId, ScopeQuery query) {
        return lambdaQuery()
                .eq(SysScope::getTenantId, tenantId)
                .eq(StringUtils.isNotBlank(query.getScopeModule()), SysScope::getScopeModule, query.getScopeModule())
                .page(Access.page());
    }

    /**
     * 查询（id）
     */
    public SysScope getById(String tenantId, Integer scopeId) {
        return lambdaQuery()
                .eq(SysScope::getTenantId, tenantId)
                .eq(SysScope::getScopeId, scopeId)
                .one();
    }

    /**
     * 删除
     */
    public void deleteByIds(String tenantId, List<Integer> scopeIds) {
        lambdaUpdate()
                .eq(SysScope::getTenantId, tenantId)
                .in(SysScope::getScopeId, scopeIds)
                .remove();
    }

    /**
     * 修改权限名称
     */
    public void updateNameById(String tenantId, Integer scopeId, String scopeName) {
        lambdaUpdate()
                .eq(SysScope::getTenantId, tenantId)
                .eq(SysScope::getScopeId, scopeId)
                .set(SysScope::getScopeName, scopeName)
                .update();
    }

    /**
     * 修改权限状态
     */
    public void updateStatusById(String tenantId, Integer scopeId, Integer status){
        lambdaUpdate()
                .eq(SysScope::getTenantId, tenantId)
                .eq(SysScope::getScopeId, scopeId)
                .set(SysScope::getScopeStatus, status)
                .update();
    }
}
