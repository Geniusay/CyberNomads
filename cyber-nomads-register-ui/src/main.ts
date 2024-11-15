import { createApp } from 'vue'
import './style.css'
import App from './App.vue'

import vuetify from "./plugins/vuetify";
import piniaPersist from "pinia-plugin-persist";
import router from "./router";
import "vue3-lottie/dist/style.css";
import Vue3Lottie from "vue3-lottie";

import "@/style/main.scss";
import "./style.css";

const pinia = createPinia();
pinia.use(piniaPersist);
const app = createApp(App);
app.use(pinia);
app.use(router);
app.use(Vue3Lottie, { name: "LottieAnimation" });
app.use(vuetify);

app.mount("#app");
