<script setup lang="ts">
import {RobotVO, RobotForm, defaultValue} from "@/views/workplace/robot/robotTypes";
import { PlatformVO } from "@/types/platformType";
import { onMounted } from "vue";
import {addRobot, changeRobot, getCookie, changeCookie, deleteRobot, generateLoginMachine, shareRobot} from "@/api/robotApi";
import { useSnackbarStore } from "@/stores/snackbarStore";
import {validateAndReturn, Validators} from "@/utils/validate";
import { useCommonStore } from "@/stores/commonStore";
import { useRobotStore } from  "@/views/workplace/robot/robotStore"
import {useDialogStore} from "@/stores/dialogStore";
import { useTaskStore,snackbarStore } from "@/views/workplace/task/taskStore"
import { Icon } from "@iconify/vue";

import EmptyDataPage from "@/components/empty/EmptyDataPage.vue";
import CircleLoading from "@/components/loading/CircleLoading.vue";
import CopyLabel from "@/components/common/CopyLabel.vue";
import LoginMachineDownloadDialog from "@/components/login-machine/LoginMachineDownloadDialog.vue";

const dialogStore = useDialogStore()
const robotStore = useRobotStore();
const commonStore = useCommonStore();
const snackbarStore = useSnackbarStore();
const taskStore = useTaskStore();

const robotList = ref<RobotVO[]>([])
const platformList = ref<PlatformVO[]>([])
const robotForm = ref<RobotForm>({...defaultValue.defaultRobotForm})
const isEdit = ref(false)
const emptyState = ref(false)
const pageLoading = ref(true)
const loginMachineTokenLoading = ref(false)
const robotFormValidator: Validators<RobotForm> = {
  id: (value)=>null,
  platform: (value) => platformList.value.some(platform=>platform.code === value) ? null : '请选择正确的平台',
  nickname: (value) => value ? null : '请输入正确的昵称',
  username: (value) => value ? null : '请输入正确的账号',
};

const saveButtonDisabled = () =>{
  if(!isEdit.value){
    return !!validateAndReturn(["platform","nickname","username"], robotForm.value, robotFormValidator)
  }else{
    return false;
  }
}

const rules = {
  nickname: [
    value => value?null:'请输入昵称'
  ],
  username:[
    value => value?null:'请输入账号'
  ]
}


onMounted(async ()=>{
  await commonStore.initPlatformsVO()
  await robotStore.initRobotList()
  robotList.value = robotStore.getRobotList as RobotVO[]
  platformList.value = commonStore.getPlatformList as PlatformVO[]
  emptyState.value = robotList.value.length == 0
  pageLoading.value = false
})

const addRobotReq = async () => {
  await addRobot(robotForm.value).then(async res=>{
    await robotStore.initRobotList()
    robotList.value = robotStore.getRobotList as RobotVO[]
    snackbarStore.showSuccessMessage("添加成功")
  }).catch(error=>{
    snackbarStore.showErrorMessage("添加失败: 请检查账号是否重复")
  })
}

const changeRobotReq = async() => {
  if(robotForm.value.id!=-1){
    await changeRobot(robotForm.value).then(res=>{
      const robot = robotList.value.find(item => item.id === robotForm.value.id);
      robot.nickname = robotForm.value.nickname
      robot.platformCnZh = commonStore.getPlatformCnZh(robotForm.value.platform)
      robot.username = robotForm.value.username
      snackbarStore.showSuccessMessage("编辑成功")
    }).catch(error=>{
      snackbarStore.showErrorMessage("编辑失败"+error.message)
    })
  }else{
    snackbarStore.showErrorMessage("错误的编辑内容")
  }
}

const dialog = ref(false);
const search = ref("");
const refForm = ref();

async function editItem(item: any) {
  isEdit.value = true;
  robotForm.value = Object.assign({}, item)  as RobotForm;
  robotForm.value.platform = item.platformCode
  dialog.value = true;
}

async function openAddDialog() {
  isEdit.value = false;
  robotForm.value = {...defaultValue.defaultRobotForm}
  dialog.value = true;
}

function close() {
  dialog.value = false;
  isEdit.value = false;
  robotForm.value = Object.assign({}, defaultValue.defaultRobotForm)
}

