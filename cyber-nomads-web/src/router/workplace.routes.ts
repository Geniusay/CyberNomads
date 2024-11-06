// users Data Page
export default [
  {
    path: "/data/cyber-nomads",
    component: () => import("@/views/workplace/robot/NomadsTablePage.vue"),
    meta: {
      requiresAuth: true,
      layout: "ui",
      category: "Apps",
      title: "NomadsTable",
    },
  },
  {
    path: "/data/task-list",
    component: () => import("@/views/workplace/task/TaskListPage.vue"),
    meta: {
      requiresAuth: true,
      layout: "landing",
    },
  }
];

