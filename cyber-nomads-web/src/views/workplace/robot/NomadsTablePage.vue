<script setup lang="ts">
import {RobotVO, RobotForm, defaultValue} from "@/views/workplace/robot/robotTypes";
import { PlatformVO } from "@/types/platformType";
import { onMounted } from "vue";
import {getRobotList, addRobot, changeRobot, getCookie, changeCookie, deleteRobot} from "@/api/robotApi";
import { useSnackbarStore } from "@/stores/snackbarStore";
import {validateAndReturn, Validators} from "@/utils/validate";
import { getPlatforms } from "@/api/commonApi";
import { useCommonStore } from "@/stores/commonStore";
import { useRobotStore } from  "@/views/workplace/robot/robotStore"

const robotStore = useRobotStore();
const commonStore = useCommonStore();
const snackbarStore = useSnackbarStore();
const robotList = ref<RobotVO[]>([])
const platformList = ref<PlatformVO[]>([])
const robotForm = ref<RobotForm>({...defaultValue.defaultRobotForm})
const isEdit = ref(false)
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
})

const addRobotReq = async () => {
  await addRobot(robotForm.value).then(res=>{
    robotStore.initRobotList()
    snackbarStore.showSuccessMessage("添加成功")
  }).catch(error=>{
    snackbarStore.showErrorMessage("添加失败")
  })
}

const changeRobotReq = async() => {
  if(robotForm.value.id!=-1){
    await changeRobot(robotForm.value).then(res=>{
      const robot = robotList.value.find(item => item.id === robotForm.value.id);
      robot.nickname = robotForm.value.nickname
      robot.plat = commonStore.getPlatformCnZh(robotForm.value.platform)
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
  robotForm.value.platform = commonStore.findPlatformByCnZh(item.plat).code
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
            <v-dialog v-model="dialog" max-width="700">
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
                      <v-col cols="12" sm="6">
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
                      <v-col cols="12" :sm="isEdit?6:12">
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
                        ></v-select>
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
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>

    <v-card class="mt-2">
      <v-table class="mt-5">
        <thead>
          <tr>
            <th class="text-subtitle-1 font-weight-semibold">Id</th>
            <th class="text-subtitle-1 font-weight-semibold">{{ $t("workplace.nomads.nickname") }}</th>
            <th class="text-subtitle-1 font-weight-semibold">{{ $t("workplace.nomads.platform") }}</th>
            <th class="text-subtitle-1 font-weight-semibold">{{ $t("workplace.nomads.username") }}</th>
            <th class="text-subtitle-1 font-weight-semibold">{{ $t("workplace.nomads.createDate") }}</th>
            <th class="text-subtitle-1 font-weight-semibold">{{ $t("workplace.nomads.isBan") }}</th>
            <th class="text-subtitle-1 font-weight-semibold" style="padding-left: 6vh">{{ $t("workplace.nomads.action") }}</th>
          </tr>
        </thead>
        <tbody class="text-body-1">
          <tr v-for="item in robotList" :key="item.id">
            <td class="font-weight-bold">#{{ item.id }}</td>
            <td class="font-weight-bold">{{ item.nickname }}</td>
            <td>{{ item.plat }}</td>
            <td>{{ item.username }}</td>
            <td>{{ item.createTime }}</td>
            <td>
              <v-chip
                class="font-weight-bold"
                :color="item.ban?'red':'green'"
                size="large"
                label
                ><v-icon size="large">{{item.ban?'mdi-robot-off':'mdi-robot-happy'}}</v-icon></v-chip
              >
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