async function save() {
  const errorsMsg = validateAndReturn(["platform","nickname","username"], robotForm.value, robotFormValidator)
  if (!errorsMsg) {
    if(isEdit.value){
      await changeRobotReq()
    }else{
      await addRobotReq()
    }
    close();
  }else{
    snackbarStore.showErrorMessage(errorsMsg)
  }

}
const formTitle = () => {
  return isEdit.value ? "workplace.nomads.editRobot":"workplace.nomads.addRobot";
}
// cookie模块
const currentRobotCookie = ref({
  cookie:"",
  id:""
})
const cookieDialog = ref(false)
const cookieEyes = ref(true)
const cookieLoading = ref(false)

const cookieTable = async (item) =>{
  cookieDialog.value = true
  if(!item.cookie){
    cookieLoading.value = true
    await getCookie(item.id).then(res=>{
      item.cookie = res.data.cookie
    }).catch(error=>{
      snackbarStore.showErrorMessage("获取异常")
    })
    cookieLoading.value = false
  }
  currentRobotCookie.value.cookie = item.cookie
  currentRobotCookie.value.id = item.id
}

const changeCookieReq = async()=>{
  cookieLoading.value = true
  await changeCookie(currentRobotCookie.value.id, currentRobotCookie.value.cookie).then(
    res=>{
      const robot = robotList.value.find(robot=>robot.id===currentRobotCookie.value.id)
      if(!!robot){
        robot.cookie = currentRobotCookie.value.cookie
      }
      snackbarStore.showSuccessMessage("更新cookie成功")
    }
  ).catch(error=>{
    snackbarStore.showErrorMessage("更新cookie失败:"+error.message)
  })
  cookieLoading.value = false
  cookieDialog.value = false
  cookieEyes.value = true
}

// 删除模块
const deleteDialog = ref(false)
const deleteItem = ref<RobotVO>()

const deleteTable = (item:any)=>{
  deleteDialog.value = true;
  deleteItem.value = item
}

const deleteRobotReq = async () => {
  await deleteRobot(deleteItem.value.id).then(res=>{
    snackbarStore.showSuccessMessage("删除成功")
    const index = robotList.value.indexOf(deleteItem.value);
    robotList.value.splice(index, 1);
  }).catch(error=>{
    snackbarStore.showErrorMessage("删除失败:"+error.message)
  })
  deleteDialog.value =false;
  deleteItem.value = {}

}

const loginMachineToken = ref("")

// 生成令牌
const generateToken = async () =>{
  await generateLoginMachine().then(res=>{
    loginMachineToken.value = res.data
    snackbarStore.showSuccessMessage("登号器令牌生成成功，10分钟后过期，请妥善保管!")
  }).catch(error=>{
    snackbarStore.showErrorMessage("登号器令牌生成失败："+error.message)
    loginMachineDialog.value = false
  })
}

const loginMachineDialog = ref(false)

const openLoginMachineDialog = async () =>{
  loginMachineTokenLoading.value = true
  await generateToken()
  loginMachineDialog.value = true
  loginMachineTokenLoading.value = false
}

const taskTypes = ref([])
const shareDialog = ref(false)
const sharedRobot = ref<RobotVO>({})
const sharedReq = ref({
  robotId:"",
  flag:false,
  focusTask:[]
})

const openSharedDialog = async (item: RobotVO) =>{
  sharedReq.value = {
    robotId: item.id,
    flag: !item.hasShared,
    focusTask: []
  }
  sharedRobot.value = item
  shareDialog.value = true
  await taskStore.initPlatformTaskTypes(item.platform)
  taskTypes.value = taskStore.getPlatformTaskTypes(item.platform)
}

const shareRobotReq = async()=>{
  const flag = sharedReq.value.flag
  await shareRobot(sharedReq.value).then(res=>{
    snackbarStore.showSuccessMessage(flag?"分享成功!":"收回成功!")
    let index = robotList.value.findIndex(item => item.id === sharedReq.value.robotId)
    robotList.value[index].hasShared = flag
  }).catch(error=>{
    snackbarStore.showErrorMessage((flag?"分享失败:":"收回失败:") + error.message)
  })
  shareDialog.value = false
}

