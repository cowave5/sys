import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import cache from "@/plugins/cache";

NProgress.configure({ showSpinner: false })

const whiteList = ['/login', '/oauth/gitlab', '/bind', '/register']

// 路由跳转
router.beforeEach((to, from, next) => {
  NProgress.start()
  // 检查AccessToken（本地持久化）
  if (cache.local.getAccessToken()) {
    to.meta.title && store.dispatch('settings/setTitle', to.meta.title)
    // 如果登录Url，直接将next置为首页
    if (to.path === '/login' || to.path === '/oauth/gitlab') {
      next({ path: '/' })
      NProgress.done()
    } else {
      // 检查用户权限信息（缓存），没有就重新获取
      if (store.getters.roles.length === 0) {
        store.dispatch('GetInfo').then(() => {
          // 重新获取路由菜单权限
          store.dispatch('GenerateRoutes').then(accessRoutes => {
            router.addRoutes(accessRoutes) // 动态添加可访问路由表
            next({ ...to, replace: true }) // hack方法 确保addRoutes已完成
          })
        }).catch(err => {
            store.dispatch('LogOut').then(() => {
              Message.error(err)
              next({ path: '/' })
            })
          })
      } else {
        next()
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})