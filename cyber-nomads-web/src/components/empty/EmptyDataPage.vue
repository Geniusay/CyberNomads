<template>
  <v-empty-state style="margin-top: 5%; margin-bottom: 5%" :image="errorImg">
    <template v-slot:title>
      <div class="text-subtitle-1 mt-4" style="font-size:1.1em!important;">
        {{$t(title)}}
      </div>
    </template>

    <template v-slot:text>
      <div class="text-caption" style="font-size:1.0em!important;">
        {{$t(content)}}
      </div>
    </template>

    <template v-slot:actions>
      <v-btn
        class="text-none"
        elevation="1"
        rounded="lg"
        size="default"
        :text="$t(btnText)"
        @click="handler()"
        max-width="200"
      ></v-btn>
    </template>
  </v-empty-state>
</template>

<script setup lang="ts">

interface Props{
  title: string,
  content: string,
  handler: ()=>void,
  btnText: string,
  errorImg: string,
}

const props = defineProps<Props>();
</script>
