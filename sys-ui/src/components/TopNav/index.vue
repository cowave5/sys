<template>
  <el-menu :default-active="activeMenu" mode="horizontal" @select="handleSelect">
    <template v-for="(item, index) in topMenus">
      <el-menu-item :style="{'--theme': theme}" :index="item.path" :key="index" v-if="index < visibleNumber">
        <svg-icon :icon-class="item.meta.icon"/> {{ $t(item.meta.title) }}
      </el-menu-item>
    </template>

    <!-- 顶部菜单超出数量折叠 -->
    <el-submenu :style="{'--theme': theme}" index="more" v-if="topMenus.length > visibleNumber">
      <template slot="title">{{ $t('commons.button.more') }}</template>
      <template v-for="(item, index) in topMenus">
        <el-menu-item :index="item.path" :key="index" v-if="index >= visibleNumber">
          <svg-icon :icon-class="item.meta.icon"/> {{ $t(item.meta.title) }}
        </el-menu-item>
      </template>
    </el-submenu>
  </el-menu>
</template>

<script>
import { constantRoutes } from "@/router";
import ParentView from '@/components/ParentView'

// 隐藏侧边栏路由
const hideList = ['/index', '/user/profile', '/user/notice', '/user/token'];

export default {
  data() {
    return {
      // 顶部栏初始数
      visibleNumber: 5,
      // 当前激活菜单的 index
      currentIndex: undefined
    };
  },
  computed: {
    theme() {
      return this.$store.state.settings.theme;
    },
    // 顶部显示菜单
    topMenus() {
      let topMenus = [];
      // 首页
      topMenus.push({ path: '/index', meta: { title: 'commons.menu.dashboard', icon: "index" }});
      // 其它路由
      this.routers.map((menu) => {
        if (menu.hidden !== true) {
          // 兼容顶部栏一级菜单内部跳转
          if (menu.path === "/") {
              topMenus.push(menu.children[0]);
          } else {
              topMenus.push(menu);
          }
        }
      });
      return topMenus;
    },
    // 所有的路由信息
    routers() {
      return this.$store.state.permission.topbarRouters;
    },
    // 设置子路由
    childrenMenus() {
      var childrenMenus = [];
      this.routers.map((router) => {
        for (var item in router.children) {
          if (router.children[item].parentPath === undefined) {
            if(router.path === "/") {
              router.children[item].path = "/" + router.children[item].path;
            } else {
              if(!this.ishttp(router.children[item].path)) {
                router.children[item].path = router.path + "/" + router.children[item].path;
              }
            }
            router.children[item].parentPath = router.path;
          }
          childrenMenus.push(router.children[item]);
        }
      });
      return constantRoutes.concat(childrenMenus);
    },
    // 默认激活的菜单
    activeMenu() {
      const path = this.$route.path;
      let activePath = path;
      if (path !== undefined && path.lastIndexOf("/") > 0 && hideList.indexOf(path) === -1) {
        const tmpPath = path.substring(1, path.length);
        activePath = "/" + tmpPath.substring(0, tmpPath.indexOf("/"));
        this.$store.dispatch('app/toggleSideBarHide', false);
        this.activeRoutes(activePath);
        return activePath;
      } else if(path === "/user/profile" || path === "/user/notice" || path === "/user/token"){
        var routes = [];
        routes.push({ path: '/user/profile', meta: { title: 'commons.theme.profile', icon: "user" }});
        routes.push({ path: '/user/notice', meta: { title: 'commons.theme.notice', icon: "notice" }});
        routes.push({ path: '/user/token', meta: { title: 'commons.theme.token', icon: "token" }});
        this.$store.commit("SET_SIDEBAR_ROUTERS", routes);
        this.$store.dispatch('app/toggleSideBarHide', false);
      } else if(!this.$route.children) {
        activePath = path;
        this.$store.dispatch('app/toggleSideBarHide', true);
        this.activeRoutes(activePath);
        return activePath;
      }
    },
  },
  beforeMount() {
    window.addEventListener('resize', this.setVisibleNumber)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.setVisibleNumber)
  },
  mounted() {
    this.setVisibleNumber();
  },
  methods: {
    // 根据宽度计算设置显示栏数
    setVisibleNumber() {
      const width = document.body.getBoundingClientRect().width / 3;
      this.visibleNumber = parseInt(width / 85);
    },
    // 菜单选择事件
    handleSelect(key, keyPath) {
      this.currentIndex = key;
      const route = this.routers.find(item => item.path === key);
      if (this.ishttp(key)) {
        // http(s):// 路径新窗口打开
        window.open(key, "_blank");
      } else if (!route || !route.children) {
        // 没有子路由路径内部打开
        this.$router.push({ path: key });
        this.$store.dispatch('app/toggleSideBarHide', true);
      } else {
        // 显示左侧联动菜单
        var routes = []
        if (this.childrenMenus && this.childrenMenus.length > 0) {
          this.childrenMenus.map((item) => {
            if (key == item.parentPath || (key == 'index' && '' == item.path)) {
              routes.push(item)
            }
          })
        }
        if (routes.length > 0) {
          this.$store.commit('SET_SIDEBAR_ROUTERS', routes)
        }

        // 找出第一个 component 不为空的路由
        const firstValid = routes.find(r => r.component
            && r.component !== 'Layout'
            && r.component !== ParentView
            && r.component !== 'InnerLink'
            && r.hidden !== true)
        if (firstValid && firstValid.path) {
          this.$router.push({ path: firstValid.path })
        }

        this.$store.dispatch('app/toggleSideBarHide', false);
      }
    },
    // 当前激活的路由
    activeRoutes(key) {
      var routes = [];
      if (this.childrenMenus && this.childrenMenus.length > 0) {
        this.childrenMenus.map((item) => {
          if (key == item.parentPath || (key == "index" && "" == item.path)) {
            routes.push(item);
          }
        });
      }
      if(routes.length > 0) {
        this.$store.commit("SET_SIDEBAR_ROUTERS", routes);
      }
    },
    ishttp(url) {
      return url.indexOf('http://') !== -1 || url.indexOf('https://') !== -1
    }
  },
};
</script>

<style lang="scss">
.topmenu-container.el-menu--horizontal > .el-menu-item {
  float: left;
  height: 50px !important;
  line-height: 50px !important;
  color: #999093 !important;
  padding: 0 5px !important;
  margin: 0 10px !important;
}

.topmenu-container.el-menu--horizontal > .el-menu-item.is-active, .el-menu--horizontal > .el-submenu.is-active .el-submenu__title {
  border-bottom: 2px solid #{'var(--theme)'} !important;
  color: #303133;
}

/* submenu item */
.topmenu-container.el-menu--horizontal > .el-submenu .el-submenu__title {
  float: left;
  height: 50px !important;
  line-height: 50px !important;
  color: #999093 !important;
  padding: 0 5px !important;
  margin: 0 10px !important;
}
</style>
