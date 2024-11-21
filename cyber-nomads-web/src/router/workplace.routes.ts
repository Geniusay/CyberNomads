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
  },
  {
    path: "/data/task-square",
    component: () => import("@/views/workplace/modulesquare/ModuleSquarePage.vue"),
    meta: {
      requiresAuth: true,
      layout: "ui",
      category: "UI",
      title: "Task Square",
    },
  },
  {
    path: "/utility/help",
    name: "utility-help",
    component: () =>
      import(
        /* webpackChunkName: "utility-help" */ "@/views/utility/HelpPage.vue"
        ),
    meta: {
      requiresAuth: true,
      title: "Help",
      layout: "ui",
      category: "Utility",
    },
  },
];

