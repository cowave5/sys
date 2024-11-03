import request from '@/utils/request'
import { appendUrl } from '@/utils/ruoyi'

/**
 * 部门成员
 */
export function listMembers(deptId, page, pageSize) {
  return request({
    url: '/admin/api/v1/dept/members/' + deptId + "?page=" + page + "&pageSize=" + pageSize,
    method: 'get'
  })
}

/**
 * 部门列表
 */
export function listDept(params) {
  return request({
    url: appendUrl('/admin/api/v1/dept', params),
    method: 'get'
  })
}

/**
 * 部门详情
 */
export function getDept(deptId) {
  return request({
    url: '/admin/api/v1/dept/' + deptId,
    method: 'get'
  })
}

/**
 * 新增部门
 */
export function addDept(data) {
  return request({
    url: '/admin/api/v1/dept',
    method: 'post',
    data: data
  })
}

// 部门只读修改
export function changeReadonly(deptId, readOnly, deptName) {
  const data = {
    deptId,
    readOnly,
    deptName
  }
  return request({
    url: '/admin/api/v1/dept/change/readonly',
    method: 'post',
    data: data
  })
}

// 查询部门树选项
export function getDeptTree(deptId) {
  return request({
    url: '/admin/api/v1/dept/tree?deptId=' + deptId,
    method: 'get'
  })
}

// 查询部门下拉树结构
export function treeselect() {
  return request({
    url: '/admin/api/v1/dept/tree',
    method: 'get'
  })
}

// 查询用户树选项
export function getUserTree() {
  return request({
    url: '/admin/api/v1/user/tree',
    method: 'get'
  })
}

// 修改部门
export function updateDept(data) {
  return request({
    url: '/admin/api/v1/dept/edit',
    method: 'post',
    data: data
  })
}

// 删除部门
export function delDept(deptId) {
  return request({
    url: '/admin/api/v1/dept/delete?deptId=' + deptId,
    method: 'get'
  })
}

// 刷新缓存
export function refreshCache() {
  return request({
    url: '/admin/api/v1/dept/refresh',
    method: 'get'
  })
}

// 获取部门岗位
export function getDeptPosts(deptId) {
  return request({
    url: '/admin/api/v1/dept/posts/id/' + deptId,
    method: 'get'
  })
}

// 设置部门岗位
export function setDeptPosts(data) {
  return request({
    url: '/admin/api/v1/dept/posts/set',
    method: 'post',
    data: data
  })
}

// 部门人员，id获取
export function getDeptUsersById(deptId) {
  return request({
    url: '/admin/api/v1/dept/users/id/' + deptId,
    method: 'get'
  })
}

// 获取部门人员
export function getDeptUsersByCode(deptCode) {
  return request({
    url: '/admin/api/v1/dept/users/code/' + deptCode,
    method: 'get'
  })
}

// 设置部门人员
export function setDeptUsers(data) {
  return request({
    url: '/admin/api/v1/dept/users/set',
    method: 'post',
    data: data
  })
}
