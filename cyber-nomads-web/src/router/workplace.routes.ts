// users Data Page
export default [
  {
    path: "/data/cyber-nomads",
    component: () => import("@/views/workplace/robot/NomadsTablePage.vue"),
    meta: {
      requiresAuth: true,
      layout: "ui",
      category: "Data",
      title: "NomadsTable",
    },
  }
];
