import request from '@/utils/request'

/**
 * 租户列表
 */
export function getTenantList(params) {
  return request({
    url: '/admin/api/v1/tenant',
    method: 'get',
    params: params
  })
}

/**
 * 租户详情
 */
export function getTenant(tenantId) {
  return request({
    url: '/admin/api/v1/tenant/' + tenantId,
    method: 'get'
  })
}

/**
 * 新增租户
 */
export function addTenant(data) {
  return request({
    url: '/admin/api/v1/tenant',
    method: 'post',
    data: data
  })
}

/**
 * 获取配置值
 */
export function getConfigValue(configKey) {
  return request({
    url: '/admin/api/v1/config/value/' + configKey,
    method: 'get'
  })
}


