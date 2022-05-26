import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '@/views/LoginView'
import RegisterView from '@/views/RegisterView'
import storage from '@/utils/storage'
import store from '@/store'
import SearchEmployeeView from '@/views/SearchEmployeeView'

const routes = [
  {
    path: '/',
    name: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView
  },
  {
    path: '/register',
    name: 'register',
    component: RegisterView
  },
  {
    path: '/home',
    name: '/home',
    component: HomeView,
    meta: {
      requireAuth: true
    },
    children: [
      {
        path: 'searchEmployee',
        name: 'searchEmployee',
        component: SearchEmployeeView
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})
// 设置路由守卫，在进页面之前，判断有token，才进入页面，否则返回登录页面
if (storage.get("token")) {
  store.commit("set_token", storage.get("token"));
}
router.beforeEach((to, from, next) => {
  // 判断要去的路由有没有requiresAuth
  // to.matched.some(r => r.meta.requireAuth) or to.meta.requiresAuth
  document.title = to.meta.title || '员工管理系统'
  if (to.matched.some(r => r.meta.requireAuth)) {
    if (store.state.token) {
      next(); //有token,进行request请求，后台还会验证token
    } else {
      next({
        path: "/login",
        // 将刚刚要去的路由path（却无权限）作为参数，方便登录成功后直接跳转到该路由，这要进一步在登陆页面判断
        query: {
          redirect: to.fullPath }
      });
    }
  } else {
    next(); //如果无需token,那么随它去吧
  }
});
export default router
