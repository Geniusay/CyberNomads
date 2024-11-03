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
      this.clearUserInfo()
    },
    clearUserInfo(){
      this.userInfo = {}
      this.token = ""
      localStorage.removeItem("cyberUser")
    },
    async fetchUserInfo(){
      const router = useRouter();
      await getUserInfo().then(res=>{
        this.setUserInfo(res.data as UserVO, this.token)
      }).catch(error=>{
        //this.clearUserInfo()
        console.log(error)
        snackbarStore.showErrorMessage("登录过期，请重新登录!")
        //router.push({path:'/login'})
      })
    }
  }
})
