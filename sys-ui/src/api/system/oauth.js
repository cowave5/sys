import request from '@/utils/request'

/**
 * 获取配置
 */
export function getConfig(appType) {
  return request({
    url: '/admin/api/v1/oauth/config/' + appType,
    method: 'get'
  })
}

/**
 * 保存配置
 */
export function saveConfig(data) {
  return request({
    url: '/admin/api/v1/oauth/config',
    method: 'patch',
    data: data
  })
}

/**
 * 用户列表
 */
export function listUser(params) {
  return request({
    url: '/admin/api/v1/oauth/user',
    method: 'get',
    params: params

  })
}

/**
 * 修改用户角色
 */
export function changeGitlabRole(userId, roleCode) {
  return request({
    url: '/admin/api/v1/oauth/user/role/' + userId + '/' + roleCode,
    method: 'patch'
  })
}

/**
 * 删除用户
 */
export function deleteGitlabUser(userId) {
  return request({
    url: '/admin/api/v1/oauth/user/' + userId,
    method: 'delete'
  })
}
