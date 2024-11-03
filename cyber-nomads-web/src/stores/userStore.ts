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
        this.userInfo = res.data as UserVO
        const user = JSON.parse(localStorage.getItem("cyberUser"))
        user['userInfo'] = this.userInfo
        localStorage.setItem("cyberUser",JSON.stringify(user))
      }).catch(error=>{
        this.clearUserInfo()
        snackbarStore.showErrorMessage("登录过期，请重新登录!")
        router.push({path:'/login'})
      })
    }
  }
})
