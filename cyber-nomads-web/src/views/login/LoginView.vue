<script setup lang="ts">
import CircleLoading from "@/components/loading/CircleLoading.vue"
import {LoginForm, RegisterForm , PicCode, defaultValue} from "@/views/login/LoginTypes";
import {UserVO} from "@/types/userType";
import {useSnackbarStore} from "@/stores/snackbarStore";
import {useUserStore} from "@/stores/userStore";
import {Validators, validateAndReturn} from "@/utils/validate";
import {
  sendPicCaptcha,
  emailLogin,
  sendCodeToEmail, emailRegister
} from "@/api/userApi"
import { useRouter } from "vue-router";
import {onMounted} from "vue";
import coolDownTimer from "@/utils/emailCoolDownUtils";

const router = useRouter()
const snackbarStore = useSnackbarStore()
const userStore = useUserStore()
const isLogin = ref(true);
const sendLoading = ref(false)
const submitLoading = ref(false)

const loginForm = ref<LoginForm>({...defaultValue.defaultLoginForm})
const registerForm = ref<RegisterForm>({...defaultValue.defaultRegisterForm})
const needOpenPicCode = ref(false)

const loginValidators: Validators<LoginForm> = {
  email: (value) => value && /^[\w-]+@([\w-]+\.)+[\w-]{2,4}$/.test(value) ? null : '请输入正确的邮箱',
  code: (value) => value.length >= 6 ? null : '请输入正确的邮箱验证码',
  password: (value) => value ? null : '请输入正确的密码'
};

const registerValidators: Validators<RegisterForm> = {
  email: (value) => value && /^[\w-]+@([\w-]+\.)+[\w-]{2,4}$/.test(value) ? null : '请输入正确的邮箱',
  code: (value) => value.length >= 6 ? null : '请输入正确的邮箱验证码',
  password: (value) => value ? null : '请输入正确的密码',
  confirmPassword: (value) => value&&value===registerForm.value.password ? null : '确认密码和原密码不一致'
};


const picCode = ref<PicCode>({...defaultValue.defaultPicCode})

onMounted( ()=>{
  coolDownTimer.loadCoolDownTimer()
})

const otherLogin = [
  {
    logo:"./assets/svg/qq.svg",
    handler:()=>{snackbarStore.showInfoMessage('QQ登录正在开发中...')}
  },
  {
    logo:"./assets/svg/wechat.svg",
    handler:()=>{snackbarStore.showInfoMessage('微信登录正在开发中...')}
  },
  {
    logo:"./assets/svg/discord.svg",
    handler:()=>{snackbarStore.showInfoMessage('Discord登录正在开发中...')}
  },
]

