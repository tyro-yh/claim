// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router.js'
import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import store from './store'
import 'font-awesome/css/font-awesome.min.css'
import jsPlumb from 'jsplumb'
Vue.use(Element)
Vue.prototype.$store = store
Vue.config.productionTip = false
Vue.prototype.$jsPlumb = jsPlumb.jsPlumb

/* eslint-disable no-new */
new Vue({
  el: '#app',
  template: '<App/>',
  router: router,
  store,
  components: { App }
})
