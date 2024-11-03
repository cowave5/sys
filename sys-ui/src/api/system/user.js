import request from '@/utils/request'
import { appendUrl } from '@/utils/ruoyi'

/**
 * 用户列表
 */
export function userList(params) {
  return request({
    url: appendUrl('/admin/api/v1/user', params),
    method: 'get'
  })
}

/**
 * 用户详情
 */
export function userInfo(userId) {
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
 * 删除用户
 */
export function delUser(userIds) {
  return request({
    url: '/admin/api/v1/user/' + userIds,
    method: 'delete'
  })
}

/**
 * 修改角色
 */
export function updateAuthRole(userId, userName, roleIds) {
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
export function changeStatus(userId, userStatus, userName) {
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
export function changePasswd(userId, userPasswd, userName) {
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
 * 修改只读
 */
export function changeReadonly(userId, readOnly, userName) {
  const data = {
    userId,
    readOnly,
    userName
  }
  return request({
    url: '/admin/api/v1/user/readonly',
    method: 'patch',
    data: data
  })
}

// 岗位关系
export function getTree() {
  return request({
    url: '/admin/api/v1/user/tree',
    method: 'get'
  })
}

// 刷新字典缓存
export function refreshCache() {
  return request({
    url: '/admin/api/v1/user/refresh',
    method: 'get'
  })
}

// 角色选择
export function getRoles() {
  return request({
    url: '/admin/api/v1/role/list',
    method: 'post',
    data: {}
  })
}

// 岗位选择
export function getPosts(deptId) {
  return request({
    url: '/admin/api/v1/post/tree/dept',
    method: 'get'
  })
}

// 用户选择
export function getUsers() {
  return request({
    url: '/admin/api/v1/user/tree/dept',
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

// 用户部门领导
export function userLeaders(userId) {
  let url;
  if (userId === undefined) {
    url = '/admin/api/v1/user/leaders';
  } else {
    url = '/admin/api/v1/user/leaders?userId=' + userId;
  }
  return request({
    url: url,
    method: 'get'
  })
}

