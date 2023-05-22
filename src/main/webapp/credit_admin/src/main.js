// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.

import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import Vue from 'vue'
import App from './App'
import router from './router'

// 引入图表组件
import VCharts from 'v-charts'
Vue.use(VCharts)

// 引入登录iconwenjain
import './assets/shtl_icon/iconfont.css'

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
Vue.use(ElementUI)
// 设置反向代理，前端请求默认发送到 http://localhost:8443/api
var axios = require('axios')
axios.defaults.baseURL = 'http://localhost:8080/credit/api'
// 全局注册，之后可在其他组件中通过 this.$axios 发送数据
Vue.prototype.$axios = axios

// 阻止vue 在启动时生成生产提示
Vue.config.productionTip = false



// 请求在到达服务器之前，先会调用use中的这个回调函数来添加请求头信息
axios.interceptors.request.use(config => {
  // console.log(config)
  // 为请求头对象，添加token验证的Authorization字段
  config.headers.token = window.sessionStorage.getItem('token')
  // 在最后必须 return config
  return config
})




/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>',
  beforeCreate() {
	  //  设置监听当前路径发生改变的方法
	  Vue.prototype.resetSetItem = function(key ,newVal) {
	  	if (key === 'activePath') {
	  		// 创建一个 StorageEvent 事件
	  		var newStorageEvent = document.createEvent('StorageEvent');
	  		const storage = {
	  			setItem: function(k ,val) {
	  				sessionStorage.setItem(k,val);
	  				// 初始化创建的事件
	  				newStorageEvent.initStorageEvent('setItem',false,false,k,null,val,null,null);
	  				// 派发对象
	  				window.dispatchEvent(newStorageEvent)
	  			}
	  		}
	  		return storage.setItem(key,newVal);
	  	}
	  }
  }
})