const errorPicImg = "/assets/img/error1.png"
const preImg = ref("data:image/png;base64, iVBORw0KGgoAAAANSUhEUgAAALQAAAAoCAIAAADYC0ddAAAE2UlEQVR4Xu2VzauVVRTGbaA0SSNoUHJARNCBiBkZQnS9cALp4w8QihxUBH04cBCUkyBvE0GE60AHanJHd3QbZFyLICGyyYXoA0n6BPEjsIGzBuGSHZvNetZae+193/Oe9+W+8EPuWet5nr3O4QHXjT56YGBAZB2OBhpn+91bBM4NZq7uInDeJr0sx9zhTwmcd5ahHO3Ri3L8t7JM4LxH9LIcvWAoRxt8vekogfO+MH7ibAS3Xaa9cqS/UdGPFcphe42VweKXp4kKYxFavjbXSPUVFtxmaaYcG17cSOA8hX03/8XoEr32VoSVAwWNoF2lzTVKv2CRWGTK5fDcjRbRaG+RUr2TUy8sEelEe0WbExf3vk5oYtESeHb7dwRaUOmhmXJkwe/mvBv1hsujKVWWwsphvGKsVl8OjzjLFMqBHw3Yl7RdHo1f1gjGQ8bKUFZYcJsyu/IOgfNRRTm+efQMgXMbdqvz9ApXU5osj+1/nsA5w3grXeFWVLK/NfzJo3bKMbf5JwLnAXar53T8hnWuUoGTvpQDt4aYuYrLoTHRcmgTEUOWrnDr1/iVhoDZ31j/CmEkiB9FjEdFDePD55YJ+qOxchDvvvkHET++dm0PMZJuZddgFG5xoqGFa3OnvUJWJGiqHNlHmQbpbjmOLVwicIsTDTFcHGpkxVkBynB7Z9czhCFIE4yJaNl54WUCt2mCkRNWTZZDQzxFHI6gHGG47cR9RL1IGo6gHjH0/jRDdvvX94lmy/Hb7M9E2GrlSE8SQ1I6Vw7x+gbLgWIRzaXNRZjyxud/EmEVymFHiStxGEjLkQoeOn+SYHYxgTHxcqTXpAeJQ22uhRgwi9+oJYgTAxSn5UCNkZAd2mmxHKlATGD8X47FpYcJXK8e7RpxLg6NuUFqyRo3XPuesBPEiUGUfXzuPQIFqQbTSud2mlPA6EQ54gon9txGfEJELAcmONOYN5Rj918LhKbBwPOzIwLnmt6IEjWGLNLqfyvaPKw0pb0yqHOlpAmlaUxfVA76OIlyMJmtHDVYjqPfXiZwbpzCDnUq8QmNOhcje5uGx6JpxGFcbf5qHyHqRYumzLomWw77eTxRVBorgzpX4KUHZ4jwt32bhtMiysRhul1NOZhYJCobK4eI+KS4dcpwq1HnCsRyeM4TcVpEGU5EC04MlwgzYkj/yvHkng8IlNmuCvA2f5TTIspwIlpwolmypAlpVLfKgRoMaacceFtRmlOPsqxL09suJyxqmuVgAo+GPh765RMCZbarCHYVA/WIU48y2/XDIyMi1WDCamBpEyyHdve2A48TtiYbYlPnQm+0i0ONaiX9O3/9VQKVgTVUjn8X/yHQ6AmxqXMxY+rV5iJR9uPlwwQKUBkyx7lyoMt5khMWOIVyRJzl2Du/QmghItmnRVIXGu2tqCwqhye52pUVBA0LnGY5PIzbKgf+NKUCUYlbTem0iC7UGBbcMkHU9KAcFSGlrlRvWCpkuI18duUu4cxMYRaPKyrfPribwAQxbVLlEB8rpS6k1JXqt761QKAGlVp43P698ziBgkhT5UCBobfLwYxrvRypeJwrB+oNQbYcmCYGIhUW0ZgNmVQ5GuGpOzcJnA+0w1COAZVOl2PNsuPp++C8ZYZydJEOlePq+AiBu4Hus+WL3wmcN8I91LvwJnHcw58AAAAASUVORK5CYII=")
const picLoading = ref(true)
const generatePicCode = async ()=>{
  picLoading.value = true
  picCode.value.code = ""
  await sendPicCaptcha().then(res=>{
    picCode.value.pid = res.data.pid
    picCode.value.img = "data:image/png;base64, "+res.data.base64
    setTimeout(()=>{
      preImg.value = picCode.value.img
    }, 300)
  }).catch((error)=>{
    snackbarStore.showErrorMessage("网络异常")
    picCode.value.img = errorPicImg
  })
  picLoading.value = false;
}

const currentForm = () => {
  return isLogin.value?loginForm:registerForm
}

