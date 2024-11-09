import { defineStore } from "pinia";
import { useSnackbarStore } from "@/stores/snackbarStore";
import {getPlatforms} from "@/api/commonApi";
import {PlatformVO} from "@/types/platformType";


const snackbarStore = useSnackbarStore();
export const useCommonStore = defineStore({
  id: "commonStore",
  state: ()=>({
    platformList: [],
  }),
  getters:{
    getPlatformList(){
      return this.platformList;
    },
    getPlatformCnZh(){
      return (code)=>this.platformList.find(item => item.code === code).platformCnZh
    },
    getPlatformImgUrl(){
      return (platform:string)=>'/assets/svg/'+platform+'.svg';
    }
  },
  actions:{
    async initPlatformsVO(){
      if(this.platformList.length==0){
        await getPlatforms().then(res=>{
          this.platformList = res.data as PlatformVO[]
        }).catch(error=>{
          snackbarStore.showErrorMessage("获取平台列表，网络异常")
        })
      }
    },
    findPlatformByCnZh(platformCnZh: string): PlatformVO | undefined {
      return this.platformList .find(item => item.platformCnZh === platformCnZh);
    }
  }
})
