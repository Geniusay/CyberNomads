<script setup lang="ts">
const projectCards = [
  {
    url:"./assets/images/homepage/project-1.jpg",
    title:"Book art design",
    tag:"Branding",
    width:835
  },
  {
    url:"./assets/images/homepage/project-2.jpg",
    title:"Book art design",
    tag:"Branding",
    width:416
  },
  {
    url:"./assets/images/homepage/project-3.jpg",
    title:"Book art design",
    tag:"Branding",
    width:416
  },
  {
    url:"./assets/images/homepage/project-4.jpg",
    title:"Book art design",
    tag:"Branding",
    width:416
  },
  {
    url:"./assets/images/homepage/project-5.jpg",
    title:"Book art design",
    tag:"Branding",
    width:416
  }
]
</script>

<template>
  <section class="section project" id="project" aria-label="project">
    <div class="container">

      <p class="section-subtitle has-before text-center">Projects</p>

      <h2 class="h2 section-title text-center">
        Pixology complete <span class="has-before">project</span>
      </h2>

      <ul class="filter-list">

        <li>
          <button class="filter-btn active" data-filter-btn>Website</button>
        </li>
      </ul>

      <ul class="grid-list">

        <li v-for="card in projectCards">
          <div class="project-card">

            <figure class="card-banner img-holder" style="--width: 835;">
              <img :src="card.url" :width="card.width" height="429" loading="lazy" alt="Book art design"
                   class="img-cover">
            </figure>

            <div class="card-content">
              <h3 class="h3">
                <a href="#" class="card-title">{{card.title}}</a>
              </h3>

              <a href="#" class="card-tag">{{card.tag}}</a>
            </div>

          </div>
        </li>

      </ul>
    </div>
  </section>
</template>

<style lang="scss" scoped>
@import url("@/styles/view/homepage/_project.scss");
</style>
