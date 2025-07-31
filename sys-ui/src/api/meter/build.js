import request from '@/utils/request'

/**
 * 目录结构
 */
export function buildFolderTree() {
  return request({
    url: '/meter/api/v1/build/folder',
    method: 'get'
  })
}

/**
 * 目录拖拽
 */
export function buildFolderDrag(data) {
  return request({
    url: '/meter/api/v1/build/folder/drag',
    method: 'patch',
    data: data
  })
}

/**
 * 新建目录
 */
export function creatBuildFolder(data) {
  return request({
    url: '/meter/api/v1/build/folder',
    method: 'post',
    data: data
  })
}

/**
 * 删除目录
 */
export function deleteBuildFolder(folderId) {
  return request({
    url: '/meter/api/v1/build/folder/' + folderId,
    method: 'delete'
  })
}

/**
 * 修改目录名称
 */
export function buildFolderRename(data) {
  return request({
    url: '/meter/api/v1/build/folder/name',
    method: 'patch',
    data: data
  })
}

/**
 * 修改目录访问限制
 */
export function buildFolderVisibility(data) {
  return request({
    url: '/meter/api/v1/build/folder/visibility',
    method: 'patch',
    data: data
  })
}
