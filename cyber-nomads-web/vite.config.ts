import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import AutoImport from "unplugin-auto-import/vite";
import vuetify from "vite-plugin-vuetify";
import prismjs from "vite-plugin-prismjs";
import { fileURLToPath, URL } from "node:url";

// https://vite.dev/config/
export default defineConfig({
  plugins: [
      vue(),
      vuetify({
        autoImport: true,
      }),
      AutoImport({
        imports: ["vue", "vue-router", "pinia"],
      }),
      prismjs({
        languages: "all",
      }),
  ],
  resolve: {
    alias: {
      "~": fileURLToPath(new URL("./", import.meta.url)),
      "@": fileURLToPath(new URL("./src", import.meta.url)),
      "@data": fileURLToPath(new URL("./src/data", import.meta.url)),
    },
    extensions: [".js", ".json", ".jsx", ".mjs", ".ts", ".tsx", ".vue"],
  },
  server: {
    port: 9200,
    proxy: {
      "/appApi": {
        target: "http://119.3.234.15:9000",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/appApi/, ""),
      },
    },
  },
  css: {
    preprocessorOptions: {
      scss: { charset: false },
      css: { charset: false },
    },
  },
  cacheDir: ".vite_cache",
})
