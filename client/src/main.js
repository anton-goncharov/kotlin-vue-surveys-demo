import Vue from 'vue'
import App from './App.vue'
import { router } from './router'
import { store } from './store'
import ImageUploader from 'vue-image-upload-resize'

Vue.config.productionTip = false

Vue.prototype.$apiUrl = process.env.VUE_APP_BACKEND_URL

new Vue({
  render: h => h(App),
  router,
  store
}).$mount('#app')

Vue.use(ImageUploader);