const sendEmailCode = async () => {
  const validEmailHandler = (value) => value && /^[\w-]+@([\w-]+\.)+[\w-]{2,4}$/.test(value)? null : '请输入正确的邮箱'
  let error = validEmailHandler(currentForm().value.email)
  if(error){
    snackbarStore.showErrorMessage(error)
    return
  }
  if(!needOpenPicCode.value){
    await generatePicCode()
    snackbarStore.showInfoMessage("请输入验证码")
    needOpenPicCode.value = true;
  }else{
    if(!picCode.value.code){
      snackbarStore.showErrorMessage("图片验证码不能为空")
      return
    }
    sendLoading.value = true
    await sendCodeToEmail(currentForm().value.email, picCode.value.pid, picCode.value.code).then(res=>{
      snackbarStore.showSuccessMessage("已发送邮箱验证码，请检查邮箱")
      needOpenPicCode.value = false;
      coolDownTimer.startCoolDown(res.data.email, res.data.coolDown)
    }).catch((error)=>{
      snackbarStore.showErrorMessage("发送失败："+error.message)
      generatePicCode()
    })
    sendLoading.value = false
  }
}

const login = async()=>{
  const errorsMsg = validateAndReturn(["email","code"], loginForm.value, loginValidators)
  if (!errorsMsg) {
    submitLoading.value = true
    await emailLogin(loginForm.value).then(res=>{
      const userData: UserVO = res.data.userVO as UserVO
      userStore.setUserInfo(userData, res.data.token)
      router.push({path:'/workplace'})
      snackbarStore.showSuccessMessage("🌈 欢迎回来，"+userData.email+"!")
    }).catch((error)=>{
      generatePicCode()
      snackbarStore.showErrorMessage("登录失败，请检查邮箱或验证码是否正确!")
    })
  }else{
    snackbarStore.showErrorMessage(errorsMsg)
  }
  submitLoading.value = false
  await generatePicCode()
}

const register = async()=>{
  const errorsMsg = validateAndReturn(["email","code","password","confirmPassword"], registerForm.value, registerValidators)
  if (!errorsMsg) {
    submitLoading.value = true
    await emailRegister(registerForm.value).then(res=>{
      console.log(res.data)
      const userData: UserVO = res.data.userVO as UserVO
      userStore.setUserInfo(userData, res.data.token)
      router.push({path:'/workplace'})
      snackbarStore.showSuccessMessage("🌈 欢迎加入Cyber Nomads!")
    }).catch((error)=>{
      generatePicCode()
      snackbarStore.showErrorMessage("注册失败："+error.message)
    })
  }else{
    snackbarStore.showErrorMessage(errorsMsg)
  }
  submitLoading.value = false
}

const switchLogin = async () =>{
  isLogin.value = !isLogin.value
  needOpenPicCode.value = false;
  await generatePicCode()
}

</script>

<template>
  <div class="login-container">
    <div id="page" class="site">
      <div class="container">
        <div class="login">
          <div class="hero">
            <h1>你好 世界<br />Cyber Nomads</h1>
            <v-fade-transition leave-absolute>
<!--              <p style="font-weight: 600;font-size:1.2rem">如果你{{isLogin?'没':'已'}}有账号<br />可以<a style="font-weight: 600" @click="switchLogin()">👉点击这里</a> {{isLogin?'进行注册':'进行登录'}}.</p>-->
              <p style="font-weight: 600;font-size:1.0rem">欢迎来到CyberNomads<br/>快来<a style="font-weight: 600">直接登录</a>体验吧</p>
            </v-fade-transition>
          </div>
          <div v-if="isLogin" class="main">
            <form action="">
              <p>
                <input v-model="loginForm.email" type="email" placeholder="邮箱" />
              </p>
              <transition name="fade">
                <p v-if="needOpenPicCode" class="code-container">
                  <input v-model="picCode.code" placeholder="图片验证码" class="pic-code-input"/>
                  <img v-if="!picLoading" @click="generatePicCode()" :src="picCode.img" alt="Base64 Image" class="pic-code" />
                  <v-img v-else
                         class="mx-auto"
                         :lazy-src="preImg"
                  >
                    <template v-slot:placeholder>
                      <CircleLoading/>
                    </template>
                  </v-img>
                </p>
              </transition>
              <p class="code-container">
                <input v-model="loginForm.code" placeholder="验证码" class="input-code"/>
                <v-btn :loading="sendLoading" @click="sendEmailCode()" :disabled="coolDownTimer.getCoolDownTime(loginForm.email)>0" color="#5865f2" min-height="60" class="send-code fixed-width-btn">
                  <template v-if="coolDownTimer.getCoolDownTime(loginForm.email)>0">
                    {{ coolDownTimer.getCoolDownTime(loginForm.email) }}秒后重新发送
                  </template>
                  <template v-else>
                    发送验证码
                  </template>
                  <template v-slot:loader>
                    <v-progress-linear indeterminate></v-progress-linear>
                  </template>
                </v-btn>
              </p>
              <p>
                <v-btn
                  color="#5865f2"
                  @click="login()"
                  :loading="submitLoading"
                  class="submit"
                >
                  登录
                  <template v-slot:loader>
                    <v-progress-linear indeterminate></v-progress-linear>
                  </template>
                </v-btn>
              </p>
            </form>
            <div class="options">
              <div class="separator">
                <p>使用其他方式登录</p>
              </div>
              <ul>
                <li v-for="login in otherLogin">
                  <a @click="login.handler()"><ion-icon style="font-size: 2rem" :src="login.logo"></ion-icon></a>
                </li>
              </ul>
            </div>
          </div>
