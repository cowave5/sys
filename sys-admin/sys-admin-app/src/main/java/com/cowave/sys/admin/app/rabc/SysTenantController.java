package com.cowave.sys.admin.app.rabc;

import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.admin.domain.rabc.SysTenant;
import com.cowave.sys.admin.domain.rabc.request.TenantCreate;
import com.cowave.sys.admin.domain.rabc.request.TenantQuery;
import com.cowave.sys.admin.service.rabc.SysTenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 租户
 * @order 0
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tenant")
public class SysTenantController {

    private final SysTenantService sysTenantService;

    /**
     * 列表
     */
    @PreAuthorize("@permit.hasPermit('sys:tenant:query')")
    @GetMapping
    public Response<Response.Page<SysTenant>> list(TenantQuery query) {
        return Response.page(sysTenantService.page(query));
    }

    /**
     * 详情
     */
    @PreAuthorize("@permit.hasPermit('sys:tenant:query')")
    @GetMapping("/{tenantId}")
    public Response<SysTenant> info(@PathVariable String tenantId) {
        return Response.success(sysTenantService.info(tenantId));
    }

    /**
     * 新增
     */
    @PreAuthorize("@permit.hasPermit('sys:tenant:creat')")
    @PostMapping
    public Response<Void> creat(@RequestBody TenantCreate tenantCreate) throws Exception {
        return Response.success(() -> sysTenantService.creat(tenantCreate));
    }
}
