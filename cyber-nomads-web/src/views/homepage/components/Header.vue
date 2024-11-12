<template>
  <header class="header" data-header>
    <div class="container">

      <a href="#" class="logo">Pixology</a>

      <nav class="navbar" data-navbar>

        <div class="wrapper">
          <a href="#" class="logo">Pixology</a>

          <button class="nav-close-btn" aria-label="close menu" data-nav-toggler>
            <ion-icon name="close-outline" aria-hidden="true"></ion-icon>
          </button>
        </div>

        <ul class="navbar-list">

          <li class="navbar-item">
            <a href="#home" class="navbar-link" data-nav-link>Home</a>
          </li>

          <li class="navbar-item">
            <a href="#service" class="navbar-link" data-nav-link>Services</a>
          </li>

          <li class="navbar-item">
            <a href="#feature" class="navbar-link" data-nav-link>Features</a>
          </li>

          <li class="navbar-item">
            <a href="#project" class="navbar-link" data-nav-link>Portfolio</a>
          </li>

          <li class="navbar-item">
            <a href="#blog" class="navbar-link" data-nav-link>Blog</a>
          </li>

        </ul>

      </nav>

      <button class="nav-open-btn" aria-label="open menu" data-nav-toggler>
        <ion-icon name="menu-outline" aria-hidden="true"></ion-icon>
      </button>

      <a href="#" class="btn btn-primary has-before has-after">Let’s Talk 👋</a>

      <div class="overlay" data-nav-toggler data-overlay></div>

    </div>
  </header>
</template>

<style lang="scss" scoped>
@import url("@/styles/view/homepage/_header.scss");
</style>
