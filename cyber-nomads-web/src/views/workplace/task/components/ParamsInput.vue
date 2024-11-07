<template>
  <v-col v-for="param in params" :key="param.name"
         cols="12"
         md="12"
  >
    <!-- 判断 childParams 是否为空 -->
    <v-text-field
      v-if="param.childParams.length === 0"
      :label="param.desc"
      v-model="taskStore.taskForm[param.name]"
    ></v-text-field>

    <v-select
      v-if="param.childParams.length !== 0"
      variant="outlined"
      color="primary"
      density="compact"
      :items="param.childParams"
      v-model="taskStore.taskForm[param.name]"
      :label="param.desc"
      item-title="desc"
      item-value="name"
    ></v-select>

    <!-- 如果 childParams 不为空，递归调用 ParamInput 组件 -->
    <ParamsInput
      v-if="param.childParams.length > 0 && !!taskStore.taskForm[param.name]"
      :params="childParams(taskStore.taskForm[param.name], param.childParams)"
    />
  </v-col>
</template>

<script setup lang="ts">
import { useTaskStore,snackbarStore } from "@/views/workplace/task/taskStore"

const taskStore = useTaskStore()


interface Props {
  params: { };
}
const props = defineProps<Props>();
onMounted(()=>{
  console.log(props.params)
})
const childParams = (paramName, childParamsList) =>{
  console.log(childParamsList)
  return [childParamsList.find(param=>param.name===paramName)]
}


</script>
