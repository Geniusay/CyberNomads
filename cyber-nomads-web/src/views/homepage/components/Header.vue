<script setup lang="ts">
import {validAuth} from "@/utils/authUtil"
import {useRouter} from "vue-router";
const router = useRouter();
const letsGo = () =>{
  if (validAuth()) {
    return "/workplace"
  }else{
    return "/login"
  }
}

const navbarList = [
  {
    title:"Home",
    href:"#"
  },
  {
    title:"Service",
    href:"#"
  },
  {
    title:"Features",
    href:"#"
  },

]
</script>

<template>
  <header class="header" data-header>
    <div class="container">

      <a href="#" class="logo" data-logo>Cyber Nomads</a>

      <nav class="navbar" data-navbar>

        <div class="wrapper">
          <a href="#" class="logo">Cyber Nomads</a>

          <button id="flipButton"  class="nav-close-btn" aria-label="close menu" data-nav-toggler>
            <ion-icon name="close-outline" aria-hidden="true"></ion-icon>
          </button>
        </div>

        <ul class="navbar-list">

          <li class="navbar-item" v-for="navbar in navbarList">
            <a :href="navbar.href" class="navbar-link" data-nav-link>{{navbar.title}}</a>
          </li>
        </ul>
      </nav>

      <button class="nav-open-btn" aria-label="open menu" data-nav-toggler>
        <ion-icon name="menu-outline" aria-hidden="true"></ion-icon>
      </button>
      <router-link :to="letsGo()">
        <a href="#" class="btn btn-primary has-before has-after">Let’s Go 👋</a>
      </router-link>


      <div class="overlay" data-nav-toggler data-overlay></div>

    </div>
  </header>
</template>

<style lang="scss" scoped>
@import url("@/styles/view/homepage/_header.scss");
</style>
