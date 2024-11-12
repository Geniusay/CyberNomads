<template>
  <section class="section newsletter has-bg-image" aria-label="newsletter"
           style="background-image: url('./assets/images/homepage/newsletter-bg.jpg')">
    <div class="container">

      <figure class="newsletter-banner">
        <img src="@/assets/images/homepage/newsletter-banner.png" width="355" height="356" loading="lazy"
             alt="newsletter banner" class="w-100">
      </figure>

      <div class="newsletter-content">

        <p class="section-subtitle has-before">Get every update</p>

        <h2 class="h2 section-title">Subscribe newslater get latest updates and deals</h2>

        <form action="" class="newsletter-form">
          <input type="email" name="email_address" placeholder="Enter your mail" required class="email-field">

          <button type="submit" class="btn btn-secondary has-before has-after">
            <span class="span">Subscribe</span>

            <ion-icon name="arrow-forward" aria-hidden="true"></ion-icon>
          </button>
        </form>

      </div>

    </div>
  </section>
</template>

<style lang="scss" scoped>
@import url("@/styles/view/homepage/_newsletter.scss");
</style>
