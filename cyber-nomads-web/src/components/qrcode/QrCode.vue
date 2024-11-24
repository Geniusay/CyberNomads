<template>
  <div id="qrcodeImage"></div>
  <v-btn @click="openUrl">
    获取二维码
  </v-btn>
  <v-text-field v-model="url"></v-text-field>
  <v-btn @click="generate">
    生成二维码
  </v-btn>
</template>

<script setup lang="ts">
import QRCode from "qrcode"

const url = ref("")
const openUrl = ()=>{
  window.open("https://passport.bilibili.com/x/passport-login/web/qrcode/generate?source=main-fe-header", "_blank");
}
const generate=()=>{
  const div = document.getElementById("qrcodeImage");
  QRCode.toDataURL(url.value, {
    width: 200, // 图片宽度
    margin: 2 // 边距
  }, (error, url) => {
    if (error) console.error(error);
    const img = document.createElement("img");
    img.src = url; // 将生成的 Base64 图像插入
    div.appendChild(img);
  });
}
</script>
