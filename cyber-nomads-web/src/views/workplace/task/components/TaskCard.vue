<script setup lang="ts">
import {mapTaskVOToTaskForm, TaskVO} from "@/views/workplace/task/taskTypes";
import {status, buttonStatus, images, isStatusIn} from "@/views/workplace/task/taskListConfig"
import { Icon } from "@iconify/vue";
import { useTaskStore,snackbarStore } from "@/views/workplace/task/taskStore"

const taskStore = useTaskStore()
interface Props {
  item: TaskVO;
}

const props = defineProps<Props>();

const task = ref<TaskVO>({} as TaskVO)
task.value = {...props.item}

const openEditDialog = () =>{
  if(isStatusIn(props.item.taskStatus,[status.exception.value, status.running.value])){
    snackbarStore.showWarningMessage("任务正在运行中，请暂停后编辑")
    return
  }
  taskStore.taskForm = mapTaskVOToTaskForm(task.value)
  taskStore.changeDialogMode(true)
  taskStore.openDialog()
}

const deleteTaskReq = async () =>{
  if(isStatusIn(props.item.taskStatus,[status.exception.value, status.running.value])){
    snackbarStore.showErrorMessage("任务正在运行中，无法删除")
    deleteDialog.value = false;
    return
  }
  await taskStore.deleteTask(props.item)
  deleteDialog.value = false;
}

const deleteDialog = ref(false)

const openDeleteDialog = ()=>{
  deleteDialog.value = true;
}


</script>

<template>
  <v-dialog v-model="deleteDialog" max-width="700">
    <v-card>
      <v-card-title class="pa-4 bg-red">
          <span class="title text-white">
            <v-icon class="mr-2">mdi-trash-can-outline</v-icon>
           Delete Task</span>
      </v-card-title>

      <v-card-text>
        确定要删除<v-chip color="orange">{{item.taskName}}</v-chip>任务吗，这个操作无法撤回!
      </v-card-text>
      <v-divider></v-divider>
      <v-card-actions class="pa-4">
        <v-spacer></v-spacer>
        <v-btn color="gray" variant="flat" @click="deleteDialog=false">Cancel</v-btn>
        <v-btn
          color="red"
          variant="flat"
          @click="deleteTaskReq()"
        >Delete</v-btn
        >
      </v-card-actions>
    </v-card>
  </v-dialog>
  <v-card max-width="480" class="mx-auto">
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
            <v-list-item @click="openEditDialog()">
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
            <v-list-item @click="openDeleteDialog()">
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
      {{ task.taskName }}

      <v-tooltip
        location="right"
      >
        <template v-slot:activator="{ props }">
          <v-icon
            :color="status[task.taskStatus].color"
            :icon="status[task.taskStatus].icon"
            size="default"
            style="float: right"
            v-bind="props"
          >
          </v-icon>
        </template>
        <span>{{$t(status[task.taskStatus].content)}}</span>
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

            社交平台：{{task.platformCnZh}}
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
            任务类型：{{task.taskTypeCnZh}}
          </v-chip>
        </v-col>
      </v-row>
    </v-card-text>
    <v-btn style="float: right;margin: 15px" color="green" icon="mdi-play" size="x-large"></v-btn>
    <v-divider class="mx-4 mb-1"></v-divider>
    <v-card-actions>
      <v-btn color="primary">
        <v-icon class="mr-1">mdi-robot</v-icon>
        {{ task.robots.length }}
      </v-btn>
      <v-spacer></v-spacer>
      <v-btn color="primary">
        <v-icon class="mr-2">mdi-calendar</v-icon>
        {{ task.createTime }}
      </v-btn>
    </v-card-actions>
  </v-card>
</template>
