<template>
  <v-row dense>
    <v-col v-for="(param, index) in params" :key="param.name"
           :cols=computeInputWidth(param,index)
           :md=computeInputWidth(param,index)
    >
      <!-- 判断 childParams 是否为空 -->
      <v-text-field
        v-if="param.selection.length === 0"
        :label="param.desc"
        v-model="taskStore.taskForm.params[param.name]"
        :required="param.required"
        variant="outlined"
      ></v-text-field>

      <v-select
        v-if="param.selection.length !== 0"
        color="primary"
        density="compact"
        :items="param.selection"
        v-model="taskStore.taskForm.params[param.name]"
        :label="param.desc"
        item-title="desc"
        item-value="name"
        variant="outlined"
      ></v-select>

      <!-- 如果 childParams 不为空，递归调用 ParamInput 组件 -->

        <ParamsInput
          v-if="param.selection.length > 0 && !!taskStore.taskForm.params[param.name]"
          :params="childParams(taskStore.taskForm.params[param.name], param.selection)"
          :key="taskStore.taskForm.params[param.name]"
        />
    </v-col>
  </v-row>
</template>

<script setup lang="ts">
import { useTaskStore,snackbarStore } from "@/views/workplace/task/taskStore"
import {onMounted} from "vue";
import {Parameter} from "@/views/workplace/task/taskTypes";

const taskStore = useTaskStore()


interface Props {
  params: Parameter[];
}

const props = defineProps<Props>();

onMounted(()=>{
  props.params = props.params.sort((a, b) => {
    // 将 selection.length > 0 的项排在前面
    return (a.selection.length > 0 ? 1 : 0) - (b.selection.length > 0 ? 1 : 0);
  })

  props.params.forEach((param)=>{
    if(param.required){
      taskStore.taskForm.params[param.name] = param.defaultValue
    }
  })
})

const childParams = (paramName, childParamsList) =>{
  return childParamsList.find(param=>param.name===paramName)?.params ?? []
}

const computeInputWidth = (param, index) =>{
  if(param.selection.length!=0){
    return 12
  }else{
    return index == props.params.length - getSelectionParamsNums() -1?12:6;
  }
}

const getSelectionParamsNums = () =>{
  return props.params.filter(params => params.selection.length != 0).length;
}

watch(
  () => taskStore.taskForm.params, // 或 `taskStore.taskForm.params[param.name]`
  (newVal, oldVal) => {

  },
  { deep: true } // 深度监听以捕捉对象内部变化
);

// 在组件卸载之前清空 taskStore.taskForm.params
onBeforeUnmount(() => {
  // 清空 当前组件下的param
  props.params.forEach(
    param=>{
      delete taskStore.taskForm.params[param.name];
    }
  )
});
</script>
