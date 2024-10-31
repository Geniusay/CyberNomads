import { createRouter, createWebHashHistory } from "vue-router";
import UserRoutes from "./user.routes";
import AuthRoutes from "./auth.routes";
import LandingRoutes from "./landing.routes";
import UtilityRoutes from "./utility.routes";
import AppsRoutes from "./apps.routes";
import DataRoutes from "./data.routes";

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

export default router;
