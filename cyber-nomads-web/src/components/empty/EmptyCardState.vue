<template>
  <v-row align="center">
    <v-col
      cols="12"
      xs="12"
      md="12"
      lg="6"
      xl="3"
      v-for="(item,index) in emptyCard"
      :key="index"
    >
      <v-skeleton-loader
        class="mx-auto border"
        max-width="480"
        type="image, article, actions"
      ></v-skeleton-loader>
    </v-col>
  </v-row>
</template>

<script setup lang="ts">
const emptyCard = [1,2,3,4,5,6,7]
</script>
