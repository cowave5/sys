import Vue from 'vue'

import Cookies from 'js-cookie'

import Element from 'element-ui'
import './assets/styles/element-variables.scss'

import '@/assets/styles/index.scss' // global css
import '@/assets/styles/ruoyi.scss' // ruoyi css
import App from './App'
import store from './store'
import router from './router'
import directive from './directive' // directive
import plugins from './plugins' // plugins
import { download } from '@/utils/request'

import './assets/icons' // icon
import './permission' // permission control
import { getConfigValue } from "@/api/system/config";
import { parseTime, resetForm, addDateRange, handleTree } from "@/utils/ruoyi";
// 分页组件
import Pagination from "@/components/Pagination";
// 自定义表格工具组件
import RightToolbar from "@/components/RightToolbar"
// 富文本组件
import Editor from "@/components/Editor"
// 文件上传组件
import FileUpload from "@/components/FileUpload"
// 图片上传组件
import ImageUpload from "@/components/ImageUpload"
// 图片预览组件
import ImagePreview from "@/components/ImagePreview"
// 字典标签组件
import DictTag from '@/components/DictTag'
// 头部标签组件
import VueMeta from 'vue-meta'
// 字典数据组件
import DictData from '@/components/DictData'

import '@/utils/drag.js'

import EventEmitter from '@/utils/event-emitter'

// 国际化
import VueI18n from 'vue-i18n';
import ElementLocale from 'element-ui/lib/locale'

import en from '@/language/en'
import zh from '@/language/zh'
import enLocale from 'element-ui/lib/locale/lang/en';
import zhLocale from 'element-ui/lib/locale/lang/zh-CN';

import ElSelectTree from "el-select-tree";

window.Vue = Vue

export const i18n = new VueI18n({
  locale: "zh",
  messages: {
    'en': Object.assign(en, enLocale),
    'zh': Object.assign(zh, zhLocale)
  }
});

ElementLocale.i18n((key, value) => i18n.t(key, value));

Vue.prototype.$eventEmitter = EventEmitter

// 全局方法挂载
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.download = download
Vue.prototype.handleTree = handleTree

// 全局组件挂载
Vue.component('DictTag', DictTag)
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)
Vue.component('Editor', Editor)
Vue.component('FileUpload', FileUpload)
Vue.component('ImageUpload', ImageUpload)
Vue.component('ImagePreview', ImagePreview)

Vue.use(directive)
Vue.use(plugins)
Vue.use(VueMeta)
Vue.use(ElSelectTree)
DictData.install()

import ElTreeSelect from 'el-tree-select';
Vue.use(ElTreeSelect);

import Contextmenu from "vue-contextmenujs"
Vue.use(Contextmenu);

import 'vue2-org-tree/dist/style.css';
import Vue2OrgTree from 'vue2-org-tree'
Vue.use(Vue2OrgTree);

Element.Dialog.props.closeOnClickModal.default = false
Vue.use(Element, {
  size: Cookies.get('size') || 'medium'
})

Vue.config.productionTip = false

new Vue({
  el: '#app',
  i18n,
  router,
  store,
  render: h => h(App)
})
