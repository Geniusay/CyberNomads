<template>
  <header class="header" data-header>
    <div class="container">
      <a href="#" class="logo">
        <img
          src="@/assets/svg/logo.svg"
          width="32"
          height="32"
          alt="Cryptex logo"
        />
        CyberNomads
      </a>

      <nav class="navbar" data-navbar>
        <ul class="navbar-list">
          <li class="navbar-item">
            <a href="#" class="navbar-link active" data-nav-link>首页</a>
          </li>

          <li class="navbar-item">
            <a href="#" class="navbar-link" data-nav-link>Buy Crypto</a>
          </li>

          <li class="navbar-item">
            <a href="#" class="navbar-link" data-nav-link>Markets</a>
          </li>

          <li class="navbar-item">
            <a href="#" class="navbar-link" data-nav-link>Sell Crypto</a>
          </li>

          <li class="navbar-item">
            <a href="#" class="navbar-link" data-nav-link>Blog</a>
          </li>

          <li class="navbar-item">
            <a href="#" class="navbar-link" data-nav-link>BITUSDT</a>
          </li>
        </ul>
      </nav>

      <button class="nav-toggle-btn" aria-label="Toggle menu" data-nav-toggler>
        <span class="line line-1"></span>
        <span class="line line-2"></span>
        <span class="line line-3"></span>
      </button>
      <router-link to="/login" class="logo">
        <a class="btn btn-outline">登录</a>
      </router-link>
    </div>
  </header>
</template>

<style scoped>
@import url("@/style/view/homepage/_header.scss");
</style>
