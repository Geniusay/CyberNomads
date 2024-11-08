import { defineStore } from "pinia";
import { useSnackbarStore } from "@/stores/snackbarStore";
import { getRobotList } from "@/api/robotApi";
import { RobotVO, RobotForm, defaultValue } from "@/views/workplace/robot/robotTypes";


export const snackbarStore = useSnackbarStore();
export const useRobotStore = defineStore({
  id: "robotStore",

  state: ()=>({
    robotList: ref<RobotVO[]>([]),
  }),
  getters:{
    getRobotList(){
      return this.robotList
    },
    getPlatformRobotList(){

    }
  },

  actions:{
    async initRobotList(){
      await getRobotList().then(res=>{
        this.robotList = res.data as RobotVO[]
      }).catch(error=>{
        snackbarStore.showErrorMessage("获取赛博游民列表，网络异常")
      })
    },
  }
})
