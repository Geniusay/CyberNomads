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
      localStorage.setItem("cyberUser",JSON.stringify({userInfo: userInfo, token: token}))
    },
    logout(){
      const router = useRouter();
      this.clearUserInfo()
      router.push({path:'/login'})
    },
    clearUserInfo(){
      this.userInfo = {}
      this.token = ""
      localStorage.removeItem("cyberUser")
    },
    fetchUserInfo(){
      const router = useRouter();
      getUserInfo().then(res=>{
        if(res.code==="200"){
          this.userInfo = res.data as UserVO
        }else{
          this.clearUserInfo()
          snackbarStore.showErrorMessage("登录过期，请重新登录!")
          router.push({path:'/login'})
        }
      })
    }
  },
  persist: {
    enabled: true,
    strategies: [{
      key:"cyberUser",
      storage: localStorage,
      paths: ["userInfo", "token"]
    }]
  }
})
