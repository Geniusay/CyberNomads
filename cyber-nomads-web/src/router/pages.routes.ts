export default [
  {
    path: "/pages/page1",
    component: () => import("@/views/pages/DesignNav.vue"),
  },
  {
    path: "/pages/form",
    component: () => import("@/views/pages/Form.vue"),
  }
];
