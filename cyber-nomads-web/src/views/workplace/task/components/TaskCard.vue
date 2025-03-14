<script setup lang="ts">
import { mapTaskVOToTaskForm, TaskVO } from "@/views/workplace/task/taskTypes";
import {status, images, isStatusIn, getButtonStatus} from "@/views/workplace/task/taskListConfig"
import { useCommonStore } from "@/stores/commonStore";
import { Icon } from "@iconify/vue";
import { useTaskStore,snackbarStore } from "@/views/workplace/task/taskStore"
import { getRandomColor } from "@/utils/toolUtils";
import LogList  from "@/views/workplace/task/components/LogList.vue"

const loading = ref(false)
const taskStore = useTaskStore()
const commonStore = useCommonStore()

interface Props {
  item: TaskVO;
  index: number
}

const props = defineProps<Props>();
const button = ref(getButtonStatus(props.item.taskStatus))

const textColor = getRandomColor()
const task = ref<TaskVO>({} as TaskVO)
task.value = {...props.item}

const openViewDialog = () =>{
  taskStore.taskForm = mapTaskVOToTaskForm(task.value)
  taskStore.openViewDialog()
}

const getImg = () =>{
  return images[(props.index)%(images.length-1)]
}

const openEditDialog = () =>{

  if(isStatusIn(task.value.taskStatus,[status.exception.value, status.running.value])){
    snackbarStore.showWarningMessage("任务正在运行中，请暂停后编辑")
    return
  }
  taskStore.taskForm = mapTaskVOToTaskForm(task.value)
  taskStore.changeDialogMode(true)
  taskStore.openDialog()
}

const deleteTaskReq = async () =>{
  loading.value = true
  if(isStatusIn(task.value.taskStatus,[status.exception.value, status.running.value])){
    snackbarStore.showErrorMessage("任务正在运行中，无法删除")
    deleteDialog.value = false;
    return
  }
  loading.value = false
  await taskStore.deleteTask(props.item)
  deleteDialog.value = false;
}

const deleteDialog = ref(false)

const openDeleteDialog =()=>{
  deleteDialog.value = true;
}

const changeTaskDialog = ref(false)

const openChangeTaskStatusDialog = async()=>{
  changeTaskDialog.value = true;
}

const changeTaskStatusByButton = async() =>{
  const currentStatus = status[task.value.taskStatus]
  await changeTaskStatus(task.value, currentStatus.next, button.value.next, button.value.nextMsg)
}

const changeTaskStatus = async(task, next, action, msg)=>{
  loading.value = true
  await taskStore.changeStatus(task, next, action, msg)
  loading.value = false
  button.value = getButtonStatus(task.taskStatus)
  changeTaskDialog.value = false;
}

const options = [
  {
    icon:"flat-color-icons:settings",
    name:"Setting",
    handler:openViewDialog
  },
  {
    icon:"flat-color-icons:support",
    name:"Edit",
    handler:openEditDialog
  },
  {
    icon:"flat-color-icons:full-trash",
    name:"Delete",
    handler:openDeleteDialog
  }
]

const logDialog = ref(false)

const openLogDialog = async()=>{
  logDialog.value = true
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
          :disabled="loading"
          :loading="loading"
          @click="deleteTaskReq()"
        >Delete</v-btn
        >
      </v-card-actions>
    </v-card>
  </v-dialog>

  <v-dialog v-model="changeTaskDialog" max-width="700">
    <v-card>
      <v-card-title class="pa-4 bg-pink">
          <span class="title text-white">
            <v-icon class="mr-2">mdi-cog</v-icon>
           {{button.nextMsg}}
          </span>
      </v-card-title>

      <v-card-text>
        确定要{{button.nextMsg}}<v-chip color="orange">{{task.taskName}}</v-chip>任务吗?
      </v-card-text>

      <v-card-actions class="pa-4">
        <v-spacer></v-spacer>
        <v-btn color="gray" variant="flat" @click="changeTaskDialog=false">Cancel</v-btn>
        <v-btn
          color="green"
          variant="flat"
          :disabled="loading"
          :loading="loading"
          @click="changeTaskStatusByButton()"
        >Confirm</v-btn
        >
      </v-card-actions>
    </v-card>
  </v-dialog>

  <v-dialog v-model="logDialog" max-width="900">
    <v-card>
      <LogList :task="item"/>
      <v-divider></v-divider>
      <v-card-actions class="pa-4">
        <v-spacer></v-spacer>
        <v-btn color="gray" variant="flat" @click="logDialog=false">Close</v-btn>
      </v-card-actions>
    </v-card>

  </v-dialog>


  <v-card max-width="480" class="mx-auto">
    <v-img cover :src="getImg()" height="200px">
      <v-toolbar color="transparent">
        <template v-slot:prepend>
          <v-menu>
            <template v-slot:activator="{ props }">
              <v-btn icon="$menu" v-bind="props"></v-btn>
            </template>

            <v-list density="compact">
              <v-list-item @click="openLogDialog()">
                <v-list-item-title class="d-inline-flex align-center">
                  <Icon
                    icon="flat-color-icons:sms"
                    :rotate="2"
                    :horizontalFlip="true"
                    :verticalFlip="true"
                    :inline="true"
                    class="mr-1"
                  />
                  {{$t(`workplace.task.log`)}}</v-list-item-title
                >
              </v-list-item>
            </v-list>
          </v-menu>
        </template>

        <v-menu>
          <template v-slot:activator="{ props }">
            <v-btn icon="mdi-dots-vertical" variant="text" v-bind="props"></v-btn>
          </template>

          <v-list density="compact">
            <v-list-item v-for="option in options" @click="option.handler()">
              <v-list-item-title class="d-inline-flex align-center">
                <Icon
                  :icon="option.icon"
                  :rotate="2"
                  :horizontalFlip="true"
                  :verticalFlip="true"
                  class="mr-1"
                />
                <span> {{ option.name }}</span>
              </v-list-item-title>
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
          <Icon
            :color="status[task.taskStatus].color"
            :icon="status[task.taskStatus].icon"
            style="float: right; font-size: 1.9rem"
            v-bind="props"
          >
          </Icon>
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
          <v-chip color="#448ac4">
            <v-icon >
              <img :src="commonStore.getPlatformImgUrl(task.platform)" alt="My Icon" start />
            </v-icon>
            社交平台： {{task.platformCnZh}}
          </v-chip>
        </v-col>
      </v-row>
      <v-row
        align="center"
        justify="start"
      >
        <v-col class="py-1 pe-0" cols="auto" >
          <v-chip color="#448ac4" >
            <v-icon
              icon="mdi-file-tree"
              start
            ></v-icon>
            任务类型：{{task.taskTypeCnZh}}
          </v-chip>
        </v-col>
      </v-row>
    </v-card-text>
    <v-btn @click="openChangeTaskStatusDialog()" style="float: right;margin: 15px" :color="button.color" :icon="button.icon" size="x-large"></v-btn>
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
