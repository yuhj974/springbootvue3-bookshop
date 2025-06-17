import { createRouter, createWebHistory } from 'vue-router'
import { jwtDecode } from 'jwt-decode'
import { ElMessage } from 'element-plus'
import AdminHome from '@/views/admin/AdminHome.vue'
import Login from '@/views/Login.vue'
import Adminbook from '../views/admin/Adminbook.vue'
import Admincategory from '../views/admin/Admincategory.vue'
import Adminuser from '../views/admin/Adminuser.vue'
import Adminadmin from '../views/admin/Adminadmin.vue'
import Adminreview from '../views/admin/Adminreview.vue'
import Adminaddress from '../views/admin/Adminaddress.vue'
import Admincart from '../views/admin/Admincart.vue'
import Adminorder from '../views/admin/Adminorder.vue'
import Adminindex from '../views/admin/Adminindex.vue'
import Userhome from '../views/user/Userhome.vue'
import Cart from '../views/user/Cart.vue'
import Detail from '../views/user/UserDetail.vue'
import Profile from '../views/user/Profile.vue'
import Adminlog from '../views/admin/Adminlog.vue'
import Admincarousel from '../views/admin/Admincarousel.vue'
import Booklist  from '../views/user/Booklist.vue'

const routes = [

  {
    path: '/',
    redirect: '/user/home'
  },

  {
    path: '/admin',
    component: AdminHome,
    redirect: '/admin/adminindex',
    meta: { 
    requiresAuth: true, 
    allowedRoles: ['ADMIN', 'SUPERADMIN'] 
},
    children: [
      {path: 'adminbook', name: 'adminbook', component: Adminbook},
      {path: 'admincategory', name: 'admincategory', component: Admincategory},
      {path: 'adminuser', name: 'adminuser', component: Adminuser},
      {path: 'adminadmin', name: 'adminadmin', component: Adminadmin},
      {path: 'adminreview', name: 'adminreview', component: Adminreview},
      {path: 'adminaddress', name: 'adminaddress', component: Adminaddress},
      {path: 'admincart', name: 'admincart', component: Admincart},
      {path: 'adminorder', name: 'adminorder', component: Adminorder},
      {path: 'adminindex', name: 'adminindex', component: Adminindex},
      {path: 'adminlog', name: 'adminlog', component: Adminlog},
      {path: 'admincarousel', name: 'admincarousel', component: Admincarousel},
    ]
  },
  {
    path: '/login',
    name: 'login',
    component: Login
  },
  
  {
    path: '/user/home',
    name: 'userhome',
    component: Userhome,
   // meta: { requiresAuth: true, allowedRoles: ['USER'] }
   meta: { requiresAuth: false }
    
  },
  {
    path: '/user/cart',
    name: 'cart',
    component: Cart,
    meta: { requiresAuth: true, allowedRoles: ['USER'] }
  },
  {
    path: '/detail/:id',
    name: 'detail',
    component: Detail,
     meta: { requiresAuth: false } 

  },
  {
    path:'/profile',
    name:'profile',
    component:Profile,
     meta: { requiresAuth: true } // 添加认证要求
  },
  {
    path: '/booklist',
    name: 'booklist',
    component: Booklist,
    meta: { requiresAuth: false }
  },
  
  
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  const publicPaths = ['/login','/user/home', '/detail']
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  const allowedRoles = to.matched
    .map(record => record.meta.allowedRoles || [])
    .flat()

  // 不需要认证的页面直接放行
  if (!requiresAuth) {
    next()
    return
  }

  // 检查登录状态
  const tokenInfo = JSON.parse(localStorage.getItem('logintoken'))
  if (!tokenInfo || !tokenInfo.token) {
    ElMessage.error('请先登录')
    next('/Login')
    return
  }

  // 解析令牌获取角色
  let userRole = ''
  try {
    const decoded = jwtDecode(tokenInfo.token)
    userRole = decoded.role.toUpperCase() // 确保角色大写
  } catch (error) {
    localStorage.removeItem('logintoken')
    ElMessage.error('登录凭证无效')
    next('/login')
    return
  }

  // 检查角色权限
  if (allowedRoles.length > 0 && !allowedRoles.includes(userRole)) {
    ElMessage.error('没有访问权限')
    userRole === 'ADMIN'||userRole === 'SUPERADMIN' ? next('/admin/adminindex') : next('/user/home')
    return
  }

  next()
})

export default router