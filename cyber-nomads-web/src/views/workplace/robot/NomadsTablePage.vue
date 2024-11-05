<script setup lang="ts">
import { faker } from "@faker-js/faker";
import {RobotVO, RobotUpdateForm, defaultValue, RobotForm} from "@/views/workplace/robot/RobotTypes";
import { PlatformVO } from "@/types/platformType";
import { onMounted } from "vue";
import { getRobotList, getPlatforms } from "@/api/robotApi";
import { useSnackbarStore } from "@/stores/snackbarStore";
import moment from "moment";
import {Validators} from "@/utils/validate";


var snackbarStore = useSnackbarStore();
const robotList = ref<RobotVO[]>([])
const platformList = ref<PlatformVO[]>([])
const robotForm = ref<RobotForm>({...defaultValue.defaultRobotForm})

const robotFormValidator: Validators<RobotForm> = {
  platform: (value) => platformList.some(platform=>platform.code === value) ? null : '请选择正确的平台',
  nickname: (value) => value ? null : '请输入正确的昵称',
  username: (value) => value ? null : '请输入正确的账号',
  cookie: (value) => value ? null : '请输入cookie',
};

onMounted(async ()=>{
   await getRobotList().then(res=>{
     robotList.value = res.data as RobotVO[]
   }).catch(error=>{
     snackbarStore.showErrorMessage("获取赛博游民列表，网络异常")
   })
  await getPlatforms().then(res=>{
    platformList.value = res.data as PlatformVO[]
  }).catch(error=>{
    snackbarStore.showErrorMessage("获取平台列表，网络异常")
  })
})
const chooseColor = () => {
  let colors = ["red", "indigo", "blue", "cyan", "teal"];
  let randomColor = colors[Math.floor(Math.random() * colors.length)];
  return randomColor;
};

const generateMessage = () => {
  return {
    // 生成4位id
    avatar: faker.internet.avatar(),
    username: faker.internet.userName(),
    usermail: faker.internet.email(),
    phone: faker.phone.number(),
    jdate: moment(faker.date.past()).format("YYYY/MM/DD"),
    role: faker.name.jobTitle(),
    rolestatus: chooseColor(),
  };
};

const list = () => {
  let list = [] as any[];
  list = Array.from({ length: 20 }, (value, index) => ({
    id: "#1000" + index + "",
    ...generateMessage(),
  }));
  return list;
};


const dialog = ref(false);
const search = ref("");
const rolesbg = ref(["primary", "secondary", "error", "success", "warning"]);
const desserts = ref(list());
const editedIndex = ref(-1);
const refForm = ref();
const editedItem = ref({
  id: "",
  avatar: "1.jpg",
  username: "",
  usermail: "",
  phone: "",
  jdate: "",
  role: "",
  rolestatus: "",
});
const defaultItem = ref({
  id: "",
  avatar: faker.internet.avatar(),
  username: "",
  usermail: "",
  phone: "",
  jdate: "",
  role: "",
  rolestatus: "",
});

const nameRules = [
  (v) => !!v || "Name is required",
  (v) => (v && v.length <= 10) || "Name must be less than 10 characters",
];

//Methods
const filteredList = computed(() => {
  return desserts.value.filter((user: any) => {
    return user.username.toLowerCase().includes(search.value.toLowerCase());
  });
});

function editItem(item: any) {
  editedIndex.value = desserts.value.indexOf(item);
  robotForm.value = Object.assign({}, item);
  dialog.value = true;
}
function deleteItem(item: any) {
  const index = desserts.value.indexOf(item);
  confirm("Are you sure you want to delete this item?") &&
    desserts.value.splice(index, 1);
  ``;
}

function close() {
  dialog.value = false;
  setTimeout(() => {
    editedItem.value = Object.assign({}, defaultItem.value);
    editedIndex.value = -1;
  }, 300);
}

function save() {
  if (editedIndex.value > -1) {
    Object.assign(desserts.value[editedIndex.value], editedItem.value);
  } else {
    desserts.value.push(editedItem.value);
  }
  close();
}

//Computed Property
const formTitle = computed(() => {
  return editedIndex.value === -1 ? "workplace.nomads.addRobot" : "workplace.nomads.editRobot";
});
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
                  <span class="title text-white">{{ $t(formTitle) }}</span>
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
                          variant="outlined"
                          color="primary"
                          density="compact"
                          disabled
                          v-model="updateForm.id"
                          label="Id"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6">
                        <v-text-field
                          variant="outlined"
                          color="primary"
                          density="compact"
                          :rules="nameRules"
                          :counter="10"
                          required
                          v-model="updateForm.username"
                          :label="$t('workplace.nomads.username')"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6">
                        <v-text-field
                          variant="outlined"
                          color="primary"
                          density="compact"
                          v-model="updateForm.nickname"
                          :label="$t('workplace.nomads.nickname')"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6">
                        <v-text-field
                          variant="outlined"
                          color="primary"
                          density="compact"
                          v-model="updateForm.cookie"
                          label="Cookie"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="12">
                        <v-select
                          variant="outlined"
                          color="primary"
                          density="compact"
                          :items="platformList"
                          v-model="updateForm.code"
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
                      editedItem.username == '' || editedItem.usermail == ''
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
