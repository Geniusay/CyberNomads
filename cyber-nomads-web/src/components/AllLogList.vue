<script setup lang="ts">
import { Icon } from "@iconify/vue";
import moment from "moment";
import {TaskLog} from "@/views/workplace/task/taskTypes";
import { getUserRecentLogs } from "@/api/taskApi"
import {useSnackbarStore} from "@/stores/snackbarStore";

const snackbarStore = useSnackbarStore()
const loading = ref(false);
const logList = ref<TaskLog[]>([])

const getAllLogs = async() =>{
  await getUserRecentLogs().then(res=>{
    logList.value = [...res.data as TaskLog[]]
  }).catch(error=>{
    snackbarStore.showErrorMessage(error.message)
  })
}

onMounted(async()=>{
  loading.value = true
  await getAllLogs();
  loading.value = false
})

const refresh = async() =>{
  loading.value = true
  await getAllLogs();
  loading.value = false
}
const convertToHtml = (text) => {
  text = text.replace(/^\[(ERROR|SUCCESS)\]\s*/, "");
  const lines = text.split("\n");

  let html = "";

  lines.forEach((line) => {
    if (line.startsWith("- ")) {
      html += `<div><span class='mr-1'>✅</span> ${line.slice(2)}</div>`;
    } else if (line.trim() === "") {
      html += "<br/>";
    } else {
      html += `<p>${line}</p>`;
    }
  });

  return html;
};

const getTagColor = (log: TaskLog) => {
  if (log.success) {
    return "green";
  } else {
    return "red";
  }
};

</script>
<template>
  <!-- loading spinner -->
  <div
    v-if="loading"
    class="h-full d-flex flex-grow-1 align-center justify-center"
  >
    <v-progress-circular indeterminate color="primary"></v-progress-circular>
  </div>
  <div id="logList" v-else>
    <h6 class="text-h6 pa-5 d-flex align-center">
      <span class="flex-fill font-weight-bold">Robot Log</span>
      <v-menu location="bottom end" transition="slide-x-transition">
        <template v-slot:activator="{ props }">
          <v-btn
            v-bind="props"
            size="small"
            variant="text"
            icon="mdi-dots-vertical"
            rounded
            color="primary"
            class="my-n2"
          ></v-btn>
        </template>
        <v-list density="compact">
          <v-list-item @click="refresh()">
            <v-list-item-title class="d-inline-flex align-center">
              <Icon
                icon="flat-color-icons:refresh"
                :rotate="2"
                :horizontalFlip="true"
                :verticalFlip="true"
                class="mr-1"
              />
              <span> Refresh</span>
            </v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
    </h6>
    <perfect-scrollbar class="timeline-container">
      <v-timeline
        class="time-line text-body-2"
        density="compact"
        side="end"
        truncate-line="start"
      >
        <v-timeline-item
          v-for="log in logList"
          :key="log.id"
          size="small"
        >
          <template v-slot:icon>
            <v-avatar>
              <img src="https://geniusserve.oss-cn-shanghai.aliyuncs.com/cybernomads/plant.ico" />
            </v-avatar>
          </template>
          <template v-slot:opposite>
            <span>{{ moment(log.createTime).format("MM,DD hh:mm") }}</span>
          </template>
          <div class="mb-1">
            <span class="text-h6 font-weight-bold">
              {{ log.robotName }}
            </span>
            <span class="ml-2 text-grey">{{log.createTime }}</span>
          </div>

          <v-card max-width="700">
            <v-card-subtitle class="pt-4">
              <v-chip
                :color="getTagColor(log)"
                size="small"
                label
                class="mr-2 font-weight-bold"
              >
                <span>{{ log.success?'SUCESS':'ERROR' }}</span>
              </v-chip>
              <span class="text-body-2">{{ log.taskName }}</span>
            </v-card-subtitle>
            <v-card-text>
              <div v-html="convertToHtml(log.content)"></div>
            </v-card-text>
          </v-card>
        </v-timeline-item>
      </v-timeline>
    </perfect-scrollbar>
  </div>
</template>

<style lang="scss" scoped>
.timeline-container {
  height: 360px;
  overflow: scroll;
}
.time-line {
  margin-left: 60px;
}
</style>
