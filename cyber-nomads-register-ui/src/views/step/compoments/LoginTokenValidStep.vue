<template>
  <v-card
      class="mx-auto"
      width="600"
  >
    <v-card rounded="lg">
      <v-card-title class="d-flex justify-space-between align-center">
        <div class="text-h5 text-medium-emphasis ps-2">
          登号器令牌验证
        </div>
      </v-card-title>

      <v-divider class="mb-4"></v-divider>

      <v-card-text>
        <div class="mb-2">登号器令牌</div>

        <v-textarea
            :counter="300"
            class="mb-2"
            rows="2"
            variant="outlined"
            persistent-counter
            v-model="loginToken"
        ></v-textarea>

        <div class="d-flex flex-column" style="margin: 15px">
          <v-btn
              class="mt-4"
              color="success"
              block
              @click="validate"
          >
            Validate
          </v-btn>
        </div>
        <div class="text-overline mb-2">❓ 如何获取登号器令牌</div>

        <div class="text-medium-emphasis mb-1">
          令牌错误或者没有令牌，请注册账号后，并请点击
          <v-chip
              @click="openGetTokenLink()"
              class="ma-2"
              color="pink"
              label
          >
            <v-icon icon="mdi-label" start></v-icon>
            令牌获取
          </v-chip>
        </div>

      </v-card-text>

    </v-card>
  </v-card>
</template>

<script setup lang="ts">
import {useSnackbarStore} from "@/stores/snackbarStore";
import {useRouter} from "vue-router";
import {useStepStore} from "@/stores/stepStore";
import {verifyCode} from "@/api/cloudApi"

const stepStore = useStepStore();
const router = useRouter()
const snackbarStore = useSnackbarStore();


const loginToken = ref("")

const validate = async () =>{
  await verifyCode(loginToken.value).then(res=>{
    if(res.code==='200'){
      snackbarStore.showSuccessMessage("令牌校验正确")
      stepStore.changeValid(1)
      stepStore.step+=1
    }else{
      snackbarStore.showErrorMessage("令牌校验失败")
    }
  })

}

const nameRules = [
  v => !!v || '请输入登号器令牌，如果没有令牌请去 https://www.cybernomads.top/data/cyber-nomads 获取',
]

const openGetTokenLink = () =>{
  const url = "https://www.cybernomads.top/data/cyber-nomads";
  const newTab = window.open(url, "_blank");
  if (!newTab) {
    snackbarStore.showErrorMessage("无法打开新标签页，可能被浏览器拦截。");
  }
}
</script>
