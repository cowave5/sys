import axios from 'axios'
import store from '@/store'
import router from '@/router';
import cache from '@/plugins/cache'
import {i18n} from '@/main'
import responseCode from '@/utils/responseCode'
import {Notification, Message, Loading, MessageBox} from 'element-ui'
import { tansParams, blobValidate } from "@/utils/ruoyi";
import { saveAs } from 'file-saver'
import {refresh} from "@/api/login";

let downloadLoadingInstance;

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'

const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API,
  timeout: 10000
})

service.interceptors.request.use(config => {
  // 鉴权Token
  const notRequireToken = (config.headers || {}).requireToken === false
  if (!notRequireToken && cache.local.getAccessToken()) {
    config.headers['Authorization'] = 'Bearer ' + cache.local.getAccessToken()
  }

  // 国际化
  config.headers['Accept-Language'] = i18n.locale;

  // Get参数
  if (config.method === 'get' && config.params) {
    let url = config.url + '?' + tansParams(config.params);
    url = url.slice(0, -1);
    config.url = url;
    config.params = {};
  }

  const notCheckRepeat = true; // 去掉限制，我们很多post是查询请求
  //const notCheckRepeat = (config.headers || {}).checkRepeat === false
  if (!notCheckRepeat && (config.method === 'post' || config.method === 'put')) {
    const requestObj = {
      url: config.url,
      data: typeof config.data === 'object' ? JSON.stringify(config.data) : config.data,
      time: new Date().getTime()
    }

    const sessionObj = cache.session.getJSON('sessionObj')
    if (sessionObj === undefined || sessionObj === null || sessionObj === '') {
      cache.session.setJSON('sessionObj', requestObj)
    } else {
      const s_url = sessionObj.url;                  // 请求地址
      const s_data = sessionObj.data;                // 请求数据
      const s_time = sessionObj.time;                // 请求时间
      const interval = 1000;                // 间隔时间(ms)，小于此时间视为重复提交
      if (s_url === requestObj.url && s_data === requestObj.data && requestObj.time - s_time < interval) {
        const message = '请求正在处理，请勿重复提交';
        console.warn(`[${s_url}]: ` + message)
        return Promise.reject(new Error(message))
      } else {
        cache.session.setJSON('sessionObj', requestObj)
      }
    }
  }
  return config
}, error => {
    console.log(error)
    Promise.reject(error)
})

let isRejected = false; // 是否以拒绝访问
let isRefreshing = false; // 是否正在刷新Token
let requestsQueue = [];     // 等待Token刷新的请求

service.interceptors.response.use(response => {
    const code = response.data.code || "200";
    const msg = response.data.msg || responseCode[code]
    if(response.request.responseType ===  'blob' || response.request.responseType ===  'arraybuffer'){
      return response.data
    }

    if (code === "498") {
      if (!isRefreshing) {
        isRefreshing = true;
        return refresh(cache.local.getRefreshToken()).then(resp => {
          isRefreshing = false;
          store.commit('SET_TOKEN', resp.data);
          requestsQueue.forEach(cb => cb(resp.data.accessToken));
          store.dispatch('RefreshSocket');
          requestsQueue = [];
          return service(response.config); // 重新发送当前请求
        }).catch(err => {
          isRefreshing = false;
          return Promise.reject(err);
        });
      }else{
        return new Promise(resolve => {
          requestsQueue.push(token => {
            response.config.headers['Authorization'] = `Bearer ${token}`;
            resolve(service(response.config));
          });
        });
      }
    }else if (code === "401") {
      if(!isRejected){
        isRejected = true;
        cache.local.removeAccessToken();
        MessageBox.alert('授权已过期，请重新登录', { type: 'warning' }).then(() => {
          router.push("/login");
        });
      }
      return Promise.reject('授权已过期')
    } else if (code === "500") {
      Message({
        message: msg,
        type: 'error'
      })
      return Promise.reject(new Error(msg))
    } else if (code !== "200") {
      Notification.error({
        title: msg
      })
      return Promise.reject('error')
    } else {
      return response.data
    }
  },
  error => {
    console.log('err' + error)
    let { message } = error;
    if (message === "Network Error") {
      message = "后端接口连接异常";
    }
    else if (message.includes("timeout")) {
      message = "系统接口请求超时";
    }
    else if (message.includes("Request failed with status code")) {
      message = "系统接口" + message.substr(message.length - 3) + "异常";
    }
    Message({
      message: message,
      type: 'error',
      duration: 5 * 1000
    });
    return Promise.reject(error)
  }
)

// 通用下载方法
export function download(url, params, filename) {
  downloadLoadingInstance = Loading.service({ text: "正在下载数据，请稍候", spinner: "el-icon-loading", background: "rgba(0, 0, 0, 0.7)", })
  return service.post(url, params, {
    transformRequest: [(params) => { return tansParams(params) }],
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    responseType: 'blob'
  }).then(async (data) => {
    const isLogin = await blobValidate(data);
    if (isLogin) {
      const blob = new Blob([data])
      saveAs(blob, filename)
    } else {
      const resText = await data.text();
      const rspObj = JSON.parse(resText);
      const errMsg = responseCode[rspObj.code] || rspObj.msg || responseCode['default']
      Message.error(errMsg);
    }
    downloadLoadingInstance.close();
  }).catch((r) => {
    console.error(r)
    Message.error('下载文件出现错误，请联系管理员！')
    downloadLoadingInstance.close();
  })
}

export default service