<script setup lang="ts">
const isLogin = ref(true);
</script>

<template>
  <div class="login-container">
    <div id="page" class="site">
      <div class="container">
        <div class="login">
          <div class="hero">
            <h1>你好 世界<br />Cyber Nomads</h1>
            <p>如果你没有账号<br />可以<a href="#">点击这里</a>进行注册.</p>
          </div>
          <div class="main">
            <form action="">
              <p>
                <input type="email" placeholder="邮箱" />
              </p>
              <p class="password">
                <input type="password" placeholder="密码" />
                <i class="ri-eye-off-line"></i>
              </p>
              <p>
                <input type="submit" class="submit" value="登录" />
              </p>
            </form>
            <div class="options">
              <div class="separator">
                <p>使用其他方式登录</p>
              </div>
              <ul>
                <li>
                  <a href="#"><i class="ri-steam-fill ri-2x"></i></a>
                </li>
                <li>
                  <a href="#"><i class="ri-playstation-fill ri-2x"></i></a>
                </li>
                <li>
                  <a href="#"><i class="ri-xbox-fill ri-2x"></i></a>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
@import url("@/style/view/login/_login.scss");
</style>
