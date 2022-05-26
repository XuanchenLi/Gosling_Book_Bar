import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import installElementPlus from './plugins/element'
import axios from 'axios'
import ElementPlus from 'element-plus'
import 'element-plus'

const app = createApp(App)
installElementPlus(app)
app.use(ElementPlus).use(store).use(router).mount('#app')
app.config.globalProperties.$http = axios;

