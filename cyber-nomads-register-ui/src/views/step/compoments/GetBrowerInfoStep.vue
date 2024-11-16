<template>
  <v-card
      class="mx-auto"
      width="600"
  >
    <v-card rounded="lg">
      <v-card-title class="d-flex justify-space-between align-center">
        <div class="text-h5 text-medium-emphasis ps-2">
          获取本地浏览器配置
        </div>
      </v-card-title>

      <v-divider class="mb-4"></v-divider>

      <v-card-text>
        <div class="mb-2">
          {{!loading?"当前浏览器信息":"获取本地浏览器信息中...."}}
        </div>
        <v-skeleton-loader v-if="loading" type="card"></v-skeleton-loader>
        <v-card
            v-else
            class="mx-auto my-8"
            elevation="16"
            max-width="600"
        >
          <v-card-item>
            <v-card-title>
              {{map[systemInfo.name]}} 浏览器
            </v-card-title>

            <v-card-subtitle>
              浏览器版本：{{systemInfo.version}}
            </v-card-subtitle>
          </v-card-item>

          <v-card-text>
            浏览器地址：{{systemInfo.path}}
          </v-card-text>
        </v-card>

        <v-col cols="12">
          <v-autocomplete
              v-model="currentBrowser"
              :items="browserList"
              item-title="name"
              item-value="value"
              density="compact"
              label="选择浏览器"
          ></v-autocomplete>
        </v-col>

        <div class="d-flex flex-column" style="margin: 10px">
          <v-btn
              :loading="downloadStatusInfo.isDownload||loading"
              color="blue"
              height="60"
              style="border-radius: 8px"
              @click="downloadDriver()"
              block
          >
            下载驱动
            <template v-slot:loader>
              <v-progress-linear indeterminate></v-progress-linear>
            </template>
          </v-btn>
          <v-progress-linear
              rounded
              striped
              style="margin-top: 10px"
              v-if="downloadStatusInfo.process!=0"
              v-model="downloadStatusInfo.process"
              color="deep-orange"
              height="25"
          >
            <template v-slot:default="{ value }">
              <strong>{{ downloadStatusInfo.msg }}%</strong>
            </template>
          </v-progress-linear>
        </div>
        <div class="text-overline mb-2">❓ 这一步是干嘛的</div>

        <div class="text-medium-emphasis mb-1">
         这一步是自动帮你获取电脑的浏览器并下载对应浏览器官方提供的webdriver驱动，下载好的驱动会在当前登号器目录下，如果没找到你已经有对应浏览器和浏览器驱动可以跳过这一步骤手动输入。
        </div>

      </v-card-text>

    </v-card>
  </v-card>
</template>

<script setup lang="ts">
import {useSnackbarStore} from "@/stores/snackbarStore";
import {useRouter} from "vue-router";
import {download, getSystemInfo, downloadStatus} from "@/api/cloudApi"
import {onMounted} from "vue";

const router = useRouter()
const snackbarStore = useSnackbarStore();
const map = {
  msedge:"Edge",
  chrome:"Chrome"
}

const browserList = [
  {
    name:"Edge",
    value:"msedge"
  },
  {
    name:"Chrome",
    value:"chrome"
  }
]

const currentBrowser = ref("msedge")
const loading = ref(false)
const downloadStatusInfo = ref({
  isDownload: false,
  msg: "下载中",
  process: 0,
})
const systemInfo = ref({
  path:"",
  name:"",
  version:""
})

onMounted(async()=>{
  await getSystemInfoReq(currentBrowser.value)
})

const getSystemInfoReq  = async (browser) =>{
  loading.value = true
  await getSystemInfo(browser).then(res=>{
    systemInfo.value.path = res.data.path
    systemInfo.value.name = res.data.name
    systemInfo.value.version = res.data.version
    snackbarStore.showSuccessMessage("获取浏览器信息")
  })
  loading.value = false
}

const downloadDriver = async () =>{
  await download()
  downloadStatusInfo.value.process = 0
  watchDownloadStatus()
}


const watchDownloadStatus = ()=>{
  downloadStatusInfo.value.isDownload = true
  downloadStatusInfo.value.msg = "下载中"
  let interval = setInterval(async ()=>{
    await downloadStatus().then(res=>{
      downloadStatusInfo.value = res.data
      if(!downloadStatusInfo.value.isDownload){
        clearInterval(interval);
      }
    })
  },1000)
}
watch(
    () =>  currentBrowser.value, // 或 `taskStore.taskForm.params[param.name]`
    async (newVal, oldVal) => {
      await getSystemInfoReq(newVal)
    },
);
</script>
