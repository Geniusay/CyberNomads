import { createRouter, createWebHashHistory } from "vue-router";

export const routes = [
  {
    path: "/",
    redirect: "/step",
    meta: {},
  } as any,
  {
    path: "/step",
    name: "step",
    component: () => import("@/views/step/UseStepView.vue"),
  } as any,
];

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
