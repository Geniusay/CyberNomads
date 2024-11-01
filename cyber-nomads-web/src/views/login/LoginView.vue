<script setup lang="ts">
import {LoginForm, RegisterForm , PicCode, defaultValue} from "@/views/login/LoginTypes";
import {useSnackbarStore} from "@/stores/snackbarStore";
import { computed } from 'vue';
import {
  sendPicCaptcha,
  emailLogin,
  sendCodeToEmail
} from "@/api/userApi"

import {onMounted} from "vue";

const snackbarStore = useSnackbarStore()
const isLogin = ref(true);
const sendLoading = ref(false)

const loginForm = ref<LoginForm>({...defaultValue.defaultLoginForm})
const register = ref<RegisterForm>({...defaultValue.defaultRegisterForm})
const picCode = ref<PicCode>({...defaultValue.defaultPicCode})

onMounted(async ()=>{
  await generatePicCode()
})

const validateForm = () => {


  if (!currentForm().value.email) {
    errors.value = '邮箱是必填项';
  } else if (!/^[\w-]+@([\w-]+\.)+[\w-]{2,4}$/.test(currentForm().value.email)) {
    errors.value = '请输入有效的邮箱地址';
  }

  if (!currentForm().value.code) {
    errors.value = '验证码是必填项';
  } else if (currentForm().value.code.length < 4) {
    errors.value = '验证码至少包含4位字符';
  }

  if(!picCode.value.code){
    errors.value = "请输入图片验证码";
  }
  return errors;
};

const validate = ():boolean =>{
  if(errors.value!==""){
    snackbarStore.showErrorMessage(errors.value)
    return false
  }
  return true
}

const errors = computed(() => validateForm());

const generatePicCode = async () =>{
  await sendPicCaptcha().then(res=>{
    if (res.code==="200") {
      picCode.value.pid = res.data.pid
      picCode.value.img = res.data.base64
    }else{
      snackbarStore.showErrorMessage("网络异常")
    }
  })
}

const currentForm = () => {
  return isLogin.value?loginForm:register
}

const sendEmailCode = async () => {
  if(validate()){
    sendLoading.value = true
    await sendCodeToEmail(currentForm().value.email, picCode.value.pid, picCode.value.code).then(res=>{
      if (res.code === "200") {
        snackbarStore.showSuccessMessage("已发送邮箱验证码，请检查邮箱")
      }else{
        snackbarStore.showErrorMessage("图片验证码错误!")
        generatePicCode()
      }
    })
    sendLoading.value = false
  }
}

const login = async()=>{
  if (validate()) {
    await emailLogin(loginForm).then(res=>{
        if(res.code==="200"){
          snackbarStore.showSuccessMessage("欢迎回来!")
        }else{
          snackbarStore.showErrorMessage("登录失败，请检查邮箱或验证码是否正确!")
        }
    })
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
              <p class="code-container">
                <input v-model="picCode.code" placeholder="图片验证码" class="pic-code-input"/>
                <img @click="generatePicCode()" :src="`data:image/png;base64,${picCode.img}`" alt="Base64 Image" class="pic-code" >
                <template v-slot:placeholder>
              <div class="d-flex align-center justify-center fill-height">
                <v-progress-circular
                  color="grey-lighten-4"
                  indeterminate
                ></v-progress-circular>
              </div>
</template> </template>
                </img>
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
