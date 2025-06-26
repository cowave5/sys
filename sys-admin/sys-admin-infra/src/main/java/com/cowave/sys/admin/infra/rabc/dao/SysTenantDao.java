package com.cowave.sys.admin.infra.rabc.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.domain.rabc.SysTenant;
import com.cowave.sys.admin.domain.rabc.request.TenantQuery;
import com.cowave.sys.admin.infra.rabc.dao.mapper.SysTenantMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * @author shanhuiming
 */
@Repository
public class SysTenantDao extends ServiceImpl<SysTenantMapper, SysTenant> {

    private static final String format = "%s-%s-%s";

    /**
     * 下一个用户编码
     */
    public String nextUserCode(String tenantId, String userType) {
        Integer userIndex = nextUserIndex(tenantId);
        return format.formatted(tenantId, userType, String.format("%05d", userIndex));
    }

    /**
     * 下一个用户序号
     */
    public Integer nextUserIndex(String tenantId) {
        while (true) {
            Integer currentIndex = lambdaQuery().eq(SysTenant::getTenantId, tenantId)
                    .oneOpt().map(SysTenant::getUserIndex).orElse(null);
            if (currentIndex == null) {
                return 1;
            }

            boolean updated = lambdaUpdate()
                    .eq(SysTenant::getTenantId, tenantId)
                    .eq(SysTenant::getUserIndex, currentIndex)
                    .setSql("user_index = user_index + 1")
                    .update();
            if (updated) {
                return currentIndex + 1;
            }
        }
    }

    /**
     * 分页查询
     */
    public Page<SysTenant> list(TenantQuery query) {
        return lambdaQuery()
                .like(StringUtils.isNotBlank(query.getTenantId()), SysTenant::getTenantId, query.getTenantId())
                .page(Access.page());
    }
}
