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


  <v-dialog
      v-model="cloudDataDialog"
      :max-width="cloudLoading?320:1400"
  >
    <v-list
        v-if="cloudLoading"
        class="py-2"
        color="primary"
        elevation="12"
        rounded="lg"
    >
      <v-list-item
          prepend-icon="mdi-cloud-download"
          title="拉取云端数据中..."
      >
        <template v-slot:prepend>
          <div class="pe-4">
            <v-icon color="primary" size="x-large"></v-icon>
          </div>
        </template>

        <template v-slot:append>
          <v-progress-circular
              color="primary"
              indeterminate="disable-shrink"
              size="16"
              width="2"
          ></v-progress-circular>
        </template>
      </v-list-item>
    </v-list>
    <v-card v-else  class="mt-2">
      <v-card-text>
        <v-table class="mt-5">
          <thead>
          <tr>
            <th class="text-subtitle-1 font-weight-semibold">Id</th>
            <th class="text-subtitle-1 font-weight-semibold">昵称</th>
            <th class="text-subtitle-1 font-weight-semibold">平台</th>
            <th class="text-subtitle-1 font-weight-semibold">账号</th>
            <th class="text-subtitle-1 font-weight-semibold">创建时间</th>
            <th class="text-subtitle-1 font-weight-semibold">是否同步</th>
            <th class="text-subtitle-1 font-weight-semibold" style="padding-left: 6vh">登录</th>
          </tr>
          </thead>
          <tbody class="text-body-1">
          <tr v-for="item in robots" :key="item.id">
            <td class="font-weight-bold">#{{ item.id }}</td>
            <td class="font-weight-bold">{{ item.nickname }}</td>
            <td>
              {{ item.platformCnZh }}</td>
            <td>{{ item.username }}</td>
            <td>{{ item.createTime }}</td>
            <v-chip
                class="font-weight-bold"
                :color="item.isAsync?'green':'red'"
                size="large"
                label
            ><v-icon size="large">{{item.isAsync?'mdi-upload':'mdi-upload-off'}}</v-icon></v-chip>
            <td>
              <div class="d-flex">
                <v-tooltip text="Login">
                  <template v-slot:activator="{ props }">
                    <v-btn
                        icon
                        variant="text"
                        @click="robotLogin(item)"
                        v-bind="props"
                        color="green"
                    >
                      <v-icon>mdi-cloud-upload-outline</v-icon>
                    </v-btn>
                  </template>
                </v-tooltip>
              </div>
            </td>
          </tr>
          </tbody>
        </v-table>
      </v-card-text>

      <v-divider></v-divider>
      <v-card-actions class="pa-4">
        <v-spacer></v-spacer>
        <v-btn
            color="green"
            variant="flat"
            @click="oneKeyLogin()"
        >一键登录</v-btn>
        <v-btn
            color="red"
            variant="flat"
            @click="closeDialog()"
        >关闭</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import {login,getRobots} from "@/api/cloudApi";
import {useSnackbarStore} from "@/stores/snackbarStore";
import {AccountForm, RobotVO} from "@/types/AllTypes";

const snackbarStore = useSnackbarStore();
const platforms = ["bilibili"]



const defaultForm = <AccountForm>{
  username:"",
  platform:platforms[0]
}
const accountForm = ref<AccountForm>({...defaultForm})
const accountDialog = ref(false)
const cloudDataDialog = ref(false)
const cloudLoading = ref(true)
const robots = ref<[]>([])


const openAccount = () =>{
  accountDialog.value = true
}


const openCloudData = async () =>{
  cloudLoading.value = true
  cloudDataDialog.value = true
  await getRobots().then(res=>{
    if(res.code==="200"){
      console.log(typeof res.data)
      robots.value = res.data?res.data:[]
      snackbarStore.showSuccessMessage("拉取云端数据成功")
    }else{
      snackbarStore.showErrorMessage("拉取云端数据失败："+res.msg)
      closeDialog()
    }
  })
  cloudLoading.value = false
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

const autoLogin = async(username, platform)=>{
  await login(username, platform).then(res=>{
    if (res.code==="200") {
      if(res.data){
        snackbarStore.showSuccessMessage("数据已同步至云端")
        return;
      }
    }
    snackbarStore.showErrorMessage("登录失败")
    throw new Error("error");
  })
}

const oneKeyLogin = async ()=>{
  for (let i = 0; i <robots.value.length ; i++) {
    let robot = robots.value[i]
    await robotLogin(robot)
  }
}

const robotLogin = async(robot)=>{
  try {
    await autoLogin(robot.username, robot.platform);
    robot.isAsync = true
  }catch (error){

  }
}

const doLogin = async()=>{
  if(accountForm.value.username===""){
    snackbarStore.showErrorMessage("请输入账号!")
    return;
  }
  await autoLogin(accountForm.value.username,accountForm.value.platform);
  closeDialog()
}
</script>
