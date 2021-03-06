import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '@/views/LoginView'
import RegisterView from '@/views/RegisterView'
import storage from '@/utils/storage'
import store from '@/store'
import SearchEmployeeView from '@/views/SearchEmployeeView'
import AuthView from '@/views/AuthView'
import DeparmentView from '@/views/DepartmentView'
import ProfitView from '@/views/ProfitView'
import SalaryView from '@/views/SalaryView'
import PersonalSalaryView from '@/views/PersonalSalaryView'
import BookSaleView from '@/views/BookSaleView'


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
        component: SearchEmployeeView,
        meta: {
          requireAuth: true
        }
      },
      {
        path: 'authority',
        name: 'authority',
        component: AuthView,
        meta: {
          requireAuth: true
        },
      },
      {
        path: 'department',
        name: 'department',
        component: DeparmentView,
        meta: {
          requireAuth: true
        }
      },
      {
        path: 'bookSale',
        name: 'bookSale',
        component: BookSaleView,
        meta: {
          requireAuth: true
        }
      },
      {
        path: 'salary',
        name: 'salary',
        component: SalaryView,
        meta: {
          requireAuth: true
        }
      },
      {
        path: 'personalSalary',
        name: 'personalSalary',
        component: PersonalSalaryView,
        meta: {
          requireAuth: true
        }
      },
    ]
  },

]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})
// ???????????????????????????????????????????????????token?????????????????????????????????????????????
if (storage.get("token")) {
  store.commit("set_token", storage.get("token"));
}
router.beforeEach((to, from, next) => {
  // ??????????????????????????????requiresAuth
  // to.matched.some(r => r.meta.requireAuth) or to.meta.requiresAuth
  document.title = to.meta.title || '??????????????????'
  if (to.matched.some(r => r.meta.requireAuth)) {
    if (store.state.token) {
      next(); //???token,??????request???????????????????????????token
    } else {
      next({
        path: "/login",
        // ????????????????????????path?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        query: {
          redirect: to.fullPath }
      });
    }
  } else {
    next(); //????????????token,??????????????????
  }
});
export default router
