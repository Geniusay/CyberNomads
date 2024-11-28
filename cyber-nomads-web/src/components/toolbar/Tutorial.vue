<template>
  <v-menu location="bottom right" transition="slide-y-transition">
    <!-- ---------------------------------------------- -->
    <!-- Activator Btn -->
    <!-- ---------------------------------------------- -->
    <template v-slot:activator="{ props }">
      <v-btn @click="openGuidance()" id="tutorial" icon v-bind="props" class="text-none">
        <!--        <v-badge content="2" color="error">-->
        <!--         -->
        <!--        </v-badge>-->
        <v-icon>mdi-help-circle-outline</v-icon>
      </v-btn>
    </template>
  </v-menu>
</template>

<script setup lang="ts" >
import {tutorial} from "@/utils/tutorial";
const route = useRouter()

const openGuidance = () =>{
  if(route.path==="/workplace/dashboard"){
    tutorial.dashBoard.drive()
  }else{
    tutorial.dashBoard.drive()
  }
}
</script>
