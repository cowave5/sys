import request from '@/utils/request'

/**
 * 获取配置
 */
export function getLdapConfig() {
  return request({
    url: '/admin/api/v1/ldap/config',
    method: 'get'
  })
}

/**
 * 保存配置
 */
export function saveLdapConfig(data) {
  return request({
    url: '/admin/api/v1/ldap/config',
    method: 'patch',
    data: data
  })
}

/**
 * 测试配置
 */
export function validLdapConfig(data) {
  return request({
    url: '/admin/api/v1/ldap/config/valid',
    method: 'post',
    data: data
  })
}

/**
 * 用户列表
 */
export function getLdapUsers(params) {
  return request({
    url: '/admin/api/v1/ldap/user',
    method: 'get',
    params: params
  })
}
