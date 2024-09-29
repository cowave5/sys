import request from '@/utils/request'

/**
 * 用户组织架构
 */
export function getUserDiagram() {
  return request({
    url: '/admin/api/v1/user/diagram',
    method: 'get'
  })
}

/**
 * 刷新用户组织
 */
export function refreshUserDiagram() {
  return request({
    url: '/admin/api/v1/user/diagram/refresh',
    method: 'get'
  })
}

/**
 * 用户列表
 */
export function getUserList(params) {
  return request({
    url: '/admin/api/v1/user',
    method: 'get',
    params: params
  })
}

/**
 * 用户详情
 */
export function getUserInfo(userId) {
  return request({
    url: '/admin/api/v1/user/' + userId,
    method: 'get'
  })
}

/**
 * 新增用户
 */
export function addUser(data) {
  return request({
    url: '/admin/api/v1/user',
    method: 'post',
    data: data
  })
}

/**
 * 删除用户
 */
export function delUser(userIds) {
  return request({
    url: '/admin/api/v1/user/' + userIds,
    method: 'delete'
  })
}

/**
 * 修改用户
 */
export function updateUser(data) {
  return request({
    url: '/admin/api/v1/user',
    method: 'patch',
    data: data
  })
}

/**
 * 修改角色
 */
export function updateUserRoles(userId, userName, roleIds) {
  const data = {
    userId,
    userName,
    roleIds
  }
  return request({
    url: '/admin/api/v1/user/roles',
    method: 'patch',
    data: data
  })
}

/**
 * 修改状态
 */
export function updateUserStatus(userId, userStatus, userName) {
  const data = {
    userId,
    userName,
    userStatus
  }
  return request({
    url: '/admin/api/v1/user/status',
    method: 'patch',
    data: data
  })
}

/**
 * 修改密码
 */
export function updateUserPasswd(userId, userPasswd, userName) {
  const data = {
    userId,
    userPasswd,
    userName
  }
  return request({
    url: '/admin/api/v1/user/passwd',
    method: 'patch',
    data: data
  })
}

/**
 * 用户流程候选人
 */
export function getUserCandidates(userId) {
  let url;
  if (userId === undefined) {
    url = '/admin/api/v1/user/candidates';
  } else {
    url = '/admin/api/v1/user/candidates?userId=' + userId;
  }
  return request({
    url: url,
    method: 'get'
  })
}

/**
 * 查询用户名称
 */
export function getUserNames(userIds) {
  return request({
    url: '/admin/api/v1/user/name/' + userIds,
    method: 'get'
  })
}

// 角色选择
export function getRoles() {
  return request({
    url: '/admin/api/v1/role',
    method: 'get'
  })
}

// 查询用户个人信息
export function getUserProfile() {
  return request({
    url: '/admin/api/v1/profile/info',
    method: 'get'
  })
}

// 修改用户个人信息
export function updateUserProfile(data) {
  return request({
    url: '/admin/api/v1/profile/edit',
    method: 'post',
    data: data
  })
}

// 用户密码重置
export function updateUserPwd(oldPasswd, newPasswd) {
  const data = {
    oldPasswd,
    newPasswd
  }
  return request({
    url: '/admin/api/v1/profile/passwd/reset',
    method: 'post',
    data: data
  })
}

// 用户头像上传
export function uploadAvatar(data) {
  return request({
    url: '/admin/api/v1/profile/avatar',
    method: 'post',
    data: data
  })
}

