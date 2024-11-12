<script setup lang="ts">
const blogs = [
  {
    url:"./assets/images/homepage/blog-1.jpg",
    tag:"Development",
    date:"July 22, 2022",
    width:644,
    height:363,
    title:"Godaddy user flow solution...",
    content:" At Pixology we specialize in designing, building, shipping and scaling beautifu. At Pixology we\n" +
      "                specialize in designing,\n" +
      "                building, shipping and scaling beautiful."
  },
  {
    url:"./assets/images/homepage/blog-2.jpg",
    tag:"Development",
    date:"July 22, 2022",
    width:202,
    height:162,
    title:"Godaddy user flow solution...",
  },
  {
    url:"./assets/images/homepage/blog-3.jpg",
    tag:"Development",
    date:"July 22, 2022",
    width:202,
    height:162,
    title:"Godaddy user flow solution...",
  },
  {
    url:"./assets/images/homepage/blog-4.jpg",
    tag:"Development",
    date:"July 22, 2022",
    width:202,
    height:162,
    title:"Godaddy user flow solution...",
  },
]
</script>

<template>
  <section class="section blog" id="blog" aria-label="blog">
    <div class="container">

      <p class="section-subtitle text-center has-before">Blog Post</p>

      <h2 class="h2 section-title text-center">
        Popular <span class="has-before">blog post</span>
      </h2>

      <ul class="blog-list">

        <li>
          <div class="blog-card large">

            <figure class="card-banner">
              <img src="./assets/images/blog-1.jpg" width="644" height="363" loading="lazy"
                   alt="Godaddy user flow solution..." class="img-cover">
            </figure>

            <div class="card-content">

              <div class="wrapper">
                <a href="#" class="tag">Development</a>

                <a href="#" class="publish-date">
                  <ion-icon name="time-outline" aria-hidden="true"></ion-icon>

                  <span class="span">July 22, 2022</span>
                </a>
              </div>

              <h3>
                <a href="#" class="card-title">Godaddy user flow solution...</a>
              </h3>

              <p class="card-text">
                At Pixology we specialize in designing, building, shipping and scaling beautifu. At Pixology we
                specialize in designing,
                building, shipping and scaling beautiful.
              </p>

            </div>

          </div>
        </li>

        <li>
          <div class="blog-card">

            <figure class="card-banner">
              <img src="./assets/images/blog-2.jpg" width="202" height="162" loading="lazy"
                   alt="Godaddy user flow solution for every individual" class="img-cover">
            </figure>

            <div class="card-content">

              <div class="wrapper">
                <a href="#" class="tag">Development</a>

                <a href="#" class="publish-date">
                  <ion-icon name="time-outline" aria-hidden="true"></ion-icon>

                  <span class="span">July 21, 2020</span>
                </a>
              </div>

              <h3 class="h3">
                <a href="#" class="card-title">Godaddy user flow solution for every individual</a>
              </h3>

            </div>

          </div>
        </li>

        <li>
          <div class="blog-card">

            <figure class="card-banner">
              <img src="./assets/images/blog-3.png" width="202" height="162" loading="lazy"
                   alt="Business solution for every individual" class="img-cover">
            </figure>

            <div class="card-content">

              <div class="wrapper">
                <a href="#" class="tag">Development</a>

                <a href="#" class="publish-date">
                  <ion-icon name="time-outline" aria-hidden="true"></ion-icon>

                  <span class="span">July 21, 2020</span>
                </a>
              </div>

              <h3 class="h3">
                <a href="#" class="card-title">Business solution for every individual</a>
              </h3>

            </div>

          </div>
        </li>

        <li>
          <div class="blog-card">

            <figure class="card-banner">
              <img src="./assets/images/blog-4.png" width="202" height="162" loading="lazy"
                   alt="How to gain pro experience ar figma update version" class="img-cover">
            </figure>

            <div class="card-content">

              <div class="wrapper">
                <a href="#" class="tag">Development</a>

                <a href="#" class="publish-date">
                  <ion-icon name="time-outline" aria-hidden="true"></ion-icon>

                  <span class="span">July 21, 2020</span>
                </a>
              </div>

              <h3 class="h3">
                <a href="#" class="card-title">How to gain pro experience ar figma update version</a>
              </h3>

            </div>

          </div>
        </li>

      </ul>

    </div>
  </section>
</template>
