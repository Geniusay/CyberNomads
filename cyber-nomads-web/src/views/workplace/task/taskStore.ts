import { defineStore } from "pinia";
import { useSnackbarStore } from "@/stores/snackbarStore";
import {getTaskList, getPlatformTaskType, createTask, updateTask, deleteTask} from "@/api/taskApi";
import {defaultValue, TaskForm, TaskType, TaskVO} from "@/views/workplace/task/taskTypes";


export const snackbarStore = useSnackbarStore();
export const useTaskStore = defineStore({
  id: "taskStore",

  state: ()=>({
    taskList: ref<TaskVO[]>([]),
    taskDialog: ref(false),
    isEdit: ref(false),
    platformTaskTypeMap:ref<Record<string, TaskType[]>>({}),
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
          this.platformTaskTypeMap[platform] = res.data as TaskType[]
        }).catch(error=>{
          snackbarStore.showErrorMessage(error.message)
        })
      }
    },
    async createTask(taskForm: TaskForm){
      await createTask(taskForm).then(res=>{
        snackbarStore.showSuccessMessage("添加成功")
        this.taskList.value.push({ ...res.data, taskStatus: (res.data as TaskVO).taskStatus.toLowerCase() });
      }).catch(error=>{
        snackbarStore.showErrorMessage("添加失败："+error.message)
      })
    },
    async updateTask(taskForm: TaskForm){
      await updateTask(taskForm).then(res=>{
        snackbarStore.showSuccessMessage("更新成功")
        const index = this.taskList.value.findIndex(item => item.id === taskForm.taskId)
        console.log(index)
        this.taskList.value[index] = { ...res.data, taskStatus: (res.data as TaskVO).taskStatus.toLowerCase() };
        console.log(this.taskList.value[index])
        console.log({ ...res.data, taskStatus: (res.data as TaskVO).taskStatus.toLowerCase() })
      }).catch(error=>{
        snackbarStore.showErrorMessage("更新失败："+error.message)
      })
    },
    async deleteTask(item: TaskVO){
      await deleteTask(item.id).then(res=>{
        snackbarStore.showSuccessMessage(item.taskName+"已成功删除")
        const index = this.taskList.value.findIndex(task => task.id === item.id);
        this.taskList.value.splice(index, 1);
      }).catch(error=>{
        snackbarStore.showErrorMessage(item.taskName+"删除失败："+error.message)
      })
    }
  }
})
