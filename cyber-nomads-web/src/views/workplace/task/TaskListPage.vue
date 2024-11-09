<template>
  <v-toolbar rounded="lg" height="100" color="primary">
    <div class="ml-5">
      <h3 class="text-h5 font-weight-bold">
        Task
        <v-chip size="small" class="ma-2"> {{taskList.length}} Tasks </v-chip>
      </h3>
    </div>
    <v-spacer></v-spacer>
    <v-btn @click="openAddDialog()" icon>
      <v-icon>mdi-table-plus</v-icon>
    </v-btn>
    <v-btn icon>
      <v-icon>mdi-dots-vertical</v-icon>
    </v-btn>
  </v-toolbar>
  <div>
    <v-container>
      <v-row align="center">
        <v-col
          cols="12"
          xs="12"
          md="12"
          lg="3"
          xl="3
        "
          v-for="item in taskList"
          :key="item.id"
        >
          <TaskCard :item="item"/>
        </v-col>
      </v-row>
    </v-container>
  </div>
  <v-dialog
    v-model="taskStore.getTaskDialog"
    max-width="600"
  >
    <CardForm/>
  </v-dialog>

</template>

<script setup lang="ts">
import CardForm from "@/views/workplace/task/components/CardForm.vue";
import TaskCard from "@/views/workplace/task/components/TaskCard.vue";
import { TaskVO } from "@/views/workplace/task/taskTypes";
import { PlatformVO } from "@/types/platformType";
import { onMounted } from "vue";
import { useCommonStore } from "@/stores/commonStore";
import { useTaskStore,snackbarStore } from "@/views/workplace/task/taskStore"
import {useRobotStore} from "@/views/workplace/robot/robotStore";


const taskStore = useTaskStore()
const commonStore = useCommonStore();
const taskList = ref<TaskVO[]>([])
const platformList = ref<PlatformVO[]>([])
const robotStore = useRobotStore();

onMounted(async()=>{
  await taskStore.initTaskList()
  await commonStore.initPlatformsVO()
  await robotStore.initRobotList()
  platformList.value = commonStore.getPlatformList as PlatformVO[]
  taskList.value = taskStore.getTaskList.value as TaskVO[]
})

const openAddDialog = ()=>{
  taskStore.taskForm.platform = platformList.value[0].platform
  taskStore.changeDialogMode(false)
  taskStore.openDialog()
}

watch(
  taskStore.taskList,
  (newList, oldList)=>{
    console.log(newList)
    taskList.value = newList as TaskVO[]
    console.log(taskList.value)
  }
)
</script>


<style scoped>

</style>
