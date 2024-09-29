import request from '@/utils/request'

/**
 * 获取验证码
 */
export function getCodeImg() {
  return request({
    url: '/admin/api/v1/auth/captcha',
    headers: {
      requireToken: false
    },
    method: 'get',
    timeout: 20000
  })
}

/**
 * 发送邮箱验证码
 */
export function getEmailCode(email) {
  return request({
    url: '/admin/api/v1/auth/captcha/email?email=' + email,
    headers: {
      requireToken: false
    },
    method: 'get',
    timeout: 20000
  })
}

/**
 * 注册
 */
export function register(data) {
  return request({
    url: '/admin/api/v1/auth/register',
    headers: {
      requireToken: false
    },
    method: 'post',
    data: data
  })
}

/**
 * 登录
 */
export function login(userAccount, passWord, code, uuid) {
  const data = {
    userAccount,
    passWord,
    "captcha" : code,
    "captchaId" : uuid
  }
  return request({
    url: '/admin/api/v1/auth/login',
    headers: {
      requireToken: false
    },
    method: 'post',
    data: data
  })
}

/**
 * 登录
 */
export function ldapLogin(userAccount, passWord) {
  const data = {
    userAccount,
    passWord
  }
  return request({
    url: '/admin/api/v1/auth/ldap',
    headers: {
      requireToken: false
    },
    method: 'post',
    data: data
  })
}

/**
 * 登录
 */
export function gitlabLogin(code) {
  return request({
    url: '/admin/api/v1/auth/callback/gitlab?code=' + code,
    headers: {
      requireToken: false
    },
    method: 'get'
  })
}

/**
 * 退出登录
 */
export function logout() {
  return request({
    url: '/admin/api/v1/auth/logout',
    method: 'post'
  })
}

/**
 * 刷新令牌
 */
export function refresh(token) {
  return request({
    url: '/admin/api/v1/auth/refresh?refreshToken=' + token,
    method: 'get'
  })
}

/**
 * 获取登录信息
 */
export function getAuthInfo() {
  return request({
    url: '/admin/api/v1/auth/info',
    method: 'get'
  })
}

/**
 * 获取权限路由
 */
export const getAuthRouters = () => {
  return request({
    url: '/admin/api/v1/auth/routes',
    method: 'get'
  })
}