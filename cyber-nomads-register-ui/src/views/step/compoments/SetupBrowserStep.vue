<template>
  <div>
    <v-text-field
        v-model="form.drivePath"
        prepend-icon="mdi-earth"
        label="webDriver路径"
    ></v-text-field>
    <v-text-field
        v-model="form.browserPath"
        prepend-icon="mdi-engine"
        label="浏览器路径"
    ></v-text-field>

    <div class="d-flex flex-column" style="margin: 15px">
      <v-btn
          class="mt-4"
          color="success"
          block
          @click="validate"
      >
        保存信息
      </v-btn>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {useSnackbarStore} from "@/stores/snackbarStore";
import {getPath, savePath} from  "@/api/cloudApi";
import {useStepStore} from "@/stores/stepStore";
const stepStore = useStepStore();

onMounted(async ()=>{
  await getPath().then(res=>{
    if(res.data){
      form.value.drivePath = res.data.pathDTO.driverPath
      form.value.browserPath = res.data.pathDTO.browserPath
      if(res.data.errorMsg){
        snackbarStore.showErrorMessage(res.data.errorMsg)
        return;
      }else{
        snackbarStore.showSuccessMessage('本地配置校验正确')
        setTimeout(()=>{
          validate()
        }, 500)

      }
    }
  })
})

const snackbarStore = useSnackbarStore();
const form = ref({
  drivePath:"",
  browserPath:""
})

const validate =async () => {
  if (form.drivePath==="" || form.browserPath==="") {
    snackbarStore.showErrorMessage("请填写正确的路径")
    return
  }
  await savePath(form.value.drivePath, form.value.browserPath).then(res=>{
    if(res.code==="200"){
      snackbarStore.showSuccessMessage('本地配置文件校验正确')
      next()
    }else{
      snackbarStore.showErrorMessage('保存配置信息失败')
    }
  })
}

const next = () =>{
  stepStore.changeValid(stepStore.step)
  stepStore.step++;
}

</script>
