<script setup lang="ts">
const images = [
  {
    url: "./assets/images/homepage/service-icon-1.png",
    name: "Product Management"
  },
  {
    url: "./assets/images/homepage/service-icon-2.png",
    name: "Product Management"
  },
  {
    url: "./assets/images/homepage/service-icon-3.png",
    name: "Product Management"
  },
  {
    url: "./assets/images/homepage/service-icon-4.png",
    name: "Product Management"
  },
  {
    url: "./assets/images/homepage/service-icon-5.png",
    name: "Product Management"
  },
  {
    url: "./assets/images/homepage/service-icon-6.png",
    name: "Product Management"
  },
  {
    url: "./assets/images/homepage/service-icon-7.png",
    name: "Product Management"
  }
]
</script>

<template>
  <section class="section service" id="service" aria-label="service">
    <div class="container">

      <p class="section-subtitle has-before text-center">Our Services</p>

      <h2 class="h2 section-title text-center">Managing you business with our <span class="has-before">best
              service</span></h2>

      <ul class="grid-list">

        <li v-for="image in images">
          <div class="service-card" style="--color: 174, 77%, 50%">

            <div class="card-icon">
              <img :src="image.url" width="30" height="30" loading="lazy"
                   alt="service icon">
            </div>

            <h3 class="h3">
              <a href="#" class="card-title">{{image.name}}</a>
            </h3>

          </div>
        </li>

        <li>
          <a href="#" class="link-card" style="--color: 203, 97%, 75%">
            <span class="span">04 More Service</span>

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
