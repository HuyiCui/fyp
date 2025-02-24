import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

// Solve the problem of frequent menu click errors in vue-router in the navigation bar or bottom navigation tabBar in version 3.0 and above.
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  {
    path: '/',
    name: 'Manager',
    component: () => import('../views/Manager.vue'),
    children: [
      { path: '403', name: 'NoAuth', meta: { name: 'No permissions' }, component: () => import('../views/manager/403') },
      { path: 'home', name: 'Home', meta: { name: 'System Home' }, component: () => import('../views/manager/Home') },
      { path: 'admin', name: 'Admin', meta: { name: 'Administrator Information' }, component: () => import('../views/manager/Admin') },
      { path: 'business', name: 'Business', meta: { name: 'Merchant Information' }, component: () => import('../views/manager/Business') },
      { path: 'user', name: 'User', meta: { name: 'User Information' }, component: () => import('../views/manager/User') },
      { path: 'adminPerson', name: 'AdminPerson', meta: { name: 'Personal information' }, component: () => import('../views/manager/AdminPerson') },
      { path: 'businessPerson', name: 'BusinessPerson', meta: { name: 'Personal information' }, component: () => import('../views/manager/BusinessPerson') },
      { path: 'password', name: 'Password', meta: { name: 'Change Password' }, component: () => import('../views/manager/Password') },
      { path: 'notice', name: 'Notice', meta: { name: 'Announcement Information' }, component: () => import('../views/manager/Notice') },
      { path: 'type', name: 'Type', meta: { name: 'Classified Information' }, component: () => import('../views/manager/Type') },
      { path: 'goods', name: 'Goods', meta: { name: 'Products Information' }, component: () => import('../views/manager/Goods') },
    ]
  },
  {
    path: '/front',
    name: 'Front',
    component: () => import('../views/Front.vue'),
    children: [
      { path: 'home', name: 'Home', meta: { name: 'System Home' }, component: () => import('../views/front/Home') },
      { path: 'person', name: 'Person', meta: { name: 'Personal information' }, component: () => import('../views/front/Person') },
      { path: 'detail', name: 'Detail', meta: { name: 'Product Details' }, component: () => import('../views/front/Detail') },
      { path: 'type', name: 'Type', meta: { name: 'Product Types' }, component: () => import('../views/front/Type') },
      { path: 'business', name: 'Business', meta: { name: 'Merchant' }, component: () => import('../views/front/Business') },
      { path: 'collect', name: 'Collect', meta: { name: 'Favourite' }, component: () => import('../views/front/Collect') },
      { path: 'address', name: 'Address', meta: { name: 'My Address' }, component: () => import('../views/front/Address') },
      { path: 'cart', name: 'Cart', meta: { name: 'My Cart' }, component: () => import('../views/front/Cart') },
      { path: 'orders', name: 'Order', meta: { name: 'My Order' }, component: () => import('../views/front/Orders') },
      { path: 'search', name: 'Search', meta: { name: 'Search' }, component: () => import('../views/front/Search') },
    ]
  },
  { path: '/login', name: 'Login', meta: { name: 'Log in' }, component: () => import('../views/Login.vue') },
  { path: '/register', name: 'Register', meta: { name: 'Register' }, component: () => import('../views/Register.vue') },
  { path: '*', name: 'NotFound', meta: { name: 'No access' }, component: () => import('../views/404.vue') },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// Note: If you don't need the front-end project, you can comment out the route guard
// Route guard
// router.beforeEach((to ,from, next) => {
//   let user = JSON.parse(localStorage.getItem("xm-user") || '{}');
//   if (to.path === '/') {
//     if (user.role) {
//       if (user.role === 'USER') {
//         next('/front/home')
//       } else {
//         next('/home')
//       }
//     } else {
//       next('/login')
//     }
//   } else {
//     next()
//   }
// })

export default router
