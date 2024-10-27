import App from "./App.vue";
import { createApp } from "vue";

import i18n from "./plugins/i18n";
import vuetify from "./plugins/vuetify";
import piniaPersist from "pinia-plugin-persist";
import router from "./router";
import "vue3-lottie/dist/style.css";
import Vue3Lottie from "vue3-lottie";
const pinia = createPinia();
pinia.use(piniaPersist);

import "@/style/main.scss";
import "./style.css";
import Prism from "prismjs";

const app = createApp(App);
app.use(pinia);
app.use(i18n);
app.use(router);
app.use(Vue3Lottie, { name: "LottieAnimation" });
app.use(vuetify);

app.mount("#app");
