import { defineStore } from "pinia";
import { UserVO } from "@/types/userType";
import { getUserInfo } from "@/api/userApi"
import { useSnackbarStore } from "@/stores/snackbarStore";
import { useRouter } from "vue-router";

const snackbarStore = useSnackbarStore();
export const useUserStore = defineStore({
  id: "userStore",
  state: ()=>({
    userInfo: <UserVO>{},
    token:""
  }),
  getters:{
    getUserInfo(): UserVO{
      return this.userInfo
    }
  },
  actions:{
    setUserInfo(userInfo: UserVO, token: string){
      this.userInfo = userInfo
      this.token = token
    },
    clearUserInfo(){
      this.userInfo = {}
      this.token = ""
    },
    fetchUserInfo(){
      const router = useRouter();
      if(!this.userInfo){
        getUserInfo().then(res=>{
          if(res.code==="200"){
            this.userInfo = res.data as UserVO
          }else{
            this.clearUserInfo()
            snackbarStore.showErrorMessage("登录过期，请重新登录!")
            router.push("/login")
          }
        })
      }
    }
  },
  persist: {
    enabled: true,
    strategies: [{
      key:"userStore",
      storage: localStorage,
      paths: ["userInfo", "token"]
    }]
  }
})
