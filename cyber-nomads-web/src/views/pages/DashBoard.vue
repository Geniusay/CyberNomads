<script setup lang="ts">
import SalesCard from "@/components/dashboard/SalesCard.vue";
import LogList from "@/components/AllLogList.vue";
import TableCard from "@/components/dashboard/TableCard.vue";
import TicketsCard from "@/components/dashboard/TicketsCard.vue";
import TodoCard from "@/components/dashboard/TodoCard.vue";
import TaskCard from "@/components/dashboard/ChartCard.vue";
import NomadsTablePage from "@/views/workplace/robot/NomadsTablePage.vue"
import { driver } from "driver.js";
import {tutorial} from "@/utils/tutorial";

onMounted(()=>{
  tutorial.newcomerGuidance();
})

</script>
<template>
  <div class="pa-5" id="dashboard">
    <!-- ---------------------------------------------- -->
    <!---First Row -->
    <!-- ---------------------------------------------- -->
    <v-row class="flex-0" dense>
      <v-col  cols="12" md="6" xl="6">
        <!-- Sales Card -->
        <v-card class="card-shadow" height="420">
          <!-- <sales-card></sales-card> -->
          <sales-card
            :value="0"
            class="h-100"
            :percentage="0.0"
            style="min-height: 380px"
            :percentage-label="$t('dashboard.lastweek')"
            :action-label="$t('dashboard.viewReport')"
          ></sales-card>
        </v-card>
      </v-col>
      <v-col  cols="12" md="6" xl="6">
        <!-- Activity Card -->
        <v-card  class="card-shadow" height="420">
          <LogList />
        </v-card>
      </v-col>
    </v-row>
    <!-- ---------------------------------------------- -->
    <!---Second Row -->
    <!-- ---------------------------------------------- -->
    <v-row  class="card-shadow flex-grow-0" dense>
      <v-col cols="12" xl="12">
        <!-- Target Card-->
        <v-card id="nomadsTablePage" class="card-shadow" min-height="420" max-height="600">
          <NomadsTablePage />
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>
<style lang="scss" scoped></style>
