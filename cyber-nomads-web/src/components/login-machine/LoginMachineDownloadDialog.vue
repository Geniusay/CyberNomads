<template>
  <v-card
    class="mx-auto"
    color="#36393f"
    max-width="650"
    min-height="350"
    theme="dark"
    variant="flat"
  >
    <v-sheet color="#202225">
      <v-card-item>
        <template v-slot:prepend>
          <v-card-title>
            <v-icon
              icon="mdi-download-box"
              start
            ></v-icon>

            登号器下载
          </v-card-title>
        </template>

        <v-divider class="mx-2" vertical></v-divider>

        <template v-slot:append>
          <v-btn
            @click="dialogStore.closeDialog('loginMachine')"
            icon="$close"
            size="large"
            variant="text"
          ></v-btn>
        </template>
      </v-card-item>
    </v-sheet>

    <v-card
      class="ma-4"
      color="#2f3136"
      rounded="lg"
      variant="flat"
    >
      <v-card-item>
        <v-card-title class="text-body-2 d-flex align-center">
          <v-icon
            color="#949cf7"
            icon="mdi-calendar"
            start
          ></v-icon>

          <span class="text-medium-emphasis font-weight-bold">{{ formatDate(loginMachineInfo.updateTime) }}</span>

          <v-spacer></v-spacer>

          <v-avatar
            image="/assets/svg/baidu.svg"
            size="x-small"
          ></v-avatar>

          <v-chip
            class="ms-2 text-medium-emphasis"
            color="grey-darken-4"
            prepend-icon="mdi-file-cloud"
            size="small"
            text="百度网盘"
            variant="flat"
          ></v-chip>
        </v-card-title>

        <div class="py-2">
          <div class="text-h6">密码：{{loginMachineInfo.code}}</div>

          <div class="font-weight-light text-medium-emphasis">
            <CopyLabel :text="loginMachineInfo.url" />
          </div>
        </div>
      </v-card-item>

      <v-divider></v-divider>

      <div class="pa-4 d-flex align-center">
        <v-icon
          color="disabled"
          icon="mdi-broadcast"
          start
        ></v-icon>

        <span class="text-caption text-medium-emphasis ms-1 font-weight-light">

           <v-chip
             class="ma-2"
             color="blue"
             label
           >
        <v-icon icon="mdi-label" start></v-icon>
           latest {{loginMachineInfo.version}}
          </v-chip>
        </span>

        <v-spacer></v-spacer>

        <v-btn
          icon="mdi-dots-horizontal"
          variant="text"
        ></v-btn>

        <v-btn
          class="text-none"
          prepend-icon="mdi-check"
          variant="text"
          border
          @click="goToDownload()"
        >
          下载
        </v-btn>
      </div>
    </v-card>
  </v-card>

</template>

<script setup lang="ts">
import {queryLoginMachineInfo} from "@/api/commonApi";
import {useSnackbarStore} from "@/stores/snackbarStore";
import {useDialogStore} from "@/stores/dialogStore";
import {formatDate} from "@/utils/toolUtils";
import CopyLabel from "@/components/common/CopyLabel.vue";

const snackbarStore = useSnackbarStore();
const dialogStore = useDialogStore()

interface LoginMachineInfo{
  url:string,
  code:string,
  version:string,
  createTime:string,
  updateTime:string
}
const loginMachineInfo = ref<LoginMachineInfo>({})

onMounted(async ()=>{
  await queryLoginMachineInfo(1).then(res=>{
      loginMachineInfo.value = res.data as LoginMachineInfo
  }).catch(error=>{
    snackbarStore.showErrorMessage("获取下载器失败"+error.message)
  })
})

const goToDownload =()=>{
  window.open(loginMachineInfo.value.url+"?pwd="+loginMachineInfo.value.code, "_blank");
}

</script>
