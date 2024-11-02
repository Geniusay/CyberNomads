import { createRouter, createWebHashHistory } from "vue-router";
import UserRoutes from "./user.routes";
import AuthRoutes from "./auth.routes";
import LandingRoutes from "./landing.routes";
import UtilityRoutes from "./utility.routes";
import AppsRoutes from "./apps.routes";
import DataRoutes from "./data.routes";
import {useSnackbarStore} from "@/stores/snackbarStore";

export const routes = [
  {
    path: "/",
    redirect: "/home",
    meta: {},
  } as any,
  {
    path: "/home",
    component: () => import("@/views/homepage/HomePageView.vue"),
  },
  {
    path:"/login",
    component: ()=>import("@/views/login/LoginView.vue")
  },
  {
    path: "/workplace",
    component: () => import("@/views/Main.vue"),
    redirect: "/workplace/dashboard",
    meta: {},
    children:[
      {
        path: "dashboard",
        meta: {
          requiresAuth: true,
          layout: "landing",
        },
        component: () => import("@/views/pages/DashBoard.vue"),
      },
      {
        path: "/:pathMatch(.*)*",
        name: "error",
        component: () =>
          import(/* webpackChunkName: "error" */ "@/views/errors/NotFoundPage.vue"),
      },
      ...UserRoutes,
      ...LandingRoutes,
      ...AuthRoutes,
      ...UtilityRoutes,
      ...AppsRoutes,
      ...DataRoutes,
    ]
  },
];

// 动态路由，基于用户权限动态去加载
export const dynamicRoutes = [];

const router = createRouter({
  history: createWebHashHistory(),
  // hash模式：createWebHashHistory，history模式：createWebHistory
  // process.env.NODE_ENV === "production"

  routes: routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition;
    } else {
      return { top: 0 };
    }
  },
});

// 设置全局前置守卫
router.beforeEach((to, from, next) => {
  const userInfo = JSON.parse(localStorage.getItem('cyberUser') || '{}');
  const loggedIn = userInfo && Object.keys(userInfo).length
  if(!loggedIn){
    if (to.path.startsWith('/home') || to.path === '/login') {
      next();
    } else {
      useSnackbarStore().showErrorMessage("暂未登录，请先登录!")
      next('/login');
    }
  }else {
    if (to.path === '/login') {
      useSnackbarStore().showErrorMessage("已登录，欢迎回来!")
      next('/workplace'); // 跳转到工作界面
    } else {
      next(); // 否则正常导航
    }
  }
});

export default router;
