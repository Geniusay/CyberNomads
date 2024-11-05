<script setup lang="ts">
import {RobotVO, RobotForm, defaultValue} from "@/views/workplace/robot/RobotTypes";
import { PlatformVO } from "@/types/platformType";
import { onMounted } from "vue";
import { getRobotList, getPlatforms, addRobot, changeRobot } from "@/api/robotApi";
import { useSnackbarStore } from "@/stores/snackbarStore";
import {validateAndReturn, Validators} from "@/utils/validate";


var snackbarStore = useSnackbarStore();
const robotList = ref<RobotVO[]>([])
const platformList = ref<PlatformVO[]>([])
const robotForm = ref<RobotForm>({...defaultValue.defaultRobotForm})
const isEdit = ref(false)
const robotFormValidator: Validators<RobotForm> = {
  id: (value)=>null,
  platform: (value) => platformList.value.some(platform=>platform.code === value) ? null : '请选择正确的平台',
  nickname: (value) => value ? null : '请输入正确的昵称',
  username: (value) => value ? null : '请输入正确的账号',
  cookie: (value) => value ? null : '请输入cookie'
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
    value => !isEdit.value&&value?null:'请输入昵称'
  ]
}

function findPlatformByCnZh(platformCnZh: string): PlatformVO | undefined {
  return platformList.value.find(item => item.platformCnZh === platformCnZh);
}

onMounted(async ()=>{
  await getRobotListReq()
  await getPlatforms().then(res=>{
    platformList.value = res.data as PlatformVO[]
  }).catch(error=>{
    snackbarStore.showErrorMessage("获取平台列表，网络异常")
  })
})

const getRobotListReq = async ()=>{
  await getRobotList().then(res=>{
    robotList.value = res.data as RobotVO[]
  }).catch(error=>{
    snackbarStore.showErrorMessage("获取赛博游民列表，网络异常")
  })
}

const addRobotReq = async () => {
  const errorsMsg = validateAndReturn(["platform","nickname","username"], robotForm.value, robotFormValidator)
  if (!errorsMsg) {
    await addRobot(robotForm).then(res=>{
      getRobotListReq()
      snackbarStore.showErrorMessage("添加成功")
    }).catch(error=>{
      snackbarStore.showErrorMessage("添加失败")
    })
  }else{
    snackbarStore.showErrorMessage(errorsMsg)
  }
}

const changeRobotReq = async() => {
  if(robotForm.value.id!=-1){
    await changeRobot(robotForm).then(res=>{

    }).catch(error=>{
      snackbarStore.showErrorMessage("编辑失败")
    })
  }else{
    snackbarStore.showErrorMessage("错误的编辑内容")
  }
}

const dialog = ref(false);
const search = ref("");
const refForm = ref();

function editItem(item: any) {
  isEdit.value = true;
  robotForm.value = Object.assign({}, item)  as RobotForm;
  robotForm.value.platform = findPlatformByCnZh(item.plat).code
  dialog.value = true;
}

function deleteItem(item: any) {
  const index = robotList.value.indexOf(item);
  robotList.value.splice(index, 1);
}

function close() {
  dialog.value = false;
  isEdit.value = false;
  Object.assign(robotForm, defaultValue.defaultRobotForm)
}

async function save() {
  if(isEdit.value){
    await changeRobotReq()
  }else{
    await addRobotReq()
  }
  close();
}
const formTitle = () => {
  return isEdit.value ? "workplace.nomads.editRobot":"workplace.nomads.addRobot";
}

</script>
<template>
  <v-container>
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

                          v-model="robotForm.nickname"
                          :label="$t('workplace.nomads.nickname')"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6">
                        <v-text-field
                          variant="outlined"
                          color="primary"
                          density="compact"
                          v-model="robotForm.cookie"
                          label="Cookie"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="12">
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
            <th class="text-subtitle-1 font-weight-semibold">{{ $t("workplace.nomads.action") }}</th>
          </tr>
        </thead>
        <tbody class="text-body-1">
          <tr v-for="item in robotList" :key="item.id">
            <td class="font-weight-bold">#{{ item.id }}</td>
            <td class="font-weight-bold">{{ item.username }}</td>
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
            <td>
              <div class="d-flex align-center">
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
                      @click="deleteItem(item)"
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
