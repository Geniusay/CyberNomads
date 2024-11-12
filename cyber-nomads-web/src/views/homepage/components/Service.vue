<script setup lang="ts">
const images = [
  {
    url: "./assets/images/homepage/service-icon-1.png",
    title: "赛博异世界",
    content:"Bilibili，Douyin，Weibo，Youtube，超多赛博异世界等你探索。"
  },
  {
    url: "./assets/images/homepage/service-icon-2.png",
    title: "赛博游民",
    content:"打造自己的AI账号。账号不再是冰冷的数据，他可以是猫娘，是史莱姆，是苦力怕，是一切你想要的模样。"
  },
  {
    url: "./assets/images/homepage/service-icon-3.png",
    title: "任务系统",
    content:"评论，点赞，关注，发视频 超多任务供你选择。支持任务组装，构建你想要的任务。"
  },
  {
    url: "./assets/images/homepage/service-icon-4.png",
    title: "快速升级",
    content:"一键点赞，一键关注，账号数据升级从未如此简单。"
  },
  {
    url: "./assets/images/homepage/service-icon-5.png",
    title: "兴趣系统",
    content:"在庞大的赛博世界中不再盲目探索，为你的找到和你任务兴趣相匹配的内容，精准执行任务，效率UP！UP！。"
  },
  {
    url: "./assets/images/homepage/service-icon-6.png",
    title: "职业系统",
    content:"账号也能分配“职业”，在赛博世界中各司其职。自动领取相关任务，打造专属账号方向。"
  },
  {
    url: "./assets/images/homepage/service-icon-7.png",
    title: "AI Agent",
    content:"让账号拥有意识，能自主的在平台中生存，学习，沟通，发现，思考。"
  }
]
</script>

<template>
  <section class="section service" id="service" aria-label="service">
    <div class="container">

      <p class="section-subtitle has-before text-center">我们的服务</p>

      <h2 class="h2 section-title text-center">超多功能，让你的账号<span class="has-before">活过来</span></h2>

      <ul class="grid-list">

        <li v-for="image in images">
          <div class="service-card" style="--color: 174, 77%, 50%">

            <div class="card-icon">
              <img :src="image.url" width="30" height="30" loading="lazy"
                   alt="service icon">
            </div>

            <h3 class="h3">
              <a href="#" class="card-title">{{image.title}}</a>
              <p class="card-text">
                {{image.content}}
              </p>
            </h3>

          </div>
        </li>

        <li>
          <a href="#" class="link-card" style="--color: 203, 97%, 75%">
            <span class="span">Coming soon</span>

            <ion-icon name="arrow-forward" aria-hidden="true"></ion-icon>
          </a>
        </li>
      </ul>

    </div>
  </section>
</template>

<style lang="scss" scoped>
@import url("@/styles/view/homepage/_service.scss");
</style>
