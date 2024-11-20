<template>
  <v-card style="height: 200%;">
    <v-card-title class="font-weight-bold">
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
    </v-card-title>
    <div>
      <EmptyDataPage
      v-if="emptyState"
      :title="'workplace.task.emptyTaskTitle'"
      :content="'workplace.task.emptyTaskContent'"
      :handler=openAddDialog
      :btnText="'workplace.task.addTask'"
      :errorImg="'/assets/img/empty_tips.png'"
      ></EmptyDataPage>
      <v-container v-else>
        <EmptyCardState v-if="pageLoading"></EmptyCardState>
        <v-row v-else align="center">
          <v-col
            cols="12"
            xs="12"
            md="12"
            lg="3"
            xl="3
        "
            v-for="(item,index) in taskList"
            :key="item.id"
          >
            <TaskCard :item="item" :index="randomSeed+index" :key="item"/>
          </v-col>
        </v-row>
      </v-container>
    </div>
  </v-card>
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
import { useTaskStore } from "@/views/workplace/task/taskStore"
import {useRobotStore} from "@/views/workplace/robot/robotStore";
import {images} from "@/views/workplace/task/taskListConfig";
import EmptyCardState from "@/components/empty/EmptyCardState.vue";
import EmptyDataPage from "@/components/empty/EmptyDataPage.vue";

const taskStore = useTaskStore()
const commonStore = useCommonStore();
const taskList = ref<TaskVO[]>([])
const platformList = ref<PlatformVO[]>([])
const robotStore = useRobotStore();
const randomSeed = Math.floor(Math.random() * images.length);
const emptyState = ref(false)
const pageLoading = ref(true)

onMounted(async()=>{
  await taskStore.initTaskList()
  await commonStore.initPlatformsVO()
  await robotStore.initRobotList()
  platformList.value = commonStore.getPlatformList as PlatformVO[]
  taskList.value = taskStore.getTaskList.value as TaskVO[]
  emptyState.value = taskList.value.length==0;
  pageLoading.value = false
})

const openAddDialog = ()=>{
  taskStore.taskForm.platform = platformList.value[0].platform
  taskStore.changeDialogMode(false)
  taskStore.openDialog()
}

watch(
  ()=>taskStore.taskList,
  (newList, oldList)=>{
    console.log(newList)
    taskList.value = taskStore.taskList.value
    emptyState.value = taskList.value.length==0;
  },
  { deep: true }
)
</script>


<style scoped>

</style>
