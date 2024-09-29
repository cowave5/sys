import request from '@/utils/request'

/**
 * 模块类型选项
 */
export function getOpLogOptions() {
  return request({
    url: '/admin/api/v1/dict/group/types/op_log',
    method: 'get'
  })
}

/**
 * 列表
 */
export function getOpLogList(params) {
  return request({
    url: '/admin/api/v1/oplog',
    method: 'get',
    params: params
  })
}

/**
 * 删除
 */
export function delOpLog(ids) {
  return request({
    url: '/admin/api/v1/oplog/' + ids,
    method: 'delete'
  })
}

/**
 * 清空
 */
export function cleanOpLog() {
  return request({
    url: '/admin/api/v1/oplog/clean',
    method: 'delete'
  })
}

// 日志详情
export function info(id) {
  return request({
    url: '/admin/api/v1/log/info/' + id,
    method: 'get'
  })
}

export function userQuery(data) {
  return request({
    url: '/admin/api/v1/log/user/query',
    method: 'post',
    data: data
  })
}

export function deptQuery(data) {
  return request({
    url: '/admin/api/v1/log/dept/query',
    method: 'post',
    data: data
  })
}

export function postQuery(data) {
  return request({
    url: '/admin/api/v1/log/post/query',
    method: 'post',
    data: data
  })
}
