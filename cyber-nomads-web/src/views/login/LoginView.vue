<script setup lang="ts">
import {LoginForm, RegisterForm , defaultValue} from "@/views/login/LoginTypes";

import {
  sendPicCaptcha,
  emailLogin,
  emailRegister,
  sendCodeToEmail
} from "@/api/userApi"

const isLogin = ref(true);
const isPreVerify = ref(false)
const sendLoading = ref(false)

const loginForm = ref<LoginForm>({...defaultValue.defaultLoginForm})
const register = ref<RegisterForm>({...defaultValue.defaultRegisterForm})
const login = () => {
  console.log(loginForm.value)
}

const currentForm = () => {
  return isLogin.value?loginForm:register
}

const sendEmailCode = () => {

  if(isPreVerify.value){

  }else{

  }
}

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
                <input v-model="loginForm.email" type="email" placeholder="邮箱" />
              </p>
              <p class="password">
                <input v-model="loginForm.password" type="password" placeholder="密码" />
                <i class="ri-eye-off-line"></i>
              </p>
              <p class="code-container">
                <input v-model="loginForm.code" placeholder="验证码" class="input-code"/>
                <v-btn :loading="sendLoading" @click="sendEmailCode()" color="#5865f2" min-height="60" class="send-code">
                  发送验证码
                  <template v-slot:loader>
                    <v-progress-linear indeterminate></v-progress-linear>
                  </template>
                </v-btn>
              </p>
              <p>
                <input @click="login()" type="submit" class="submit" value="登录" />
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
@import url("@/styles/view/login/_login.scss");
</style>
