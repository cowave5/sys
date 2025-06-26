import {login, logout, getAuthInfo, ldapLogin, gitlabLogin, logon} from '@/api/auth'
import cache from "@/plugins/cache";

const user = {
  state: {
    accessToken: cache.local.getAccessToken(),
    refreshToken: cache.local.getRefreshToken(),
    tenantId: null,
    userId: null,
    name: '',
    avatar: '',
    roles: [],
    permissions: []
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      cache.local.setToken(token);
    },
    SET_TENANT: (state, tenantId) => {
      state.tenantId = tenantId
    },
    SET_USERID: (state, userId) => {
      state.userId = userId
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    },
  },

  actions: {
    Logon({ commit }, userInfo) {
      const tenantId = userInfo.tenantId;
      const username = userInfo.username.trim();
      const password = userInfo.password;
      return new Promise((resolve, reject) => {
        logon(tenantId, username, password).then(res => {
          commit('SET_TOKEN', res.data);
          resolve();
        }).catch(error => {
          reject(error)
        })
      })
    },

    Login({ commit }, userInfo) {
      const tenantId = userInfo.tenantId;
      const username = userInfo.username.trim();
      const password = userInfo.password;
      const code = userInfo.code;
      const uuid = userInfo.uuid;
      return new Promise((resolve, reject) => {
        login(tenantId, username, password, code, uuid).then(res => {
          commit('SET_TOKEN', res.data);
          resolve();
        }).catch(error => {
          reject(error)
        })
      })
    },

    Ldap({ commit }, ldapInfo) {
      const username = ldapInfo.username.trim()
      const password = ldapInfo.password
      return new Promise((resolve, reject) => {
        ldapLogin(username, password).then(res => {
          commit('SET_TOKEN', res.data);
          resolve();
        }).catch(error => {
          reject(error)
        })
      })
    },

    OauthGitlab({ commit }, code) {
      return new Promise((resolve, reject) => {
        gitlabLogin(code).then(res => {
          commit('SET_TOKEN', res.data);
          resolve();
        }).catch(error => {
          reject(error)
        })
      })
    },

    LogOut({ commit }) {
      return new Promise((resolve, reject) => {
        logout().then(() => {
          cache.local.removeAccessToken();
          commit('SET_ROLES', [])
          commit('SET_PERMISSIONS', [])
          commit('SET_USERID', null)
          commit('SET_NAME', '')
          commit('SET_AVATAR', '')
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    GetInfo({ commit }) {
      return new Promise((resolve, reject) => {
        getAuthInfo().then(res => {
          const user = res.data
          const avatar = (user.avatar === "" || user.avatar == null) ? require("@/assets/images/profile.jpg") : user.avatar;
          commit('SET_TENANT', user.tenantId)
          commit('SET_USERID', user.userId)
          commit('SET_NAME', user.userName)
          commit('SET_AVATAR', avatar)
          commit('SET_ROLES', user.roles)
          commit('SET_PERMISSIONS', user.permissions)
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },

  }
}

export default user
