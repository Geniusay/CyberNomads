<script setup lang="ts">
import Header from "@/views/homepage/components/Header.vue";
import NewHeader from "@/views/homepage/components/NewHeader.vue";
import Hero from "@/views/homepage/components/Hero.vue";
import Service from "@/views/homepage/components/Service.vue";
import Project from "@/views/homepage/components/Project.vue";
import NewSletter from "@/views/homepage/components/NewSletter.vue";
import Footer from "@/views/homepage/components/Footer.vue";
import Feature from "@/views/homepage/components/Feature.vue";

onMounted(() => {
  const addEventOnElem = function (elem, type, callback) {
    if (elem.length > 1) {
      for (let i = 0; i < elem.length; i++) {
        elem[i].addEventListener(type, callback);
      }
    } else {
      elem.addEventListener(type, callback);
    }
  }

  /**
   * navbar toggle
   */

  const navbar = document.querySelector("[data-navbar]");
  const navTogglers = document.querySelectorAll("[data-nav-toggler]");
  const navbarLinks = document.querySelectorAll("[data-nav-link]");
  const overlay = document.querySelector("[data-overlay]");
  const logo = document.querySelector("[data-logo]");
  const toggleNavbar = function () {
    navbar.classList.toggle("active");
    overlay.classList.toggle("active");
  }

  addEventOnElem(navTogglers, "click", toggleNavbar);

  const closeNavbar = function () {
    navbar.classList.remove("active");
    overlay.classList.remove("active");
    logo.classList.remove("active")
  }

  addEventOnElem(navbarLinks, "click", closeNavbar);

  /**
   * header & back top btn show when scroll down to 100px
   */

  const header = document.querySelector("[data-header]");

  const headerActive = function () {
    if (window.scrollY > 80) {
      header.classList.add("active");
      logo.classList.add("active")
    } else {
      header.classList.remove("active");
      logo.classList.remove("active")
    }
  }

  addEventOnElem(window, "scroll", headerActive);
});
</script>

<template>
  <div id="top" class="home-container">
    <Header></Header>
    <main>
      <article>
        <NewHeader />
        <Service />
        <Feature/>
        <Project />
        <NewSletter />
        <Footer />
      </article>
    </main>

  </div>
</template>

<style lang="scss" scoped>
@import url("@/styles/view/homepage/main.scss");
</style>
