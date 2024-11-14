<script setup lang="ts">
import {joinQQGroup} from "@/utils/toolUtils";

const icons = [{
  icon:"./assets/svg/qq.svg",
  handle: ()=>joinQQGroup()
}]

const usefulLinks = [
  { title: "Usefull Links", type: "title" },
  { label: "关于TML", href: "#" },
  { label: "联系我们", href: "#" },
  { label: "怎么使用？", href: "#" },
];

const communityLinks = [
  { title: "Community", type: "title" },
  { label: "帮助中心", href: "#" },
  { label: "博客", href: "#" },
];

const instaPosts = [
  { src: "https://geniusserve.oss-cn-shanghai.aliyuncs.com/cybernomads/taskImg/task1.png", href: "#" },
  { src: "https://geniusserve.oss-cn-shanghai.aliyuncs.com/cybernomads/taskImg/task2.png", href: "#" },
  { src: "https://geniusserve.oss-cn-shanghai.aliyuncs.com/cybernomads/taskImg/task3.png", href: "#" },
  { src: "https://geniusserve.oss-cn-shanghai.aliyuncs.com/cybernomads/taskImg/task4.png", href: "#" },
  { src: "https://geniusserve.oss-cn-shanghai.aliyuncs.com/cybernomads/taskImg/task5.png", href: "#" },
  { src: "https://geniusserve.oss-cn-shanghai.aliyuncs.com/cybernomads/taskImg/task6.png", href: "#" },
];

</script>

<template>
  <footer class="footer">
    <div class="container">

      <div class="footer-top section">

        <div class="footer-brand">

          <p class="footer-list-title">About CyberNomads</p>

          <p class="footer-text">
            A cloud service for creating AI accounts, endowing accounts with soul and data with life.
          </p>

          <ul class="social-list">

            <li v-for="icon in icons">
              <a @click="icon.handle()" class="social-link">
                <ion-icon :src="icon.icon"></ion-icon>
              </a>
            </li>
          </ul>

        </div>

        <ul class="footer-list">
          <li v-for="(link, index) in usefulLinks" :key="index">
            <p v-if="link.type === 'title'" class="footer-list-title">{{ link.title }}</p>
            <a v-else :href="link.href" class="footer-link">{{ link.label }}</a>
          </li>
        </ul>

        <ul class="footer-list">
          <li v-for="(link, index) in communityLinks" :key="'community-' + index">
            <p v-if="link.type === 'title'" class="footer-list-title">{{ link.title }}</p>
            <a v-else :href="link.href" class="footer-link">{{ link.label }}</a>
          </li>
        </ul>

        <ul class="footer-list">
        <li>
          <p class="footer-list-title">Task Post</p>
        </li>

        <li>
          <ul class="insta-post">
            <li v-for="(post, index) in instaPosts" :key="index">
              <div class="insta-card">
                <figure class="post-banner">
                  <img :src="post.src" width="77" height="63" loading="lazy" alt="instagram post" class="img-cover">
                </figure>
                <a :href="post.href" class="card-content">
                  <ion-icon name="logo-instagram" aria-hidden="true"></ion-icon>
                </a>
              </div>
            </li>
          </ul>
        </li>
      </ul>

      </div>

      <div class="footer-bottom">

        <p class="copyright">
          &copy; 2024 CyberNomads. All Rights Reserved by TML
        </p>

        <ul class="footer-bottom-list">

          <li>
            <a href="#" class="footer-bottom-link">Privacy policy</a>
          </li>

          <li>
            <router-link to="/login">
              <a class="footer-bottom-link">Login / Signup</a>
            </router-link>

          </li>

        </ul>

      </div>

    </div>
  </footer>
</template>

<style lang="scss" scoped>
@import url("@/styles/view/homepage/_footer.scss");
</style>
