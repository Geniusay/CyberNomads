import { defineStore } from "pinia";
import { useSnackbarStore } from "@/stores/snackbarStore";
import { getTaskList,getPlatformTaskType } from "@/api/taskApi";
import {defaultValue, TaskForm, TaskVO} from "@/views/workplace/task/taskTypes";


export const snackbarStore = useSnackbarStore();
export const useTaskStore = defineStore({
  id: "taskStore",

  state: ()=>({
    taskList: ref<TaskVO[]>([]),
    taskDialog: ref(false),
    isEdit: ref(false),
    platformTaskTypeMap:ref({}),
    taskForm: ref<TaskForm>({...defaultValue.defaultTaskForm})
  }),
  getters:{
    getTaskList(){
      return this.taskList;
    },
    getTaskDialog(){
      return this.taskDialog;
    },
    getIsEdit(){
      return this.isEdit;
    },
    getPlatformTaskTypes(){
      return (platform)=>this.platformTaskTypeMap[platform]
    },
    getTaskForm(){
      return this.taskForm
    }
  },

  actions:{
    async initTaskList(){
      await getTaskList().then(res=>{
        this.taskList.value = (res.data as TaskVO[]).map(task => ({
          ...task,
          taskStatus: task.taskStatus.toLowerCase()
        }));
      }).catch(error=>{
        snackbarStore.showErrorMessage(error)
      })
    },
    openDialog(){
      this.taskDialog = true
    },
    closeDialog(){
      this.taskDialog = false
    },
    changeDialogMode(isEdit: boolean){
      this.isEdit = isEdit
    },
    async initPlatformTaskTypes(platform:string){
      if(!(platform in this.platformTaskTypeMap)){
        await getPlatformTaskType(platform).then(res=>{
          this.platformTaskTypeMap[platform] = res.data
        }).catch(error=>{
          snackbarStore.showErrorMessage(error.message)
        })
      }
    }
  }
})
