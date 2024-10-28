export default [
  {
    path: "/landing",
    name: "landing-home",
    component: () =>
      import(
        /* webpackChunkName: "landing-home" */ "@/views/landing/HomePage.vue"
      ),
    meta: {
      requiresAuth: true,
      layout: "landing",
    },
  },
  {
    path: "/landing/pricing",
    name: "landing-pricing",
    component: () =>
      import(
        /* webpackChunkName: "landing-pricing" */ "@/views/landing/pricing/PricingPage.vue"
      ),
    meta: {
      requiresAuth: true,
      layout: "landing",
    },
  }
];
