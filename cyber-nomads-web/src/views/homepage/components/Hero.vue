<template>
  <section class="section hero" id="home" aria-label="hero">
    <div class="container">

      <div class="hero-content">

        <h1 class="h1 hero-title">
          Building Digital <span class="has-before">Product</span>, Brand and Experience
        </h1>

        <p class="hero-text">
          At Pixology we specialize in designing, building, shipping and scaling beautiful, usable products with
          blazing-fast
          efficiency
        </p>

        <div class="wrapper">
          <a href="#" class="btn btn-primary has-before has-after">How It Works</a>

          <button class="hero-btn" aria-label="pixology promo">
            <ion-icon name="play-outline" aria-hidden="true"></ion-icon>

            <span class="span">Behind the scenes</span>
          </button>
        </div>

        <ul class="social-list">

          <li>
            <a href="#" class="social-link" style="--color: hsl(241, 77%, 63%);">
              <ion-icon name="logo-facebook"></ion-icon>

              <span class="span">Facebook</span>
            </a>
          </li>

          <li>
            <a href="#" class="social-link" style="--color: hsl(0, 100%, 50%);">
              <ion-icon name="logo-youtube"></ion-icon>

              <span class="span">Youtube</span>
            </a>
          </li>

          <li>
            <a href="#" class="social-link" style="--color: hsl(203, 89%, 53%);">
              <ion-icon name="logo-twitter"></ion-icon>

              <span class="span">Twitter</span>
            </a>
          </li>

        </ul>

      </div>

      <figure class="hero-banner">
        <img src="@/assets/images/homepage/hero-banner.png" width="794" height="637" alt="hero banner" class="w-100">
      </figure>

    </div>
  </section>
</template>

<style lang="scss" scoped>
@import url("@/styles/view/homepage/_hero.scss");
</style>

