<script setup lang="ts">
import Snackbar from "@/components/common/Snackbar.vue";
</script>

<template>
  <div class="center-container">
    <router-view> </router-view>
    <Snackbar />
  </div>
</template>

<style scoped>
.center-container {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center;    /* 垂直居中 */
  height: 100vh;          /* 占满整个视口高度 */
  background-color: #f0f0f0; /* 可选背景色 */
}
</style>
