<script setup lang="ts">
  const icons = [
    "logo-youtube","logo-twitter","logo-facebook","logo-skype"
  ]

  const usefulLinks = [
    { title: "Usefull Links", type: "title" },
    { label: "Contact us", href: "#" },
    { label: "How it Works", href: "#" },
    { label: "Create", href: "#" },
    { label: "Explore", href: "#" },
    { label: "Terms & Services", href: "#" }
  ];

  const communityLinks = [
    { title: "Community", type: "title" },
    { label: "Help Center", href: "#" },
    { label: "Partners", href: "#" },
    { label: "Suggestions", href: "#" },
    { label: "Blog", href: "#" },
    { label: "Newsletters", href: "#" }
  ];

  const instaPosts = [
    { src: "./assets/images/homepage/insta-post-1.jpg", href: "#" },
    { src: "./assets/images/homepage/insta-post-2.jpg", href: "#" },
    { src: "./assets/images/homepage/insta-post-3.jpg", href: "#" },
    { src: "./assets/images/homepage/insta-post-4.jpg", href: "#" },
    { src: "./assets/images/homepage/insta-post-5.jpg", href: "#" },
    { src: "./assets/images/homepage/insta-post-6.jpg", href: "#" }
  ];

</script>

<template>
  <footer class="footer">
    <div class="container">

      <div class="footer-top section">

        <div class="footer-brand">

          <p class="footer-list-title">About Pixology</p>

          <p class="footer-text">
            A new way to make the payments easy, reliable and 100% secure. claritatem itamconse quat. Exerci tationulla
          </p>

          <ul class="social-list">

            <li v-for="icon in icons">
              <a href="#" class="social-link">
                <ion-icon :name="icon"></ion-icon>
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
          <p class="footer-list-title">Instagram Post</p>
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
          &copy; 2022 Pixology. All Rights Reserved by codewithsadee
        </p>

        <ul class="footer-bottom-list">

          <li>
            <a href="#" class="footer-bottom-link">Terms and conditions</a>
          </li>

          <li>
            <a href="#" class="footer-bottom-link">Privacy policy</a>
          </li>

          <li>
            <a href="#" class="footer-bottom-link">Login / Signup</a>
          </li>

        </ul>

      </div>

    </div>
  </footer>
</template>

<style lang="scss" scoped>
@import url("@/styles/view/homepage/_footer.scss");
</style>
