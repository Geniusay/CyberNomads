<template>
  <v-card
    prepend-icon="mdi-account"
    :title="taskStore.getIsEdit?'Edit Task':'Add Task'"
  >
    <v-card-text>
      <v-row dense>
        <v-col
          cols="12"
          md="12"
        >
          <v-text-field
            :label="$t('workplace.task.taskName')"
            required
          ></v-text-field>
        </v-col>

        <v-col
          cols="12"
          sm="12"
        >
          <v-select
            variant="outlined"
            color="primary"
            density="compact"
            :items="platformList"
            v-model="taskStore.getTaskForm.platform"
            :label="$t('workplace.task.platform')"
            item-title="platformCnZh"
            item-value="platform"
          ></v-select>
        </v-col>

        <v-col
          cols="12"
          sm="12"
        >
          <v-select
            variant="outlined"
            color="primary"
            density="compact"
            :items="taskTypes"
            v-model="taskStore.getTaskForm.taskType"
            :label="$t('workplace.task.taskType')"
            item-title="taskTypeValue"
            item-value="taskTypeKey"
          ></v-select>
        </v-col>

        <small class="text-caption text-medium-emphasis">任务参数</small>
        <ParamsInput
          v-if="!!taskStore.getTaskForm.taskType"
          :params="childParams(taskStore.getTaskForm.taskType)"
        />
      </v-row>

    </v-card-text>

    <v-divider></v-divider>

    <v-card-actions>
      <v-spacer></v-spacer>

      <v-btn
        text="Close"
        variant="plain"
        @click="submit()"
      ></v-btn>

      <v-btn
        color="primary"
        text="Save"
        variant="tonal"
        @click="close()"
      ></v-btn>
    </v-card-actions>
  </v-card>
</template>

<script setup lang="ts">
import ParamsInput from "@/views/workplace/task/components/ParamsInput.vue"
import { useTaskStore,snackbarStore } from "@/views/workplace/task/taskStore"
import { useCommonStore } from "@/stores/commonStore";
import { PlatformVO } from "@/types/platformType";
import {onMounted} from "vue";

const commonStore = useCommonStore();
const taskStore = useTaskStore()
const platformList = commonStore.getPlatformList as PlatformVO[]
const taskTypes = ref([])

onMounted(async ()=>{
  const platform = taskStore.taskForm.platform
  if(!!platform){
    await taskStore.initPlatformTaskTypes(platform)
    taskTypes.value = taskStore.getPlatformTaskTypes(platform)
  }
})

const submit =() =>{
  taskStore.closeDialog()
}

const close = () =>{
  taskStore.closeDialog()
}

const childParams = (taskType) =>{
  const childParam = taskTypes.value.find(param=>param.taskTypeKey===taskType)
  console.log(childParam.params)
  return childParam.params
}

watch(
  () => taskStore.taskForm.platform,  // 监听 platform 的值
  async (newValue, oldValue) => {
    await taskStore.initPlatformTaskTypes(newValue)
    taskTypes.value = taskStore.getPlatformTaskTypes(newValue)
  }
);

</script>
