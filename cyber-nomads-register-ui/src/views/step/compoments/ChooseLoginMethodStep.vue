<template>
  <v-card
      class="mx-auto"
      width="600"
      min-height="500"
  >
    <v-card-title class="d-flex justify-space-between align-center">
      <div class="text-h5 text-medium-emphasis ps-2">
        选择登录方式
      </div>
    </v-card-title>
    <v-card-text>
      <v-row style="margin-top: 20%" justify="center">
        <v-col cols="auto"  class="pa-2 ma-5">
          <v-btn
              height="72"
              min-width="164"
              @click="openAccount()"
              append-icon="mdi-shield-key"
          >
            账号密码登录
          </v-btn>
        </v-col>

        <v-col cols="auto" class="pa-2 ma-5">
          <v-btn
              height="72"
              min-width="164"
              @click="openCloudData()"
              append-icon="mdi-cloud-arrow-down"
          >
            线上数据拉取
          </v-btn>
        </v-col>
      </v-row>
    </v-card-text>
  </v-card>

  <v-dialog
      v-model="accountDialog"
      max-width="600"
  >
    <v-card>
      <v-card-title class="d-flex justify-space-between align-center">
        <div class="text-h5 text-medium-emphasis ps-2">
          <v-icon  icon="mdi-robot-happy"></v-icon>
          平台账号登录
        </div>

        <v-btn
            icon="mdi-close"
            variant="text"
            @click="closeDialog()"
        ></v-btn>
      </v-card-title>
      <v-card-text>
        <v-row dense>
          <v-col
              cols="12"
              sm="12"
          >
            <v-select
                :items=platforms
                label="账号平台"
                v-model="accountForm.platform"
                required
            ></v-select>
          </v-col>

          <v-col
              cols="12"
              sm="12"
          >
            <v-text-field
                :label="accountForm.platform+'账号'"
                v-model="accountForm.username"
                required
            ></v-text-field>
          </v-col>
        </v-row>

      </v-card-text>

      <v-divider></v-divider>
      <div class="d-flex flex-column" style="margin: 15px">
        <v-btn
            class="mt-4"
            color="success"
            block
            @click="doLogin()"
        >
          登录启动
        </v-btn>
      </div>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import {login} from "@/api/cloudApi";
import {useSnackbarStore} from "@/stores/snackbarStore";

const snackbarStore = useSnackbarStore();
const platforms = ["bilibili"]

interface AccountForm{
  username: string,
  platform: string
}

const defaultForm = <AccountForm>{
  username:"",
  platform:platforms[0]
}

const accountForm = ref<AccountForm>({...defaultForm})

const accountDialog = ref(false)

const cloudDataDialog = ref(false)

const openAccount = () =>{
  accountDialog.value = true
}

const openCloudData = () =>{
  cloudDataDialog.value = true
}

const closeDialog = () =>{
  if(accountDialog.value){
    accountForm.value = {...defaultForm}
    accountDialog.value = false
  }
  if(cloudDataDialog.value){
    cloudDataDialog.value = false
  }
}

const doLogin = async()=>{
  if(accountForm.value.username===""){
    snackbarStore.showErrorMessage("请输入账号!")
    return;
  }
  await login(accountForm.value.username, accountForm.value.platform).then(res=>{
    if (res.code==="200") {
      snackbarStore.showSuccessMessage("数据已同步至账号")
      closeDialog()
    }else{
      snackbarStore.showErrorMessage("登录失败："+res.msg)
    }
  })
}
</script>
