<template>
  <v-stepper v-model="stepStore.step">
    <template v-slot:default="{ prev, next }">
      <v-stepper-header>
        <template v-for="n in stepStore.steps" :key="`${n}-step`">
          <v-stepper-item
              :complete="stepStore.step > n"
              :step="`Step {{ n }}`"
              :value="n"
              :title="stepStore.stepList[n-1]"
          ></v-stepper-item>

          <v-divider
              v-if="n !== stepStore.steps"
              :key="n"
          ></v-divider>
        </template>
      </v-stepper-header>

      <v-stepper-window>
        <v-stepper-window-item
            v-for="n in stepStore.steps"
            :key="`${n}-content`"
            :value="n"
        >
          <LoginTokenValidStep v-if="n===1"></LoginTokenValidStep>
        </v-stepper-window-item>
      </v-stepper-window>

      <v-stepper-actions
          :disabled="stepStore.disabled"
          @click:next="next"
          @click:prev="prev"
          next-text="下一步"
          prev-text="上一步"
      ></v-stepper-actions>
    </template>
  </v-stepper>
</template>

<script setup lang="ts">
import LoginTokenValidStep from "@/views/step/compoments/LoginTokenValidStep.vue";
import {useStepStore} from "@/stores/stepStore";

const stepStore = useStepStore();


</script>

<style scoped>

</style>
