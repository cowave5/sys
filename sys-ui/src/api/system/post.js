import request from '@/utils/request'

/**
 * 岗位列表
 */
export function listPost(params) {
  return request({
    url: '/admin/api/v1/post',
    method: 'get',
    params: params
  })
}

/**
 * 岗位详情
 */
export function getPost(postId) {
  return request({
    url: '/admin/api/v1/post/' + postId,
    method: 'get'
  })
}

/**
 * 新增岗位
 */
export function addPost(data) {
  return request({
    url: '/admin/api/v1/post',
    method: 'post',
    data: data
  })
}

/**
 * 删除岗位
 */
export function delPost(postIds) {
  return request({
    url: '/admin/api/v1/post/' + postIds,
    method: 'delete'
  })
}

/**
 * 修改岗位
 */
export function updatePost(data) {
  return request({
    url: '/admin/api/v1/post',
    method: 'patch',
    data: data
  })
}

/**
 * 修改只读
 */
export function changeReadonly(postId, readOnly, postName) {
  const data = {
    postId,
    readOnly,
    postName
  }
  return request({
    url: '/admin/api/v1/post/readonly',
    method: 'patch',
    data: data
  })
}

/**
 * 岗位组织架构
 */
export function getPostDiagram() {
  return request({
    url: '/admin/api/v1/post/diagram',
    method: 'get'
  })
}

/**
 * 刷新岗位组织
 */
export function refreshDiagram() {
  return request({
    url: '/admin/api/v1/post/diagram/refresh',
    method: 'get'
  })
}

/**
 * 岗位流程候选人
 */
export function getPostCandidatesByCode(postCode) {
  return request({
    url: '/admin/api/v1/post/candidates/' + postCode,
    method: 'get'
  })
}
