import Vue from 'vue';
import App from './App.vue';
import router from './router';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import Axios from 'axios';
import store from './store';
import VueQuillEditor from 'vue-quill-editor'
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'


// 设置默认请求的 header 中的 Authorization 字段
Axios.defaults.headers.common['Authorization'] = 'Bearer ' + localStorage.getItem('jwt')
//设置 Axios 的默认 baseURL
Axios.defaults.baseURL='/api'
// 将 Axios 实例挂载到 Vue.prototype 上，以便全局使用
Vue.prototype.$axios = Axios
// 修改 Vue.config 的配置
Vue.config.productionTip = false

Vue.use(ElementUI);
Vue.use(VueQuillEditor)

const instance = Axios.create({
  // 配置选项
});

instance.interceptors.response.use(
  response => response, 
  error => {
    if (error.response.status === 401) {
      store.dispatch('logout').then(() => {
        router.push('/login')
      })
      // 处理错误逻辑
    }
    return Promise.reject(error);
  }
);


new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
