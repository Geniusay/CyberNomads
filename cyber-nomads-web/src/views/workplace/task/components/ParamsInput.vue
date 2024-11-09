<template>
  <v-row dense>
    <v-col v-for="(param, index) in params" :key="param.name"
           :cols=computeInputWidth(param,index)
           :md=computeInputWidth(param,index)
    >
      <!-- 判断 childParams 是否为空 -->
      <v-text-field
        v-if="param.inputType === InputType.input"
        :label="param.desc"
        v-model="taskStore.taskForm.params[param.name]"
        :rules="[required(taskStore.taskForm.params[param.name], param.required)]"
        variant="outlined"
      ></v-text-field>

      <v-textarea
        v-if="param.inputType === InputType.textarea"
        :label="param.desc"
        v-model="taskStore.taskForm.params[param.name]"
        :rules="[required(taskStore.taskForm.params[param.name], param.required)]"
        variant="outlined">
      </v-textarea>

      <v-select
        v-if="param.inputType === InputType.selection"
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
          v-if="param.inputType === InputType.selection && !!taskStore.taskForm.params[param.name]"
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
import {InputType} from "@/views/workplace/task/taskListConfig";

const taskStore = useTaskStore()


interface Props {
  params: Parameter[];
}

const props = defineProps<Props>();

onMounted(()=>{
  props.params = props.params.sort((a, b) => {
    // 将 selection.length > 0 的项排在前面
    return (b.selection.length == 0 ? 1 : 0) - (a.selection.length == 0 ? 1 : 0);
  })

  console.log(props.params)

  props.params.forEach((param)=>{
    if(param.required&&!taskStore.taskForm.params[param.name]){
      taskStore.taskForm.params[param.name] = param.defaultValue
    }
  })
})

const childParams = (paramName, childParamsList) =>{
  return childParamsList.find(param=>param.name===paramName)?.params ?? []
}

const computeInputWidth = (param, index) =>{
  if(param.inputType===InputType.input){
    return index == getInputParamsNums()-1?12:6;
  }else{
    return 12
  }
}

const getInputParamsNums = () =>{
  return props.params.filter(params => params.inputType === InputType.input).length;
}

const required = (v,required) =>{
  return required && !v ? 'This field is required' : true;
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