</script>
<template>
  <v-container>
    <v-dialog v-model="cookieDialog" max-width="700">
      <v-card>
        <v-card-title class="pa-4 bg-orange">
          <span class="title text-white">
            <v-icon class="mr-2">mdi-cookie-edit-outline</v-icon>
           Cookie Info</span>
        </v-card-title>

        <v-card-text>
          <v-row>
            <v-col
              cols="12"
              sm="12"
            >
              <v-text-field
                v-model="currentRobotCookie.cookie"
                :append-icon="cookieEyes ? 'mdi-eye-off' : 'mdi-eye'"
                prepend-icon="mdi-send"
                :type="cookieEyes ? 'password' : 'text'"
                name="input-10-1"
                @click:append="cookieEyes = !cookieEyes"
                @click:prepend="changeCookieReq()"
                placeholder="请输入Cookie"
              >
               <template v-slot:append-inner>
                <v-fade-transition leave-absolute>
                  <v-progress-circular
                    v-if="cookieLoading"
                    color="info"
                    size="24"
                    indeterminate
                  ></v-progress-circular>
                </v-fade-transition>
              </template>
              </v-text-field>
            </v-col>
          </v-row>
        </v-card-text>
      </v-card>
    </v-dialog>
    <v-dialog v-model="deleteDialog" max-width="700">
      <v-card>
        <v-card-title class="pa-4 bg-red">
          <span class="title text-white">
            <v-icon class="mr-2">mdi-robot-dead-outline</v-icon>
           Delete Robot</span>
        </v-card-title>

        <v-card-text>
          确定要删除{{deleteItem.nickname}}吗，这个操作无法撤回!
        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions class="pa-4">
          <v-spacer></v-spacer>
          <v-btn color="gray" variant="flat" @click="deleteDialog=false">Cancel</v-btn>
          <v-btn
            color="red"
            variant="flat"
            @click="deleteRobotReq()"
          >Delete</v-btn
          >
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-card>
      <v-card-text>
        <v-row>
          <v-col cols="12" lg="4" md="6">
            <v-text-field
              density="compact"
              v-model="search"
              label="Search Contacts"
              hide-details
              variant="outlined"
              color="primary"
            ></v-text-field>
          </v-col>
          <v-col cols="12" lg="8" md="6" class="text-right">
            <v-dialog v-model="dialogStore.dialogMap['loginMachine']" max-width="800">
              <template v-slot:activator="{ props }">
                <v-btn @click="dialogStore.openDialog('loginMachine')" style="margin-right:15px" color="blue" v-bind="props" flat class="ml-auto">
                  <v-icon color="white" class="mr-2">mdi-download-box</v-icon>
                  {{ $t("workplace.nomads.downloadLoginMachine") }}
                </v-btn>
              </template>
              <LoginMachineDownloadDialog />
            </v-dialog>

            <v-dialog v-model="loginMachineDialog" max-width="800">
              <template v-slot:activator="{ props }">
                <v-btn @click="openLoginMachineDialog()" style="margin-right:15px" color="warning" v-bind="props" flat class="ml-auto">
                  <v-icon color="white" class="mr-2">mdi-cloud-key</v-icon>
                  {{ $t("workplace.nomads.generateToken") }}
                </v-btn>
              </template>
              <v-card
                class="pa-4 bg-orange"
                prepend-icon="mdi-cloud-key"
                subtitle="登号器令牌将在10分钟后过期，请妥善保管!"
              >
                <template v-slot:title>
                  <span class="font-weight-black">
                    登号器令牌
                  </span>
                </template>
                <v-card-text class="bg-surface-light pt-4">
                  <CircleLoading v-if="loginMachineTokenLoading"/>
                  <td v-else class="font-weight-bold text-body-2">
                    <CopyLabel :text="loginMachineToken" />
                  </td>
                </v-card-text>
              </v-card>
            </v-dialog>

            <v-dialog id="edit robot" v-model="dialog" max-width="700">
              <template v-slot:activator="{ props }">
                <v-btn color="primary" v-bind="props" flat class="ml-auto">
                  <v-icon class="mr-2">mdi-robot-industrial</v-icon>
                  {{ $t("workplace.nomads.addRobot") }}
                </v-btn>
              </template>
              <v-card>
                <v-card-title class="pa-4 bg-secondary">
                  <span class="title text-white">{{ $t(formTitle()) }}</span>
                </v-card-title>

                <v-card-text>
                  <v-form
                    class="mt-5"
                    ref="form"
                    v-model="refForm"
                    lazy-validation
                  >
                    <v-row>
                      <v-col cols="12" sm="12">
                        <v-text-field
                          v-if="isEdit"
                          variant="outlined"
                          color="primary"
                          density="compact"
                          disabled
                          v-model="robotForm.id"
                          label="Id"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6">
                        <v-text-field
                          variant="outlined"
                          color="primary"
                          density="compact"
                          :rules="rules['nickname']"
                          v-model="robotForm.nickname"
                          :label="$t('workplace.nomads.nickname')"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6">
                        <v-select
                          variant="outlined"
                          color="primary"
                          density="compact"
                          :items="platformList"
                          v-model="robotForm.platform"
                          :label="$t('workplace.nomads.platform')"
                          item-title="platformCnZh"
                          item-value="code"
                        >
                          <template v-slot:chip="{ props, item }">
                            <v-chip
                              style="font-size: 1.1em"
                              v-bind="props"
                            >
                              <v-icon>
                                <img :src="commonStore.getPlatformImgUrl(item.raw.platform)" alt="My Icon" />
                              </v-icon>
                              {{item.raw.platformCnZh}}
                            </v-chip>
                          </template>

                          <template v-slot:item="{ props, item }">
                            <v-list-item
                              v-bind="props"
                            >
                              <v-chip  style="font-size: 1.1em">
                                <v-icon>
                                  <img :src="commonStore.getPlatformImgUrl(item.raw.platform)" alt="My Icon" />
                                </v-icon>
                                {{item.raw.platformCnZh}}
                              </v-chip>
                            </v-list-item>
                          </template>
                        </v-select>
                      </v-col>
                      <v-col cols="12" sm="12">
                        <v-text-field
                          variant="outlined"
                          color="primary"
                          density="compact"
                          :rules="rules['username']"
                          required
                          v-model="robotForm.username"
                          :label="$t('workplace.nomads.username')"
                        ></v-text-field>
                      </v-col>
                    </v-row>
                  </v-form>
                </v-card-text>
                <v-divider></v-divider>
                <v-card-actions class="pa-4">
                  <v-spacer></v-spacer>
                  <v-btn color="error" @click="close">Cancel</v-btn>
                  <v-btn
                    color="secondary"
                    :disabled="
                      saveButtonDisabled()
                    "
                    variant="flat"
                    @click="save"
                  >Save</v-btn
                  >
                </v-card-actions>
              </v-card>
            </v-dialog>

            <v-dialog id="share robot" v-model="shareDialog" max-width="700">
              <v-card
                class="mx-auto"
                color="#36393f"
                min-width="650"
                max-height="350"
                theme="dark"
                variant="flat"
              >
                <v-sheet color="#202225">
                  <v-card-item>
                    <template v-slot:prepend>
                      <v-card-title>
                        <Icon style="box-sizing: border-box; display: inline-block; vertical-align: middle; margin-right: 8px;"  width="30"
                              icon="line-md:external-link-rounded" />
                        <span style="box-sizing: border-box;">{{sharedRobot.hasShared?'收回账号':'分享账号'}}</span>
                      </v-card-title>
                    </template>

                    <v-divider class="mx-2" vertical></v-divider>

                    <template v-slot:append>
                      <v-btn
                        icon="$close"
                        size="large"
                        variant="text"
                        @click="shareDialog=false"
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
                        icon="mdi-robot"
                        start
                      ></v-icon>

                      <span class="text-medium-emphasis font-weight-bold">{{sharedRobot.nickname}}</span>

                      <v-spacer></v-spacer>

                      <v-chip
                        class="ms-2 text-medium-emphasis"
                        color="grey-darken-4"
                        size="default"
                        variant="flat"
                      >
                        <span>{{sharedRobot.platformCnZh}}</span>
                        <v-icon >
                          <img :src="commonStore.getPlatformImgUrl(sharedRobot.platform)" alt="My Icon" start />
                        </v-icon>
                      </v-chip>
                    </v-card-title>

                    <div class="py-2" v-if="!sharedRobot.hasShared">
                      <v-col
                        cols="12"
                        sm="12"
                      >
                        <v-autocomplete
                          color="primary"
                          density="compact"
                          :items="taskTypes"
                          v-model="sharedReq.focusTask"
                          label="关注任务类型"
                          item-title="taskTypeValue"
                          item-value="taskTypeKey"
                          variant="outlined"
                          chips
                          closable-chips
                          multiple
                        ></v-autocomplete>
                      </v-col>

                      <div class="font-weight-light text-medium-emphasis">
                        该账号在共享过程中只会做选择的任务类型 （❗不选则为接受任何任务类型）
                      </div>
                    </div>
                  </v-card-item>

                  <v-divider></v-divider>

                  <div class="pa-4 d-flex align-center">
                    <v-btn
                      icon="mdi-help-circle-outline"
                      class="text-none text-subtitle-1"
                      size="small"
                      variant="text"
                    >
                    </v-btn>

                    <v-spacer></v-spacer>

                    <v-btn
                      class="me-2 text-none"
                      :color="sharedRobot.hasShared?'red':'#4f545c'"
                      :prepend-icon="sharedRobot.hasShared?'mdi-share-off-outline':'mdi-export-variant'"
                      variant="flat"
                      @click="shareRobotReq()"
                    >
                      {{sharedRobot.hasShared?'收回':'分享'}}
                    </v-btn>
                  </div>
                </v-card>
              </v-card>
            </v-dialog>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>

    <v-card class="mt-2" min-height="700">
      <CircleLoading v-if="pageLoading" style="margin-top: 20%; margin-bottom: 20%"/>
      <EmptyDataPage
        v-else-if="emptyState"
        :title="'workplace.nomads.emptyTitle'"
        :content="'workplace.nomads.emptyTitle'"
        :handler=openAddDialog
        :btnText="'workplace.nomads.addRobot'"
        :errorImg="'/assets/img/empty_tips.png'"
      ></EmptyDataPage>
      <v-table v-else class="mt-5">
        <thead>
          <tr>
            <th class="text-subtitle-1 font-weight-semibold">Id</th>
            <th class="text-subtitle-1 font-weight-semibold">{{ $t("workplace.nomads.nickname") }}</th>
            <th class="text-subtitle-1 font-weight-semibold">{{ $t("workplace.nomads.platform") }}</th>
            <th class="text-subtitle-1 font-weight-semibold">{{ $t("workplace.nomads.username") }}</th>
            <th class="text-subtitle-1 font-weight-semibold">{{ $t("workplace.nomads.createDate") }}</th>
            <th class="text-subtitle-1 font-weight-semibold">{{ $t("workplace.nomads.isShare") }}</th>
            <th class="text-subtitle-1 font-weight-semibold" style="padding-left: 6vh">{{ $t("workplace.nomads.action") }}</th>
          </tr>
        </thead>
        <tbody class="text-body-1">
          <tr v-for="item in robotList" :key="item.id">
            <td class="font-weight-bold">#{{ item.id }}</td>
            <td class="font-weight-bold">{{ item.nickname }}</td>
            <td>
              <v-icon >
              <img :src="commonStore.getPlatformImgUrl(item.platform)" alt="My Icon" start />
              </v-icon>
              {{ item.platformCnZh }}</td>
            <td>{{ item.username }}</td>
            <td>{{ item.createTime }}</td>
            <td>
              <v-btn class="me-2 text-none"
                     variant="tonal"
                     :color="!item.hasShared?'red':'green'"
                     @click="openSharedDialog(item)"
              >
                <v-icon size="large">{{!item.hasShared?'mdi-robot-off':'mdi-robot-happy'}}</v-icon>
              </v-btn>
            </td>
            <td >
              <div class="d-flex">
                <v-tooltip text="Cookie">
                  <template v-slot:activator="{ props }">
                    <v-btn
                      icon
                      variant="text"
                      @click="cookieTable(item)"
                      v-bind="props"
                      color="orange"
                    >
                      <v-icon>mdi-cookie-cog</v-icon>
                      <!-- <img
                        width="26"
                        src="https://img.icons8.com/fluency/48/null/filled-trash.png"
                    /> -->
                    </v-btn>
                  </template>
                </v-tooltip>
                <v-tooltip text="Edit">
                  <template v-slot:activator="{ props }">
                    <v-btn
                      color="blue"
                      icon
                      variant="text"
                      @click="editItem(item)"
                      v-bind="props"
                    >
                      <v-icon>mdi-pencil-outline</v-icon>
                      <!-- <img
                        width="26"
                        src="https://img.icons8.com/fluency/48/null/edit.png"
                    /> -->
                    </v-btn>
                  </template>
                </v-tooltip>
                <v-tooltip text="Delete">
                  <template v-slot:activator="{ props }">
                    <v-btn
                      icon
                      variant="text"
                      @click="deleteTable(item)"
                      v-bind="props"
                      color="error"
                    >
                      <v-icon>mdi-delete-outline</v-icon>
                      <!-- <img
                        width="26"
                        src="https://img.icons8.com/fluency/48/null/filled-trash.png"
                    /> -->
                    </v-btn>
                  </template>
                </v-tooltip>
              </div>
            </td>
          </tr>
        </tbody>
      </v-table>
    </v-card>
  </v-container>
</template>
