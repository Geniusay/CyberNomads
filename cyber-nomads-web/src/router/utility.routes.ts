export default [
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