<!--          <div v-if="!isLogin" class="main">-->
<!--            <form action="">-->
<!--              <p>-->
<!--                <input v-model="registerForm.email" type="email" placeholder="邮箱" />-->
<!--              </p>-->
<!--              <p>-->
<!--                <input v-model="registerForm.password" type="password" placeholder="密码" />-->
<!--              </p>-->
<!--              <p>-->
<!--                <input v-model="registerForm.confirmPassword" type="password" placeholder="确认密码" />-->
<!--              </p>-->
<!--              <transition name="fade">-->
<!--                <p  v-if="needOpenPicCode" class="code-container">-->
<!--                  <input v-model="picCode.code" placeholder="图片验证码" class="pic-code-input"/>-->
<!--                  <img v-if="!picLoading" @click="generatePicCode()" :src="picCode.img" class="pic-code" />-->
<!--                  <v-img v-else-->
<!--                         class="mx-auto"-->
<!--                         :lazy-src="preImg"-->
<!--                  >-->
<!--                    <template v-slot:placeholder>-->
<!--                      <CircleLoading/>-->
<!--                    </template>-->
<!--                  </v-img>-->
<!--                </p>-->
<!--              </transition>-->
<!--              <p class="code-container">-->
<!--                <input v-model="registerForm.code" placeholder="验证码" class="input-code"/>-->
<!--                <v-btn :loading="sendLoading" @click="sendEmailCode()" :disabled="coolDownTimer.getCoolDownTime(registerForm.email)>0" color="#5865f2" min-height="60" class="send-code fixed-width-btn">-->
<!--                  <template v-if="coolDownTimer.getCoolDownTime(registerForm.email)>0">-->
<!--                    {{ coolDownTimer.getCoolDownTime(registerForm.email) }}秒后重新发送-->
<!--                  </template>-->
<!--                  <template v-else>-->
<!--                    发送验证码-->
<!--                  </template>-->
<!--                  <template v-slot:loader>-->
<!--                    <v-progress-linear indeterminate></v-progress-linear>-->
<!--                  </template>-->
<!--                </v-btn>-->
<!--              </p>-->
<!--              <p>-->
<!--                <v-btn-->
<!--                  color="#5865f2"-->
<!--                  size="small"-->
<!--                  variant="flat"-->
<!--                  @click="register()"-->
<!--                  :loading="submitLoading"-->
<!--                  class="submit text-none text-subtitle-1"-->
<!--                >-->
<!--                  注册-->
<!--                  <template v-slot:loader>-->
<!--                    <v-progress-linear indeterminate></v-progress-linear>-->
<!--                  </template>-->
<!--                </v-btn>-->
<!--              </p>-->
<!--            </form>-->
<!--          </div>-->
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
@import url("@/styles/view/login/_login.scss");
.fixed-width-btn {
  width: 125px;
  box-sizing: border-box;
}
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.6s ease;
}
.fade-enter, .fade-leave-to {
  opacity: 0;
}
</style>
