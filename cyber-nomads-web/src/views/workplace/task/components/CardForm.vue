<template>
  <v-card>
    <v-card-title :class="'pa-4 bg-'+cardHeadConfig.color">
          <span class="title text-white">
            <v-icon class="mr-2">mdi-script-text</v-icon>
           {{cardHeadConfig.title}}</span>
    </v-card-title>

    <v-card-text>
      <v-row dense>
        <v-col
          cols="12"
          sm="12"
        >
          <v-text-field
            :label="$t('workplace.task.taskName')"
            v-model="taskStore.getTaskForm.taskName"
            variant="outlined"
            :rules="[required(taskStore.getTaskForm.taskName, true)]"
            :disabled="taskStore.viewMode"
            required
          ></v-text-field>
        </v-col>

        <v-col
          cols="12"
          sm="12"
        >
          <v-select
            color="primary"
            density="compact"
            :items="platformList"
            v-model="taskStore.getTaskForm.platform"
            :rules="[required(taskStore.getTaskForm.platform, true)]"
            :label="$t('workplace.task.platform')"
            item-title="platformCnZh"
            item-value="platform"
            variant="outlined"
            :disabled="taskStore.viewMode"
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
              <v-list-item v-bind="props">
                <v-chip>
                  <v-icon>
                    <img :src="commonStore.getPlatformImgUrl(item.raw.platform)" alt="My Icon" />
                  </v-icon>
                  {{' '+item.raw.platformCnZh}}
                </v-chip>
              </v-list-item>
            </template>
          </v-select>
        </v-col>

        <v-col
          cols="12"
          sm="12"
        >
          <v-autocomplete
            v-model="taskStore.getTaskForm.robotIds"
            :disabled="taskStore.viewMode"
            :items="robotList"
            label="Robot"
            item-title="nickname"
            item-value="id"
            variant="outlined"
            chips
            closable-chips
            multiple
          >
            <template v-slot:chip="{ props, item }">
              <v-chip
                style="font-size: 1.1em"
                v-bind="props"
                color="#d70e76"
                :text="item.raw.nickname"
              ></v-chip>
            </template>

            <template v-slot:item="{ props, item }">
              <v-list-item
                v-bind="props"
              >
                <v-chip  style="font-size: 1.1em">
                  <v-icon>
                    <img :src="commonStore.getPlatformImgUrl(item.raw.platform)" alt="My Icon" />
                  </v-icon>
                  {{item.raw.nickname}}
                </v-chip>
              </v-list-item>
            </template>
          </v-autocomplete>
        </v-col>

        <v-col
          cols="12"
          sm="12"
        >
          <v-select
            color="primary"
            density="compact"
            :items="taskTypes"
            v-model="taskStore.taskForm.taskType"
            :label="$t('workplace.task.taskType')"
            :rules="[required(taskStore.getTaskForm.taskType, true)]"
            item-title="taskTypeValue"
            item-value="taskTypeKey"
            variant="outlined"
            :disabled="taskStore.viewMode"
          ></v-select>
        </v-col>

<!--        <small class="text-caption text-medium-emphasis">任务参数</small>-->
        <ParamsInput
          v-if="!!taskStore.taskForm.taskType"
          :params="childParams(taskStore.taskForm.taskType)?.params ?? []"
          :key="taskStore.taskForm.taskType"
        />
      </v-row>

    </v-card-text>

    <v-divider></v-divider>

    <v-card-actions>
      <v-spacer></v-spacer>

      <v-btn
        text="Close"
        variant="plain"
        @click="close()"
      ></v-btn>

      <v-btn
        v-if="!taskStore.viewMode"
        color="success"
        text="Save"
        variant="tonal"
        :disabled="loading"
        :loading="loading"
        @click="submit()"
      ></v-btn>
    </v-card-actions>
  </v-card>
</template>

<script setup lang="ts">
import ParamsInput from "@/views/workplace/task/components/ParamsInput.vue"
import { useTaskStore,snackbarStore } from "@/views/workplace/task/taskStore"
import { useCommonStore } from "@/stores/commonStore";
import { PlatformVO } from "@/types/platformType";
import { onMounted } from "vue";
import {defaultValue, TaskForm} from "@/views/workplace/task/taskTypes"
import { RobotVO } from "@/views/workplace/robot/robotTypes";
import {useRobotStore} from "@/views/workplace/robot/robotStore";
import {validateAndReturn, Validators} from "@/utils/validate";
import {validateAndGetParams} from "@/views/workplace/task/taskListConfig";

const commonStore = useCommonStore();
const taskStore = useTaskStore()
const platformList = commonStore.getPlatformList as PlatformVO[]
const taskTypes = ref([])
const robotList = ref<RobotVO[]>([])
const robotStore = useRobotStore();
const loading = ref(false)
const cardHeadConfig = ref({
  title:"",
  color:"green"
})

const taskFormValidator: Validators<TaskForm> = {
  taskId: (value)=>value?null:"不正确的任务ID",
  platform: (value) => platformList.some(platform=>platform.platform === value) ? null : '请选择正确的平台',
  taskName: (value) => value ? null : '请输入正确的任务名',
  taskType: (value) => value ? null : '请选择正确的任务类型',
  params:(value) => {
    const msg = validateAndGetParams(value, childParams(taskStore.taskForm.taskType)?.params)
    return !msg?null:msg
  },
  robotIds:(value) => null,
};

onMounted(async ()=>{
  const platform = taskStore.taskForm.platform
  if(!!platform){
    await taskStore.initPlatformTaskTypes(platform)
    taskTypes.value = taskStore.getPlatformTaskTypes(platform)
  }
  robotList.value = robotStore.getRobotList
  initHeadConfig()
})

const initHeadConfig = () =>{
  if(taskStore.viewMode){
    cardHeadConfig.value = {title:"Task Detail", color:"grey"}
  }else{
    cardHeadConfig.value = taskStore.getIsEdit?{title:'Edit Task', color:"orange"} :{title:'Add Task', color:"green"}
  }

}

const submit = async () =>{
  const taskForm = {...taskStore.taskForm}
  const error = validateAndReturn(taskStore.isEdit?["taskName","platform","taskType","taskId","params"]:["taskName","platform","taskType","params"], taskForm, taskFormValidator)
  if(!!error){
    snackbarStore.showErrorMessage(error)
    return
  }
  loading.value = true
  if(!taskStore.isEdit){
    await taskStore.createTask(taskForm)
  }else{
    await taskStore.updateTask(taskForm)
  }
  loading.value = false
  taskStore.taskForm = {...defaultValue.defaultTaskForm}
  taskStore.closeDialog()

}

const close = () =>{
  taskStore.taskForm = {...defaultValue.defaultTaskForm}
  taskStore.closeDialog()
}

const childParams = (taskType) =>{
  return taskTypes.value.find(param=>param.taskTypeKey===taskType)
}

const required = (v,required) =>{
  return required && !v ? 'This field is required' : true;
}

watch(
  () => taskStore.taskForm.platform,  // 监听 platform 的值
  async (newValue, oldValue) => {
    await taskStore.initPlatformTaskTypes(newValue)
    taskStore.taskForm.taskType = ""
    taskTypes.value = taskStore.getPlatformTaskTypes(newValue)
  }
);

</script>
