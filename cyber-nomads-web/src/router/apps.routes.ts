import todoRoutes from "@/views/app/todo/todoRoutes";

export default [
  {
    path: "/apps/todo",
    meta: {
      requiresAuth: true,
      layout: "ui",
      category: "APP",
      title: "Todo",
    },
    component: () => import("@/views/app/todo/TodoApp.vue"),
    children: [...todoRoutes],
  },
];
