<template>
  <v-card
    prepend-icon="mdi-account"
    :title="taskStore.getIsEdit?'Edit Task':'Add Task'"
  >
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
          ></v-select>
        </v-col>

        <v-col
          cols="12"
          sm="12"
        >
          <v-select
            v-model="taskStore.getTaskForm.robotIds"
            :items="robotList"
            label="Robot"
            item-title="nickname"
            item-value="id"
            chips
            multiple
            variant="outlined"
          ></v-select>
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
          ></v-select>
        </v-col>

<!--        <small class="text-caption text-medium-emphasis">任务参数</small>-->
        <ParamsInput
          v-if="!!taskStore.taskForm.taskType"
          :params="childParams()?.params ?? []"
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
        color="success"
        text="Save"
        variant="tonal"
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
import {createTask, updateTask} from "@/api/taskApi"
import { defaultValue } from "@/views/workplace/task/taskTypes"
import { TaskVO } from "@/views/workplace/task/taskTypes"
import {RobotVO} from "@/views/workplace/robot/robotTypes";

import {useRobotStore} from "@/views/workplace/robot/robotStore";

const commonStore = useCommonStore();
const taskStore = useTaskStore()
const platformList = commonStore.getPlatformList as PlatformVO[]
const taskTypes = ref([])
const robotList = ref<RobotVO[]>([])
const robotStore = useRobotStore();

onMounted(async ()=>{
  const platform = taskStore.taskForm.platform
  console.log("初始化"+platform)
  if(!!platform){
    await taskStore.initPlatformTaskTypes(platform)
  }
  robotList.value = robotStore.getRobotList
})

const submit = async () =>{
  const taskForm = {...taskStore.taskForm}
  if(!taskStore.isEdit){
    await taskStore.createTask(taskForm)
  }else{
    await taskStore.updateTask(taskForm)
  }

  taskStore.taskForm = {...defaultValue.defaultTaskForm}
  taskStore.closeDialog()
}

const close = () =>{
  taskStore.taskForm = {...defaultValue.defaultTaskForm}
  taskStore.closeDialog()
}

const childParams = () =>{
  console.log(taskStore.platformTaskTypeMap)
  console.log(taskStore.taskForm.platform)
  console.log(taskStore.getPlatformTaskTypes(taskStore.taskForm.platform))
  return taskStore.getPlatformTaskTypes(taskStore.taskForm.platform).find(param=>param.taskTypeKey===taskStore.taskForm.taskType)
}

const required = (v,required) =>{
  return required && !v ? 'This field is required' : true;
}

watch(
  () => taskStore.taskForm.platform,  // 监听 platform 的值
  async (newValue, oldValue) => {
    await taskStore.initPlatformTaskTypes(newValue)
    taskStore.taskForm.taskType = ""
  }
);

</script>
