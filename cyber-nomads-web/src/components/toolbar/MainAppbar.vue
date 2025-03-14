<!--
* @Component:
* @Maintainer: J.K. Yang
* @Description:
-->
<script setup lang="ts">
import { useDisplay } from "vuetify";
import { useCustomizeThemeStore } from "@/stores/customizeTheme";
import LanguageSwitcher from "@/components/toolbar/LanguageSwitcher.vue";
import ToolbarNotifications from "./ToolbarNotifications.vue";
import ToolbarUser from "./ToolbarUser.vue";
import { useTodoStore } from "@/views/app/todo/todoStore";
import ThemeToggle from "./ThemeToggle.vue";
import {useSnackbarStore} from "@/stores/snackbarStore";
import Tutorial from "@/components/toolbar/Tutorial.vue";
const { mdAndUp } = useDisplay();
const todoStore = useTodoStore();
const customizeTheme = useCustomizeThemeStore();
const showMobileSearch = ref(false);

var snackbarStore = useSnackbarStore();
const todoEvent = ()=>{
  snackbarStore.showInfoMessage("Todo清单正在开发中，敬请期待~")
}

const groupEvent = ()=>{
  snackbarStore.showInfoMessage("好友系统正在开发中，敬请期待~")
}
</script>

<template>
  <!-- ---------------------------------------------- -->
  <!--App Bar -->
  <!-- ---------------------------------------------- -->
  <v-app-bar :density="mdAndUp ? 'default' : 'compact'">
    <!-- ---------------------------------------------- -->
    <!-- search input mobil -->
    <!-- ---------------------------------------------- -->
    <div class="d-flex flex-fill align-center" v-if="showMobileSearch">
      <v-text-field
        color="primary"
        variant="solo"
        prepend-inner-icon="mdi-magnify"
        append-inner-icon="mdi-close"
        @click:append-inner="showMobileSearch = false"
        hide-details
        placeholder="Search"
      ></v-text-field>
    </div>
    <div v-else class="px-2 d-flex align-center justify-space-between w-100">
      <!-- ---------------------------------------------- -->
      <!-- NavIcon -->
      <!-- ---------------------------------------------- -->
      <v-app-bar-nav-icon
        @click="customizeTheme.mainSidebar = !customizeTheme.mainSidebar"
      ></v-app-bar-nav-icon>
      <div>
        <v-text-field
          v-if="mdAndUp"
          class="ml-2"
          style="width: 400px"
          color="primary"
          variant="solo"
          density="compact"
          prepend-inner-icon="mdi-magnify"
          hide-details
          placeholder="Search"
        ></v-text-field>
      </div>

      <v-spacer></v-spacer>

      <div class="d-flex">
        <v-btn v-if="!mdAndUp" icon @click="showMobileSearch = true">
          <v-icon>mdi-magnify</v-icon>
        </v-btn>
        <!-- search input desktop -->
        <Tutorial/>
        <ToolbarNotifications />
        <v-btn v-if="mdAndUp" @click="groupEvent()" icon>
          <v-badge dot color="success">
            <v-icon>mdi-account-multiple-outline</v-icon>
          </v-badge>
        </v-btn>
        <v-btn v-if="mdAndUp" icon @click="todoEvent()">
          <v-icon>mdi-calendar-check</v-icon>
        </v-btn>
        <v-divider vertical thickness="2" inset class="ml-5 mr-1"></v-divider>
        <ThemeToggle />
        <LanguageSwitcher/>
        <ToolbarUser />
      </div>
    </div>
  </v-app-bar>
</template>

<style scoped lang="scss"></style>
