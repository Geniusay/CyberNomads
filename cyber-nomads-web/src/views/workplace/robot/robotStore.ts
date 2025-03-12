import { defineStore } from "pinia";
import { useSnackbarStore } from "@/stores/snackbarStore";
import { getRobotList } from "@/api/robotApi";
import { RobotVO } from "@/views/workplace/robot/robotTypes";


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

    },
    getSharedRobotList(){
      return (robotList: RobotVO[])=>{
        robotList.filter(item=>{
          item.hasShared && this.robotList.some(robot => robot.id === item.id)
        })
      }
    },
    isSharedRobot(){
      return (robotId: string)=>{
        !this.robotList.some(robot => robot.id === robotId)
      }
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
