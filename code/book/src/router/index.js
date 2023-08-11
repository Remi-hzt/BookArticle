import Vue from 'vue'
import VueRouter from 'vue-router'
import index from '../views/index'
import userindex from '../views/UserView/index'

Vue.use(VueRouter)
// 3.配置路由规则
const router = new VueRouter({
  mode: 'history', // 将 mode 设置为 'history'

  routes: [
    {
      name: "主页重定向",
      path: "/",
      redirect: "/login"
    },
    {
      path: '/login',
      name: "login",
      component: ()=>import('@/views/login/index'),
      hidden: true
    },
    {
      path: '/register',
      name: "register",
      component: ()=>import('@/views/login/register'),
      hidden: true
    },
    {
      path: '/forgetpwd',
      name: "forgetpwd",
      component: ()=>import('@/views/login/forgetpwd'),
      hidden: true
    },
    {
      name: "index",
      path: "/admin/index",
      component: index,
      props: true, // 开启将参数作为props的功能
     // meta: { requiresAuth: true, roles: ['ROLE_ADMIN'] }, // 配置元信息：需要认证，角色为 admin
      children:[
        {
          name: "Home",
          path: '/admin/Home',
          component: ()=>import('@/views/Home'),
        },
        {
            name: "novellist",
            path: '/admin/novellist',
            component: ()=>import('@/views/novel/novellist'),
        },
        {
            name: "addnovel",
            path: '/admin/addnovel',
            component: ()=>import('@/views/novel/addnovel'),
        },   
        {
          name: "booklist",
          path: '/admin/booklist',
          component: ()=>import('@/views/book/booklist'),
      },
      {
          name: "addbook",
          path: '/admin/addbook',
          component: ()=>import('@/views/book/addbook'),
      },  
        {
          name: "userlist",
          path: '/admin/userlist',
          component: ()=>import('@/views/user/userlist'),
        },
        {
            name: "adduser",
            path: '/admin/adduser',
            component: ()=>import('@/views/user/adduser'),
        }, 
        {
            name: "message",
            path: '/admin/message',
            component: ()=>import('@/views/user/message'),
        }, 
      ]
    },
    {
      name: "userindex",
      path: "/user/index",
      component: userindex,
      props: true, // 开启将参数作为props的功能
      children:[
        {
          name: "userhome",
          path: '/user/home',
          component: ()=>import('@/views/UserView/home.vue'),
        },
        {
          name: "addmynovel",
          path: '/user/addmynovel',
          component: ()=>import('@/views/UserView/novel/addmynovel.vue'),
        },
        {
          name: "novel",
          path: '/user/novel',
          component: ()=>import('@/views/UserView/novel/novel.vue'),
        },
        {
          name: "book",
          path: '/user/book',
          component: ()=>import('@/views/UserView/book.vue'),
        },
      ]
    },
  ]
});
// 路由守卫
router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('jwt') // 判断用户是否已经登录，可以通过本地存储或者Cookie来保存token
  if (to.path !== '/login' && to.path !== '/register' && to.path !== '/forgetpwd' && !isAuthenticated) { // 如果用户未登录并且访问的不是登录页面，就重定向到登录页面
    next('/login')
  } else {
    next()
  }
})
// 4.导出路由对象
export default router
