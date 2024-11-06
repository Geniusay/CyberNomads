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
            <v-img cover :src="item.image" height="200px"></v-img>
            <v-card-title class="text-h6 font-weight-bold">
              {{ item.title }}
              <v-tooltip
                location="right"
              >
                <template v-slot:activator="{ props }">
                  <v-icon
                    :color="status[item.status].color"
                    :icon="status[item.status].icon"
                    size="default"
                    style="float: right"
                    v-bind="props"
                  >
                  </v-icon>
                </template>
                <span>{{$t(status[item.status].content)}}</span>
              </v-tooltip>

            </v-card-title>
            <v-card-text style="margin-top: 10px;display: inline-block">
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
                <v-col class="py-1 pe-0" cols="auto">
                  <v-chip color="rgb(38, 198, 218)">
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
                {{ item.author }}
              </v-btn>
              <v-spacer></v-spacer>
              <v-btn color="primary">
                <v-icon class="mr-2">mdi-calendar</v-icon>
                {{ item.createAt }}
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>

</template>

<script setup lang="ts">

const status = {
  error:{
    icon: "mdi-alert-circle",
    color:"orange",
    content:"workplace.task.error"
  },
  pending:{
    icon: "mdi-alert-circle",
    color:"grey",
    content:"workplace.task.pending"
  },
  running:{
    icon:"mdi-arrow-right-drop-circle",
    color:"green",
    content:"workplace.task.running"
  },
  pause:{
    icon:"mdi-pause-circle",
    color:"yellow",
    content:"workplace.task.pause"
  },
  finish:{
    icon:"mdi-check-circle",
    color:"green",
    content:"workplace.task.finish"
  }
}

const buttonStatus = {
  ready:{
    icon: "mdi-play",
    color: "green",
  },
  running:{
    icon: "mdi-pause",
    color: "red",
  },
  pause:{
    icon: "mdi-reload",
    color: "grey",
  },
  stop:{
    icon: "mdi-reload",
    color: "grey",
  },
}

const taskList = [
  {
    id: 1,
    title: "任务测试",
    status: "pending",
    taskTypeCnZh:"定时任务",
    platformCnZh:"哔哩哔哩",
    image:
      "https://images.unsplash.com/photo-1585829365295-ab7cd400c167?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NXx8YXJ0aWNsZXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=600&q=60",
    createAt: "4/22/2022",
    lastReadAt: "1/13/2023",
    author: "5",
    content:
      "",
  },
];
</script>

<style scoped>

</style>
