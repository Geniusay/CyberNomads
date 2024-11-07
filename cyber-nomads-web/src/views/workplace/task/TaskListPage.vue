<template>
  <v-toolbar rounded="lg" height="100" color="primary">
    <div class="ml-5">
      <h3 class="text-h5 font-weight-bold">
        Task
        <v-chip size="small" class="ma-2"> {{taskList.length}} Tasks </v-chip>
      </h3>
    </div>
    <v-spacer></v-spacer>
    <v-btn icon>
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
          xs="6"
          md="6"
          lg="3"
          xl="3
        "
          v-for="item in taskList"
          :key="item.id"
        >
          <v-card max-width="400" class="mx-auto">
            <v-img cover src="https://images.unsplash.com/photo-1585829365295-ab7cd400c167?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NXx8YXJ0aWNsZXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=600&q=60" height="200px">
              <v-toolbar color="transparent">
                <template v-slot:prepend>
                  <v-btn icon="$menu"></v-btn>
                </template>

                <v-menu>
                  <template v-slot:activator="{ props }">
                    <v-btn icon="mdi-dots-vertical" variant="text" v-bind="props"></v-btn>
                  </template>

                  <v-list density="compact">
                    <v-list-item @click="addDialog=true">
                      <v-list-item-title class="d-inline-flex align-center">
                        <Icon
                          icon="flat-color-icons:support"
                          :rotate="2"
                          :horizontalFlip="true"
                          :verticalFlip="true"
                          class="mr-1"
                        />
                        <span> Edit</span>
                      </v-list-item-title>
                    </v-list-item>
                    <v-list-item @click="$emit('delete')">
                      <v-list-item-title class="d-inline-flex align-center">
                        <Icon
                          icon="flat-color-icons:full-trash"
                          :rotate="2"
                          :horizontalFlip="true"
                          :verticalFlip="true"
                          :inline="true"
                          class="mr-1"
                        />
                        Delete</v-list-item-title
                      >
                    </v-list-item>
                  </v-list>
                </v-menu>
              </v-toolbar>
            </v-img>
            <v-card-title class="text-h6 font-weight-bold">
              {{ item.taskName }}
              <v-tooltip
                location="right"
              >
                <template v-slot:activator="{ props }">
                  <v-icon
                    :color="status[item.taskStatus].color"
                    :icon="status[item.taskStatus].icon"
                    size="default"
                    style="float: right"
                    v-bind="props"
                  >
                  </v-icon>
                </template>
                <span>{{$t(status[item.taskStatus].content)}}</span>
              </v-tooltip>

            </v-card-title>
            <v-card-text style="width:70%; overflow: hidden; margin-top: 10px;display: inline-block">
              <v-row
                align="center"
                justify="start"
              >
                <v-col

                  class="py-3 pe-0"
                  cols="auto"
                >
                  <v-chip color="rgb(38, 198, 218)">
                    <v-icon
                      icon="mdi-forum"
                      start
                    ></v-icon>

                    社交平台：{{item.platformCnZh}}
                  </v-chip>
                </v-col>
              </v-row>
              <v-row
                align="center"
                justify="start"
              >
                <v-col class="py-1 pe-0" cols="auto" >
                  <v-chip color="rgb(38, 198, 218)" >
                    <v-icon
                      icon="mdi-file-tree"
                      start
                    ></v-icon>
                    任务类型：{{item.taskTypeCnZh}}
                  </v-chip>
                </v-col>
              </v-row>
            </v-card-text>
            <v-btn style="float: right;margin: 15px" color="green" icon="mdi-play" size="x-large"></v-btn>
            <v-divider class="mx-4 mb-1"></v-divider>
            <v-card-actions>
              <v-btn color="primary">
                <v-icon class="mr-1">mdi-robot</v-icon>
                {{ item.robots.length }}
              </v-btn>
              <v-spacer></v-spacer>
              <v-btn color="primary">
                <v-icon class="mr-2">mdi-calendar</v-icon>
                {{ item.createTime }}
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
  <v-dialog
    v-model="addDialog"
    max-width="600"
  >
  </v-dialog>
</template>

<script setup lang="ts">
import { status, buttonStatus, images } from "@/views/workplace/task/TaskListConfig"
import { useSnackbarStore } from "@/stores/snackbarStore";
import { getTaskList } from "@/api/taskApi";
import { TaskVO } from "@/views/workplace/task/TaskTypes";
import { PlatformVO } from "@/types/platformType";
import {onMounted} from "vue";
import { useCommonStore } from "@/stores/commonStore";
import { Icon } from "@iconify/vue";

const commonStore = useCommonStore();
const snackbarStore = useSnackbarStore();
const taskList = ref<TaskVO[]>([])
const addDialog = ref(false)
const platformList = ref<PlatformVO[]>([])

const getTaskListReq = async()=>{
  await getTaskList().then(res=>{
    taskList.value = (res.data as TaskVO[]).map(task => ({
      ...task,
      taskStatus: task.taskStatus.toLowerCase()
    }));
  }).catch(error=>{
    snackbarStore.showErrorMessage(error)
  })
}

onMounted(async()=>{
  await getTaskListReq()
  await commonStore.initPlatformsVO()
  platformList.value = commonStore.getPlatformList
})

</script>

<style scoped>

</style>
